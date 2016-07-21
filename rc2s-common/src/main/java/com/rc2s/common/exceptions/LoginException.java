package com.rc2s.common.exceptions;

/**
 * LoginException
 * 
 * RC2S exception raised during login process
 * 
 * @author RC2S
 */
public class LoginException extends RC2SException
{
    public LoginException(final Throwable t)
    {
        super(t);
    }
    
    public LoginException(final String msg)
    {
        super(msg);
    }
}
