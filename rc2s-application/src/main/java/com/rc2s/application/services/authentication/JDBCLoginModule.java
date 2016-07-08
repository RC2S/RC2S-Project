package com.rc2s.application.services.authentication;

import com.rc2s.common.vo.User;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class JDBCLoginModule implements LoginModule
{
    private Subject subject;
    
    private CallbackHandler callbackHandler;
    
    private Map sharedState;
    
    private Map options;
    
    private User foundUser;
    
    private UserPrincipal userPrincipal;
    
    private boolean succeeded = false;
    
    private boolean commitSucceeded = false;
    
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
        Map<String, ?> sharedState, Map<String, ?> options)
    {
        this.subject            = subject;
        this.callbackHandler    = callbackHandler;
        this.sharedState        = sharedState;
        this.options            = options;
    }

    @Override
    public boolean login() throws LoginException
    {
        if (callbackHandler == null)
			throw new LoginException("The callbackHandler is null");

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("name:");
		callbacks[1] = new PasswordCallback("password:", false);

		try
        {
			callbackHandler.handle(callbacks);
		}
        catch (IOException e)
        {
			throw new LoginException("IOException calling handle on callbackHandler");
		}
        catch (UnsupportedCallbackException e)
        {
			throw new LoginException("UnsupportedCallbackException calling handle on callbackHandler");
		}

		NameCallback nameCallback           = (NameCallback) callbacks[0];
		PasswordCallback passwordCallback   = (PasswordCallback) callbacks[1];

		String name = nameCallback.getName();
		String password = new String(passwordCallback.getPassword());

		if ((foundUser = getUserByUsernameAndPassword(name, password)) != null)
        {
			succeeded = true;
			return succeeded;
		}
        else
        {
			succeeded = false;
			throw new FailedLoginException("Sorry! No login for you.");
		}
    }

    @Override
    public boolean commit() throws LoginException
    {
        if (!succeeded || foundUser == null)
            return false;
        
        userPrincipal = new UserPrincipal(foundUser.getUsername());
        
        if (!subject.getPrincipals().contains(userPrincipal))
            subject.getPrincipals().add(userPrincipal);
        
        foundUser = null;
        commitSucceeded = true;
        return true;
    }

    @Override
    public boolean abort() throws LoginException
    {
        if(!succeeded)
        {
            return false;
        }
        else if (succeeded && !commitSucceeded)
        {
            succeeded = false;
            userPrincipal = null;
        }
        else
        {
            logout();
        }
        return true;
    }

    @Override
    public boolean logout() throws LoginException
    {
        subject.getPrincipals().remove(userPrincipal);
        succeeded = false;
        commitSucceeded = false;
        if(foundUser != null)
            foundUser = null;
        userPrincipal = null;
        return true;
    }
    
    @SuppressWarnings("unchecked")
    private User getUserByUsernameAndPassword(final String username, final String password)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("rc2s");
        EntityManager em = emf.createEntityManager();
        
        Query query = em.createQuery("SELECT u from User as u WHERE u.username=:username AND u.password=:password")
                .setParameter("username", username)
                .setParameter("password", password);
        List<User> users = query.getResultList();
        if(!users.isEmpty())
            return users.get(0);
        
        return null;
    }
}
