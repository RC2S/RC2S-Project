package com.rc2s.dao;

import com.rc2s.common.vo.User;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO implements IUserDAO
{  
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public ArrayList<User> getUsers()
    {
        if(sessionFactory == null)
        {
            throw new IllegalStateException("coucou");
        }
        Session session = sessionFactory.openSession();
        session.close();
        //return (ArrayList<User>) sessionFactory.getCurrentSession().createQuery("select u from User as u").list();
        ArrayList<User> list = new ArrayList<>();
        list.add(new User("Mathieu", "azeaze"));
        list.add(new User("Vincent", "ztzt"));
        list.add(new User("Valentin", "qsdqsd"));
        return list;
    }
}
