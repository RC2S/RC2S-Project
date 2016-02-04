package com.rc2s.dao;

import javax.interceptor.Interceptors;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Repository;

@Repository
//@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserDAO
{
    /*private SessionFactory sessionFactory;
    
    
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }*/
    
    public String getUsersByName()
    {
        return "coucou";
    }
}
