package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import javax.ejb.Stateless;
import com.rc2s.application.services.user.IUserService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * UserFacadeBean
 * 
 * User EJB, bridge to UserService
 * 
 * @author RC2S
 */
@Stateless(mappedName = "UserEJB")
public class UserFacadeBean implements UserFacadeRemote
{
    @EJB
    private IUserService userService;
    
    @Inject
    private Logger log;
    
	/**
	 * getAuthenticatedUser
	 * 
	 * Get an authenticated user from db from classic credentials
	 * 
	 * @param username
	 * @param password
	 * @return User authenticated
	 * @throws EJBException 
	 */
    @Override
    @RolesAllowed({"user"})
    public User getAuthenticatedUser(String username, String password) throws EJBException
    {
        try
		{
			return userService.getAuthenticatedUser(username, password);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
    }
    
	/**
	 * getAll
	 * 
	 * Get all the users in db
	 * 
	 * @return List<User>
	 * @throws EJBException 
	 */
    @Override
    @RolesAllowed({"admin"})
    public List<User> getAll() throws EJBException
    {
		try
		{
			return userService.getAll();
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
    }
	
	/**
	 * add
	 * 
	 * Adds a given user to db
	 * 
	 * @param user
	 * @return User added
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"admin"})
	public User add(final User user) throws EJBException
	{
		try
		{
			return userService.add(user);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}

	/**
	 * update
	 * 
	 * Updates the given user in db
	 * 
	 * @param user
	 * @param passwordUpdated
	 * @return User updated
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"admin"})
	public User update(final User user, final boolean passwordUpdated) throws EJBException
	{
		try
		{
			return userService.update(user, passwordUpdated);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}

	/**
	 * delete
	 * 
	 * Deletes the given user in db
	 * 
	 * @param user
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"admin"})
	public void delete(final User user) throws EJBException
	{
		try
		{
			userService.delete(user);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
}
