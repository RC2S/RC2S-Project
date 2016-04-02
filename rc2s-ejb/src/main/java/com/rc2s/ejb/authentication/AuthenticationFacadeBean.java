package com.rc2s.ejb.authentication;

import javax.ejb.Stateless;

@Stateless(mappedName = "AuthenticationEJB")
public class AuthenticationFacadeBean implements AuthenticationFacadeRemote
{
    @Override
    public void login()
    {
        
    }
}
