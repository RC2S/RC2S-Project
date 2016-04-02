package com.rc2s.dao;

import com.rc2s.common.vo.User;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;

@Dependent
public class UserDAO implements IUserDAO
{  
    @Override
    public List<User> getUsers()
    {
        List<User> users = new ArrayList();
        users.add(new User("mathieu", "azeaze"));
        return users;
    }
}