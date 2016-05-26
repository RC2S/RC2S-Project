package com.rc2s.application.services.authentication;

import com.rc2s.common.vo.User;
import javax.ejb.Local;

@Local
public interface IAuthenticationService
{
    public boolean login(User user);
}
