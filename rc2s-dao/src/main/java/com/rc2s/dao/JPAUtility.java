package com.rc2s.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtility
{
    private static EntityManagerFactory emFactory;
    
    static
    {
        emFactory = Persistence.createEntityManagerFactory("rc2s");
    }
    
    public static EntityManager getEntityManager()
    {
        return emFactory.createEntityManager();
    }
    
    public static void close()
    {
        emFactory.close();
    }
}
