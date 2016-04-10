package com.rc2s.application.services.user;

import com.rc2s.common.vo.User;
import com.rc2s.dao.IUserDAO;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
public class UserService implements IUserService
{
    @Inject
    private IUserDAO userDAO;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<User> getAllUsers()
    {
        if(entityManager == null) {
            System.out.println("************** FAIL Service **************");
        }
        return null; //userDAO.getUsers();
    }
}
