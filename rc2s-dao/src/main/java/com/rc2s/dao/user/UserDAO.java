package com.rc2s.dao.user;

import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserDAO implements IUserDAO
{  
    @PersistenceContext(unitName="rc2s")
    private EntityManager em;
    
	@Override
    public List<User> getUsers()
    {
        Query query = em.createQuery("SELECT u from User as u");
        return query.getResultList();
    }
    
    @Override
    public User getUserByNameAndPassword(String username, String password)
    {
        Query query = em.createQuery("SELECT u from User as u WHERE u.username=:username AND u.password=:password")
                .setParameter("username", username)
                .setParameter("password", password);
        List<User> users = query.getResultList();
        if(!users.isEmpty())
            return users.get(0);
        
        return null;
    }
}