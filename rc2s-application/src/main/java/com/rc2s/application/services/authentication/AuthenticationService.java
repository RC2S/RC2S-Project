package com.rc2s.application.services.authentication;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.utils.Hash;
import com.rc2s.common.vo.User;
import com.rc2s.dao.user.IUserDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AuthenticationService implements IAuthenticationService
{  
    @EJB
    private IUserDAO userDAO;
    
    private final String SALT = "c33A0{-LO;<#CB `k:^+8DnxAa.BX74H07z:Qn+U0yD$3ar+.:ge[nc>Trs|Fxy";
	private final String PEPPER = ">m9I}JqHTg:VZ}XISdcG;)yGu)t]7Qv5YT:ZWI^#]f06Aq<c]n7a? x+ZEl#pt:";
    
    @Override
    @Deprecated
    public boolean loginJaas(final String username, final String password)
    {
        System.setProperty("java.security.auth.login.config", AuthenticationService.class.getResource("/jaas.config").toString());

        try {
            LoginContext lc = new LoginContext(
                "JDBCLoginModule",
                new JDBCCallbackHandler(username, password)
            );
            
            lc.login();
            
            Subject subject = lc.getSubject();
            
            return subject != null;
        }
        catch (LoginException ex)
        {
            Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
	public User login(final String username, final String password) throws ServiceException
	{
		try
		{
			User user = null;
			
			if(username != null && password != null)
			{
				String securedPassword = Hash.sha1(SALT + password + PEPPER);
				user = userDAO.getAuthenticatedUser(username, securedPassword);
				
				if(user != null)
				{
					int code = userDAO.setLastLogin(user);
				
					if(code != 1)
					{
						System.err.println("Unable to update user's last IP address. Return code is: " + code);
					}
				}
			}
			
			return user;
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
}


/*
https://www.javacodegeeks.com/2012/06/java-jaas-form-based-authentication.html
http://www.javaworld.com/article/2074873/java-web-development/all-that-jaas.html?page=2
http://www.radcortez.com/custom-principal-and-loginmodule-for-wildfly/
http://byorns.blogspot.fr/2015/01/how-to-setup-custom-jaas-login-module.html
http://www.edc4it.com/blog/java/understanding-java-security-and-jaas-part-3-a-custom-login-module.html
https://github.com/martijnblankestijn/glassfish-jdbc-realm
*/