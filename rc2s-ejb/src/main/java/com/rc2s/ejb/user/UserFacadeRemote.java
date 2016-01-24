package com.rc2s.ejb.user;

import javax.ejb.Remote;

@Remote
public interface UserFacadeRemote
{
    public String getAllUsers();
}
