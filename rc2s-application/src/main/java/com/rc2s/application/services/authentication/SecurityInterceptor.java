package com.rc2s.application.services.authentication;

import com.rc2s.common.exceptions.LoginException;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Interceptor
public class SecurityInterceptor
{
    @AroundInvoke
    public Object checkAuthentication(InvocationContext ctx) throws Exception
    {
        User caller;
        Object[] parameters = ctx.getParameters();
         
        if (parameters == null || parameters.length == 0)
            throw new IllegalArgumentException("Wrong number of arguments");
        else if (!(parameters[0] instanceof User))
            throw new IllegalArgumentException("Wrong first parameter type");
        
        caller = (User) parameters[0];
        
        if (getUserByUsernameAndPassword(caller.getUsername(), caller.getPassword()) != null)
            return ctx.proceed();
        else
            throw new LoginException("Access Denied");
    }
    
    @SuppressWarnings("unchecked")
    private User getUserByUsernameAndPassword(String username, String password)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("rc2s");
        EntityManager em = emf.createEntityManager();
        
        Query query = em.createQuery("SELECT u from User as u WHERE u.username=:username AND u.password=:password AND u.activated=1 AND u.locked=0")
                .setParameter("username", username)
                .setParameter("password", password);
        
        List<User> users = query.getResultList();
        
        if(!users.isEmpty())
            return users.get(0);
        
        return null;
    }
}
