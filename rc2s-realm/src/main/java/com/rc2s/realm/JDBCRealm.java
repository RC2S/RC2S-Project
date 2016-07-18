package com.rc2s.realm;

import com.sun.appserv.connectors.internal.api.ConnectorRuntime;
import com.sun.appserv.security.AppservRealm;
import com.sun.enterprise.security.BaseRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;
import com.sun.enterprise.security.auth.realm.Realm;
import com.sun.enterprise.security.common.Util;
import com.sun.logging.LogDomains;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.utilities.BuilderHelper;

public class JDBCRealm extends AppservRealm
{
    protected static final Logger LOG = LogDomains.getLogger(Realm.class,
			LogDomains.SECURITY_LOGGER);

    public static final String AUTH_TYPE = "jdbc-salted-peppered";
	private final Map<String, Vector<String>> groupCache = new HashMap<>();
	private String passwordQuery    = null;
	private String groupQuery       = null;
	private PasswordHash passwordHash = null;

	private ActiveDescriptor<ConnectorRuntime> cr;

	/**
	 * Initialize a realm with some properties. This can be used when
	 * instantiating realms from their descriptions. This method may only be
	 * called a single time.
	 * 
	 * @param props
	 *            Initialization parameters used by this realm.
	 * @exception BadRealmException
	 *                If the configuration parameters identify a corrupt realm.
	 * @exception NoSuchRealmException
	 *                If the configuration parameters specify a realm which
	 *                doesn't exist.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public synchronized void init(Properties props) throws BadRealmException,
			NoSuchRealmException
    {
		super.init(props);

		String jaasCtx          = props.getProperty(BaseRealm.JAAS_CONTEXT_PARAM);
		String dbUser           = props.getProperty(Params.DB_USER);
		String dbPassword       = props.getProperty(Params.DB_PASSWORD);
		String dsJndi           = props.getProperty(Params.DATASOURCE_JNDI);
		String charset          = props.getProperty(Params.CHARSET);
		String userTable        = props.getProperty(Params.USER_TABLE);
		String userNameColumn   = props.getProperty(Params.USER_NAME_COLUMN);
		String passwordColumn   = props.getProperty(Params.PASSWORD_COLUMN);
		String groupTable       = props.getProperty(Params.GROUP_TABLE);
		String groupNameColumn  = props.getProperty(Params.GROUP_NAME_COLUMN);
        String linkUserGrpTable = props.getProperty(Params.LINK_USER_GROUP_TABLE);
        String linkUserColumn   = props.getProperty(Params.LINK_USER_COLUMN);
        String linkGroupColumn  = props.getProperty(Params.LINK_GROUP_COLUMN);
		String groupTableUserNameColumn = props.getProperty(
				Params.GROUP_TABLE_USER_NAME_COLUMN, userNameColumn);
        String salt             = props.getProperty(Params.SALT);
        String pepper           = props.getProperty(Params.PEPPER);
		
        cr = (ActiveDescriptor<ConnectorRuntime>) Util.getDefaultHabitat()
				.getBestDescriptor(
						BuilderHelper
								.createContractFilter(ConnectorRuntime.class
										.getName()));
		
        checkPropertySet(jaasCtx, JAAS_CONTEXT_PARAM);
		checkPropertySet(dsJndi, Params.DATASOURCE_JNDI);
		checkPropertySet(userTable, Params.USER_TABLE);
		checkPropertySet(groupTable, Params.GROUP_TABLE);
		checkPropertySet(userNameColumn, Params.USER_NAME_COLUMN);
		checkPropertySet(passwordColumn, Params.PASSWORD_COLUMN);
		checkPropertySet(groupNameColumn, Params.GROUP_NAME_COLUMN);
        checkPropertySet(salt, Params.SALT);
        checkPropertySet(pepper, Params.PEPPER);
        
        passwordHash = new PasswordHash(salt, pepper);

		passwordQuery = String.format("SELECT %s FROM %s WHERE %s = ? AND locked = 0 AND activated = 1",
				passwordColumn, userTable, userNameColumn);
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
          .append("g." + groupNameColumn + " ")
          .append("FROM ")
          .append("`" + groupTable + "` AS g ")
          .append("INNER JOIN `" + linkUserGrpTable + "` AS lug ON lug." + linkGroupColumn + " = g.id ")
          .append("INNER JOIN `" + userTable + "` AS u ON u.id = lug." + linkUserColumn + " AND u." + userNameColumn + " = ?");

        groupQuery = sb.toString();

		this.setProperty(BaseRealm.JAAS_CONTEXT_PARAM, jaasCtx);
        
		if (dbUser != null && dbPassword != null) {
			this.setProperty(Params.DB_USER, dbUser);
			this.setProperty(Params.DB_PASSWORD, dbPassword);
		}
		
        this.setProperty(Params.DATASOURCE_JNDI, dsJndi);
		
        if (charset != null)
			this.setProperty(Params.CHARSET, charset);

        LOG.info(getClass().getSimpleName() + ": "
                + BaseRealm.JAAS_CONTEXT_PARAM + "= " + jaasCtx + ", "
                + Params.DATASOURCE_JNDI + " = " + dsJndi + ", " + Params.DB_USER + " = "
                + dbUser + ", " + Params.CHARSET + " = " + charset + ", "
                + passwordHash);
	}

	private void checkPropertySet(String paramValue, String paramName)
			throws BadRealmException
    {
		if (paramValue == null)
        {
			String msg = sm.getString("realm.missingprop", paramName,
					"JDBCRealm");
			throw new BadRealmException(msg);
		}
	}

	@Override
	public String getAuthType()
    {
		return AUTH_TYPE;
	}

	/**
	 * Returns the name of all the groups that this user belongs to. It loads
	 * the result from groupCache first. This is called from web path group
	 * verification, though it should not be.
	 * 
	 * @param username
	 *            Name of the user in this realm whose group listing is needed.
	 * @return Enumeration of group names (strings).
	 * @exception InvalidOperationException
	 *                thrown if the realm does not support this operation - e.g.
	 *                Certificate realm does not support this operation.
	 */
	@Override
	public Enumeration<String> getGroupNames(String username)
			throws InvalidOperationException, NoSuchUserException
    {
		Vector<String> vector = groupCache.get(username);
		
        if (vector == null)
        {
			String[] grps = findGroups(username);
			setGroupNames(username, grps);
			vector = groupCache.get(username);
		}
        
		return vector.elements();
	}

	private void setGroupNames(String username, String[] groups)
    {
		Vector<String> v = new Vector<>();
		for (String group : groups) {
			v.add(group);
		}

		synchronized (this) {
			groupCache.put(username, v);
		}
	}

	/**
	 * Invoke the native authentication call.
	 * 
	 * @param username
	 *            User to authenticate.
	 * @param password
	 *            Given password.
	 * @return true of false, indicating authentication status.
	 * 
	 */
	public String[] authenticate(String username, char[] password)
    {
        LOG.log(Level.FINE, "authenticating username={0}", username);
		String[] groups = null;
		
        if (isUserValid(username, password))
        {
			groups = findGroups(username);
			groups = addAssignGroups(groups);
			setGroupNames(username, groups);
		}
        
		return groups;
	}

	private String getPasswordHash(String username)
    {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection
						.prepareStatement(passwordQuery))
        {
			statement.setString(1, username);
			try (ResultSet rs = statement.executeQuery())
            {
				if (rs.next())
					return rs.getString(1);
			}
		}
        catch (Exception ex)
        {
			LOG.log(Level.SEVERE, "jdbcrealm.invaliduser", username);
			LOG.log(Level.SEVERE, null, ex);
		}
        
		return null;
	}

	private boolean isUserValid(String user, char[] password)
    {
		boolean valid = false;

		try
        {
			String correctHash = getPasswordHash(user);
			valid = passwordHash.compareHash(new String(password), correctHash);
		}
        catch (Exception ex)
        {
			LOG.log(Level.SEVERE, "jdbcrealm.invaliduser", user);
			if (LOG.isLoggable(Level.FINE))
				LOG.log(Level.FINE, "Cannot validate user", ex);
		}
		return valid;
	}

	private String[] findGroups(String user)
    {
		ResultSet rs = null;
        
		try (Connection connection = getConnection();
				PreparedStatement statement = connection
						.prepareStatement(groupQuery))
        {
			statement.setString(1, user);
			rs = statement.executeQuery();
            
			return toArrayAndClose(rs, 1);
		}
        catch (Exception ex)
        {
			LOG.log(Level.SEVERE, "jdbcrealm.grouperror", user);
			LOG.log(Level.SEVERE, "Cannot load group", ex);
            
			return null;
		}
	}

	private String[] toArrayAndClose(ResultSet resultSet, int columnNr)
			throws SQLException
    {
		try (ResultSet rs = resultSet)
        {
			final List<String> result = new ArrayList();
            
			while (rs.next())
				result.add(rs.getString(columnNr));
            
			final String[] groupArray = new String[result.size()];
            
			return result.toArray(groupArray);
		}
	}

	/**
	 * Return a connection from the properties configured
	 * 
	 * @return a connection
	 */
	private Connection getConnection() throws LoginException
    {
		final String dsJndi     = this.getProperty(Params.DATASOURCE_JNDI);
		final String dbUser     = this.getProperty(Params.DB_USER);
		final String dbPassword = this.getProperty(Params.DB_PASSWORD);
        
		try
        {
			ConnectorRuntime connectorRuntime = Util.getDefaultHabitat()
					.getServiceHandle(cr).getService();
            
			final DataSource dataSource = (DataSource) connectorRuntime
					.lookupNonTxResource(dsJndi, false);
            
			Connection connection = null;
			
            if (dbUser != null && dbPassword != null)
				connection = dataSource.getConnection(dbUser, dbPassword);
			else
				connection = dataSource.getConnection();

			return connection;
		}
        catch (Exception ex)
        {
			String msg = sm.getString("jdbcrealm.cantconnect", dsJndi, dbUser);
			LoginException loginEx = new LoginException(msg);
			loginEx.initCause(ex);
			throw loginEx;
		}
	}   
}
