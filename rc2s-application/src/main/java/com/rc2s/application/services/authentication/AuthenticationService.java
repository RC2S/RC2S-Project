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
        System.setProperty("java.security.auth.login.config", "jaas.config");
        
        try {
            LoginContext lc = new LoginContext(
                "JDBCLoginModule",
                new JDBCCallbackHandler(user.getUsername(), user.getPassword())
            );
            
            lc.login();
            
            Subject subject = lc.getSubject();
            
        } catch (LoginException ex) {
            Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE,
                null, ex);
        }
        return false;
    }
}
