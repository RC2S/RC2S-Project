package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import javax.ejb.Stateless;
import com.rc2s.application.services.user.IUserService;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(mappedName = "UserEJB")
//@DeclareRoles({"admin"})
public class UserFacadeBean  implements UserFacadeRemote
{	
    @Inject
    private IUserService userService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    //@RolesAllowed({"admin"})
    public List<User> getAllUsers()
    {
        if(entityManager == null) {
            System.out.println("************** FAIL **************");
        }
        userService.getAllUsers();
        Query query = entityManager.createQuery("SELECT u from User as u");
        return query.getResultList();
        //return userService.getAllUsers();
    }
}
