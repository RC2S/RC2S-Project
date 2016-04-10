package com.rc2s.dao;

import com.rc2s.common.vo.User;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Dependent
public class UserDAO implements IUserDAO
{  
    @PersistenceContext(unitName = "rc2s")
    private EntityManager entityManager;
    
    @Override
    public List<User> getUsers()
    {
        if(entityManager == null){
            System.out.println("************** FAIL **************");
        }
        Query query = entityManager.createQuery("SELECT u from User as u");
        return query.getResultList();
        
        /*List<User> users = new ArrayList();
        users.add(new User("mathieu", "azeaze"));
        return users;*/
    }
}