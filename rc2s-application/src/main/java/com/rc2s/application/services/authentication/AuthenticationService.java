package com.rc2s.application.services.authentication;

import com.rc2s.common.vo.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

@Stateless
public class AuthenticationService implements IAuthenticationService
{
    @Override
    public boolean login(User user)
    {
        System.setProperty("java.security.auth.login.config", AuthenticationService.class.getResource("/jaas.config").toString());

        try {
            LoginContext lc = new LoginContext(
                "JDBCLoginModule",
                new JDBCCallbackHandler(user.getUsername(), user.getPassword())
            );
            
            lc.login();
            
            Subject subject = lc.getSubject(); 
            //LoginContext lc = new LoginContext();
            //lc.login(user.getUsername(), user.getPassword());
        }
        catch (LoginException ex)
        {
            Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
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