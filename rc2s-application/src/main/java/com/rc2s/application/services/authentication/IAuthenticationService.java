package com.rc2s.application.services.authentication;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.User;
import javax.ejb.Local;

@Local
public interface IAuthenticationService
{
    public User login(String username, String password) throws ServiceException;
    
    public boolean loginJaas(String username, String password);
}
