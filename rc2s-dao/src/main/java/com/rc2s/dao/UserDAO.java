package com.rc2s.dao;

import com.rc2s.common.vo.User;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO implements IUserDAO
{  
    private static final Logger log = LogManager.getLogger(UserDAO.class);
    
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public ArrayList<User> getUsers()
    {
        log.info("-------***** Users *********");
        return (ArrayList<User>) sessionFactory.getCurrentSession().createQuery("from User").list();
    }
}