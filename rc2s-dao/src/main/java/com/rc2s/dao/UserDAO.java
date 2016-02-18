package com.rc2s.dao;

import com.rc2s.common.vo.User;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO implements IUserDAO
{  
    @Override
    public ArrayList<User> getUsers()
    {
        //return (ArrayList<User>) sessionFactory.getCurrentSession().createQuery("from User").list();
        ArrayList<User> list = new ArrayList<>();
        list.add(new User("Mathieu", "azeaze"));
        list.add(new User("Vincent", "ztzt"));
        list.add(new User("Valentin", "qsdqsd"));
        return list;
    }
}
