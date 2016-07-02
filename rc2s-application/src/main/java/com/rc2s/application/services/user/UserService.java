package com.rc2s.application.services.user;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.utils.Hash;
import com.rc2s.common.vo.User;
import com.rc2s.dao.user.IUserDAO;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors(SecurityInterceptor.class)
public class UserService implements IUserService
{
    @EJB
    private IUserDAO userDAO;
    
    private final String SALT = "c33A0{-LO;<#CB `k:^+8DnxAa.BX74H07z:Qn+U0yD$3ar+.=:ge[nc>Trs|Fxy";
	private final String PEPPER = ">m9I}JqHTg:VZ}XISdcG;)yGu)t]7Qv5YT:ZWI^#]f06Aq<c]n7a? x+=ZEl#pt:";
    
    
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
	public User add(User user) throws ServiceException
	{
		try
		{
			user.setPassword(Hash.sha1(SALT + user.getPassword() + PEPPER));
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
				user.setPassword(Hash.sha1(SALT + user.getPassword() + PEPPER));
			
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
