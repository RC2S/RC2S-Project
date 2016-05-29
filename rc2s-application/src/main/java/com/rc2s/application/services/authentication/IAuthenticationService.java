package com.rc2s.application.services.authentication;

import javax.ejb.Local;

@Local
public interface IAuthenticationService
{
    public boolean login(String username, String password);
}
