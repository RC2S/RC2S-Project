package com.rc2s.common.exceptions;

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
