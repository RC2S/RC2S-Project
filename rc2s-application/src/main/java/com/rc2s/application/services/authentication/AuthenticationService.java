package com.rc2s.application.services.authentication;

import com.rc2s.common.utils.Hash;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

@Stateless
public class AuthenticationService implements IAuthenticationService
{
    private final String SALT = "c33A0{-LO;<#CB `k:^+8DnxAa.BX74H07z:Qn+U0yD$3ar+.=:ge[nc>Trs|Fxy";
	private final String PEPPER = ">m9I}JqHTg:VZ}XISdcG;)yGu)t]7Qv5YT:ZWI^#]f06Aq<c]n7a? x+=ZEl#pt:";
    
    @Override
    public boolean login(String username, String password)
    {
        System.setProperty("java.security.auth.login.config", AuthenticationService.class.getResource("/jaas.config").toString());

        try {
            LoginContext lc = new LoginContext(
                "JDBCLoginModule",
                new JDBCCallbackHandler(username, Hash.sha1(SALT + password + PEPPER))
            );
            
            lc.login();
            
            Subject subject = lc.getSubject();
            
            InitialContext ic = new InitialContext();
            SessionContext sessionContext = (SessionContext) ic.lookup("java:comp/EJBContext");
            
            for(Principal p : subject.getPrincipals()) {
                System.out.println("Princ " + p.getName());
            }
            System.out.println("before add");
            sessionContext.getContextData().put("Principal", subject.getPrincipals());
            System.out.println("after add");
            return subject != null;
        }
        catch (LoginException ex)
        {
            Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex)
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