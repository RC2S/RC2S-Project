package com.rc2s.ejb.authentication;

import com.rc2s.application.services.authentication.IAuthenticationService;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "AuthenticationEJB")
public class AuthenticationFacadeBean implements AuthenticationFacadeRemote
{
    @EJB
    IAuthenticationService authenticationService;
    
    @Override
    public boolean login()
    {
        return authenticationService.login();
    }
}
