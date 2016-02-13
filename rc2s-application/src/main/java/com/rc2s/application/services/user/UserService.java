package com.rc2s.application.services.user;

import com.rc2s.common.vo.User;
import com.rc2s.dao.UserDAOI;
import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements UserServiceI
{
    private UserDAOI userDAO;
    
    @Autowired
    public void setUserDAO(UserDAOI userDAO) {
        this.userDAO = userDAO;
    }
    
    @Override
    public ArrayList<User> getAllUsers()
    {
        return userDAO.getUsers();
    }
}
