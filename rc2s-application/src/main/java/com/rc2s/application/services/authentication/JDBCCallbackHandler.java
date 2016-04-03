package com.rc2s.application.services.authentication;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class JDBCCallbackHandler implements CallbackHandler
{
    private String username;
    private String password;
    
    public JDBCCallbackHandler(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        
    }
}
