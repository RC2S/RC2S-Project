package com.rc2s.dao.user;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.User;
import com.rc2s.dao.GenericDAO;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class UserDAO extends GenericDAO<User> implements IUserDAO
{	
	@Override
	public User getAuthenticatedUser(final String username, final String password) throws DAOException
	{
		try
		{
			Query query = em().createQuery("SELECT u from User as u WHERE u.username = :username AND u.password = :password AND u.activated = 1 AND u.locked = 0")
							  .setParameter("username", username)
							  .setParameter("password", password);
			return (User)query.getSingleResult();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public int setLastLogin(final User user) throws DAOException
	{
		try
		{
			Query query = em().createQuery("UPDATE User SET lastLogin = :lastLogin WHERE id = :id")
							  .setParameter("lastLogin", new Date())
							  .setParameter("id", user.getId());
			return query.executeUpdate();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
}