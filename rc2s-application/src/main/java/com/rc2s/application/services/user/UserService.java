package com.rc2s.application.services.user;

import com.rc2s.common.vo.User;
import com.rc2s.dao.user.IUserDAO;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class UserService implements IUserService
{
    @EJB
    private IUserDAO userDAO;
    
    @Override
    public List<User> getAllUsers()
    {
        return userDAO.getUsers();
    }
}
