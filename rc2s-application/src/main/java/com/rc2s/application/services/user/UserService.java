package com.rc2s.application.services.user;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.utils.Hash;
import com.rc2s.common.vo.User;
import com.rc2s.dao.user.IUserDAO;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * UserService
 * 
 * Service for users management
 * Works with the IUserDAO
 * 
 * @author RC2S
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserService implements IUserService
{
    @EJB
    private IUserDAO userDAO;
    
    @Inject
    private Logger log;
    
    private final String SALT   = "c33A0{-LO;<#CB `k:^+8DnxAa.BX74H07z:Qn+U0yD$3ar+.:ge[nc>Trs|Fxy";
	private final String PEPPER = ">m9I}JqHTg:VZ}XISdcG;)yGu)t]7Qv5YT:ZWI^#]f06Aq<c]n7a? x+ZEl#pt:";
    
	/**
	 * getAuthenticatedUser
	 * 
	 * Retrieve a User object on the usernamepassword control basis
	 * 
	 * @param username
	 * @param password
	 * @return User authenticated
	 * @throws ServiceException 
	 */
    @Override
    public User getAuthenticatedUser(final String username, final String password) throws ServiceException
    {
        try
		{
			User user = null;
			
			if (username != null && password != null)
			{
				user = userDAO.getAuthenticatedUser(username, Hash.sha1(SALT + password + PEPPER));
				
				if (user != null)
				{
					int code = userDAO.setLastLogin(user);
				
					if (code != 1)
						log.error("Unable to update user's last IP address. Return code is: " + code);
				}
			}
			
			return user;
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
    }
    
	/**
	 * getAll
	 * 
	 * Get all the users in db
	 * 
	 * @return List<User>
	 * @throws ServiceException 
	 */
    @Override
    public List<User> getAll() throws ServiceException
    {
		try
		{
			return userDAO.getAll();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
    }
	
	/**
	 * add
	 * 
	 * Add a given user to db
	 * 
	 * @param user
	 * @return User added
	 * @throws ServiceException 
	 */
	@Override
	public User add(final User user) throws ServiceException
	{
		try
		{
			user.setPassword(Hash.sha1(SALT + user.getPassword() + PEPPER));
			user.setCreated(new Date());
            
			return userDAO.save(user);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	/**
	 * update
	 * 
	 * Update a given user
	 * If password needs update process we have to recompute the hash
	 * 
	 * @param user
	 * @param passwordUpdated
	 * @return User updated
	 * @throws ServiceException 
	 */
	@Override
	public User update(final User user, final boolean passwordUpdated) throws ServiceException
	{
		try
		{
			if (passwordUpdated)
				user.setPassword(Hash.sha1(SALT + user.getPassword() + PEPPER));
			user.setUpdated(new Date());
            
			return userDAO.update(user);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	/**
	 * delete
	 * 
	 * Delete the given user
	 * 
	 * @param user
	 * @throws ServiceException 
	 */
	@Override
	public void delete(final User user) throws ServiceException
	{
		try
		{
			userDAO.delete(user.getId());
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
}
