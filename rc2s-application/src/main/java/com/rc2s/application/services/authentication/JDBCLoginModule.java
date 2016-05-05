package com.rc2s.application.services.authentication;

import java.io.IOException;
import java.util.Map;
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
    
    private boolean succeeded = false;
    
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
        Map<String, ?> sharedState, Map<String, ?> options)
    {
        this.subject            = subject;
        this.callbackHandler    = callbackHandler;
        this.sharedState        = sharedState;
        this.options            = options;
        succeeded               = false;
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

		if ("myName".equals(name) && "myPassword".equals(password))
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
        return succeeded;
    }

    @Override
    public boolean abort() throws LoginException
    {
        return false;
    }

    @Override
    public boolean logout() throws LoginException
    {
        
        return false;
    }
}
