package com.rc2s.application.services.user;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IUserService
{
    public List<User> getAll() throws ServiceException;
	public User login(String username, String password) throws ServiceException;
	public User add(User user) throws ServiceException;
	public User update(User user, boolean passwordUpdated) throws ServiceException;
	public void delete(User user) throws ServiceException;
}
