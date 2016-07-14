package com.rc2s.realm;

import com.sun.enterprise.security.BasePasswordLoginModule;
import com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm;
import java.util.Arrays;
import java.util.logging.Level;
import javax.security.auth.login.LoginException;

public class JDBCLoginModule extends BasePasswordLoginModule
{

    @Override
	protected void authenticateUser() throws LoginException
    {
		if (!(getCurrentRealm() instanceof JDBCRealm))
        {
			String msg = sm.getString("jdbclm.badrealm");
			throw new LoginException(msg);
		}

		final JDBCRealm jdbcRealm = (JDBCRealm) getCurrentRealm();

		final String username = getUsername();
		if ((username == null) || (username.length() == 0))
        {
			String msg = sm.getString("jdbclm.nulluser");
			throw new LoginException(msg);
		}

		String[] grpList = jdbcRealm.authenticate(username, getPasswordChar());

		if (grpList == null) // JAAS behavior
        {
			String msg = sm.getString("jdbclm.loginfail", username);
			throw new LoginException(msg);
		}

		if (_logger.isLoggable(Level.FINEST))
        {
			_logger.finest("JDBC login succeeded for: " + username + " groups:"
					+ Arrays.toString(grpList));
		}

		commitUserAuthentication(grpList);
	}
}
