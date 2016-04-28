package com.rc2s.application.services.user;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IUserService
{
    public List<User> getAllUsers() throws ServiceException;
	public User login(String username, String password) throws ServiceException;
}
