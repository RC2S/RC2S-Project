package com.rc2s.application.services.user;

import com.rc2s.common.vo.User;
import com.rc2s.dao.UserDAO;
import java.util.ArrayList;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Service;

@Service
@Interceptors(SpringBeanAutowiringInterceptor.class)
@Transactional
public class UserService implements UserServiceI
{
    private UserDAO userDAO;
    
    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    @Override
    public ArrayList<User> getAllUsers()
    {
        return userDAO.getUsers();
    }
}
