package com.rc2s.application.services.user;

import com.rc2s.common.vo.User;
import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rc2s.dao.IUserDAO;

@Service
@Transactional
public class UserService implements IUserService
{
    private IUserDAO userDAO;
    
    @Autowired
    public void setUserDAO(IUserDAO userDAO)
    {
        this.userDAO = userDAO;
    }
    
    @Override
    public ArrayList<User> getAllUsers()
    {
        return userDAO.getUsers();
    }
}
