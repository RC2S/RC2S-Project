package com.rc2s.application.services.user;

import com.rc2s.common.vo.User;
import com.rc2s.dao.IUserDAO;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class UserService implements IUserService
{
    @Inject
    private IUserDAO userDAO;
    
    @Override
    public List<User> getAllUsers()
    {
        return userDAO.getUsers();
    }
}
