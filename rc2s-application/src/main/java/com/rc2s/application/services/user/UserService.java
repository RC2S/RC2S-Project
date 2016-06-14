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

@Stateless
public class UserService implements IUserService
{
    @EJB
    private IUserDAO userDAO;
	
	private static final String SALT = "c33A0{-LO;<#CB `k:^+8DnxAa.BX74H07z:Qn+U0yD$3ar+.=:ge[nc>Trs|Fxy";
	private static final String PEPPER = ">m9I}JqHTg:VZ}XISdcG;)yGu)t]7Qv5YT:ZWI^#]f06Aq<c]n7a? x+=ZEl#pt:";
    
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
	
	@Override
	public User login(String username, String password) throws ServiceException
	{
		try
		{
			User user = null;
			
			if(username != null && password != null)
			{
				password = Hash.sha1(UserService.SALT + password + UserService.PEPPER);
				user = userDAO.getAuthenticatedUser(username, password);
				
				if(user != null)
				{
					int code = userDAO.setLastLogin(user);
				
					if(code != 1)
					{
						System.err.println("Unable to update user's last IP address. Return code is: " + code);
					}
				}
			}
			
			return user;
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public User add(User user) throws ServiceException
	{
		try
		{
			user.setPassword(Hash.sha1(UserService.SALT + user.getPassword() + UserService.PEPPER));
			System.out.println(user.getPassword());
			
			user.setCreated(new Date());
			return userDAO.save(user);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public User update(User user, boolean passwordUpdated) throws ServiceException
	{
		try
		{
			if(passwordUpdated)
				user.setPassword(Hash.sha1(UserService.SALT + user.getPassword() + UserService.PEPPER));
			
			user.setUpdated(new Date());
			return userDAO.update(user);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void delete(User user) throws ServiceException
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