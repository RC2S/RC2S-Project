package com.rc2s.ejb.authentication;

import com.rc2s.common.vo.User;
import javax.ejb.Remote;

@Remote
public interface AuthenticationFacadeRemote
{
    public boolean login(String username, String password);
}
