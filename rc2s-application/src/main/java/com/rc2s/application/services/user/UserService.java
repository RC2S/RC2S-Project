package com.rc2s.application.services.user;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.User;
import com.rc2s.dao.user.IUserDAO;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class UserService implements IUserService
{
    @EJB
    private IUserDAO userDAO;
    
    @Override
    public List<User> getAllUsers() throws ServiceException
    {
		try
		{
			return userDAO.getUsers();
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
}
