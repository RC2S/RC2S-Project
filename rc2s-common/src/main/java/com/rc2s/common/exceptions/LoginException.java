package com.rc2s.common.exceptions;

public class LoginException extends RC2SException
{
    public LoginException(Throwable t)
    {
        super(t);
    }
    
    public LoginException(String msg)
    {
        super(msg);
    }
}
