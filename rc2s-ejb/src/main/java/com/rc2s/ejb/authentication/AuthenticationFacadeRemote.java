package com.rc2s.ejb.authentication;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.User;
import javax.ejb.Remote;

@Remote
public interface AuthenticationFacadeRemote
{
    public User login(String username, String password) throws EJBException;
}
