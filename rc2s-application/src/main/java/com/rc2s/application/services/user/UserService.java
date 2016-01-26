package com.rc2s.application.services.user;

import com.rc2s.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceI
{
    /*private UserDAO userDAO;
    
    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }*/
    
    @Override
    public String getAllUsersOrderedByName()
    {
        return "coucou"; //userDAO.getUsersByName();
    }
}
