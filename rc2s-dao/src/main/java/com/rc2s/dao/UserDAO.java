package com.rc2s.dao;

import com.rc2s.common.vo.User;
import java.util.ArrayList;
import javax.interceptor.Interceptors;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Repository;

@Repository
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserDAO
{
    private SessionFactory sessionFactory;
    
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
    
    public ArrayList<User> getUsers()
    {
        return (ArrayList<User>) sessionFactory.getCurrentSession().createQuery("from User").list();
    }
}
