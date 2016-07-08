package com.rc2s.application.services.authentication;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class JDBCCallbackHandler implements CallbackHandler
{
    private final String username;
    private final String password;
    
    public JDBCCallbackHandler(final String username, final String password)
    {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        for(int i = 0; i < callbacks.length; i++)
        {
			if(callbacks[i] instanceof NameCallback)
            {
				NameCallback nameCallback = (NameCallback) callbacks[i];
				nameCallback.setName(username);
			}
            else if(callbacks[i] instanceof PasswordCallback)
            {
				PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
				passwordCallback.setPassword(password.toCharArray());
			}
            else
            {
				throw new UnsupportedCallbackException(callbacks[i], "The submitted Callback is unsupported");
			}
		}
    }
}
