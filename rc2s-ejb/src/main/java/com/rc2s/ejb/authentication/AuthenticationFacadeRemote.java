package com.rc2s.ejb.authentication;

import javax.ejb.Remote;

@Remote
public interface AuthenticationFacadeRemote
{
    public void login();
}
