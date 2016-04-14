package com.rc2s.dao;

import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserDAO implements IUserDAO
{  
    @PersistenceContext
    private EntityManager em;
    
    public List<User> getUsers()
    {
        Query query = em.createQuery("SELECT u from User as u");
        return query.getResultList();
    }
}