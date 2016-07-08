package com.rc2s.ejb.user;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.common.vo.User;
import javax.ejb.Stateless;
import com.rc2s.application.services.user.IUserService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import java.util.List;
import javax.ejb.EJB;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "UserEJB")
@Interceptors(SecurityInterceptor.class)
public class UserFacadeBean implements UserFacadeRemote
{
    @EJB
    private IUserService userService;
    
    @Override
    public List<User> getAll(final User caller) throws EJBException
    {
		try
		{
			return userService.getAll();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
    }
	
	@Override
	public User add(final User caller, final User user) throws EJBException
	{
		try
		{
			return userService.add(user);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public User update(final User caller, final User user, final boolean passwordUpdated) throws EJBException
	{
		try
		{
			return userService.update(user, passwordUpdated);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public void delete(final User caller, final User user) throws EJBException
	{
		try
		{
			userService.delete(user);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
