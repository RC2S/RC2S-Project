package com.rc2s.application.services.user;

import com.rc2s.dao.UserDAO;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Service;

@Service
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserService implements UserServiceI
{
    private UserDAO userDAO;
    
    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    @Override
    public String getAllUsersOrderedByName()
    {
        //return "coucou";
        return userDAO.getUsersByName();
    }
}
