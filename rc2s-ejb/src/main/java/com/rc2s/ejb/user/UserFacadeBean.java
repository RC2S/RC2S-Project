package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import com.rc2s.application.services.user.IUserService;

@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserFacadeBean implements UserFacadeRemote
{
    @Autowired
    private IUserService userService;
    
    @Override
    public ArrayList<User> getAllUsers()
    {
        return userService.getAllUsers();
    }
}
