package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import javax.ejb.Stateless;
import com.rc2s.application.services.user.IUserService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import java.util.List;
import javax.ejb.EJB;

@Stateless(mappedName = "UserEJB")
public class UserFacadeBean implements UserFacadeRemote
{
    @EJB
    private IUserService userService;
    
    @Override
    public List<User> getAll() throws EJBException
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
	public User login(String username, String password) throws EJBException
	{
		try
		{
			return userService.login(username, password);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
