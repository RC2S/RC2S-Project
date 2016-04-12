package com.rc2s.ejb.authentication;

import com.rc2s.application.services.authentication.IAuthenticationService;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless(mappedName = "AuthenticationEJB")
public class AuthenticationFacadeBean implements AuthenticationFacadeRemote
{
    @Inject
    IAuthenticationService authenticationService;
    
    @Override
    public boolean login()
    {
        return authenticationService.login();
    }
}
