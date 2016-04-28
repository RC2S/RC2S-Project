package com.rc2s.dao.user;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IUserDAO
{
    public List<User> getUsers() throws DAOException;
	public User getAuthenticatedUser(String username, String password) throws DAOException;
	public int setLastLogin(User user) throws DAOException;
}
