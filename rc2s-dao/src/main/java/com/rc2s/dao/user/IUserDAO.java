package com.rc2s.dao.user;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.User;
import com.rc2s.dao.IGenericDAO;
import javax.ejb.Local;

/**
 * IUserDAO
 * 
 * Interface for users management
 * 
 * @author RC2S
 */
@Local
public interface IUserDAO extends IGenericDAO<User>
{
	public User getAuthenticatedUser(final String username, final String password) throws DAOException;
    
	public int setLastLogin(final User user) throws DAOException;
}
