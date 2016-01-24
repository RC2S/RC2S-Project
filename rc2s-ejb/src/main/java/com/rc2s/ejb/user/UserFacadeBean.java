package com.rc2s.ejb.user;

import com.rc2s.application.services.user.UserServiceI;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserFacadeBean implements UserFacadeRemote
{
    @Autowired
    private UserServiceI userService;
    
    @Override
    public String getAllUsers() {
        return userService.getAllUsersOrderedByName();
    }
}
