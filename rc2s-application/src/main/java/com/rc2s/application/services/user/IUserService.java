package com.rc2s.application.services.user;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IUserService
{
    public User getAuthenticatedUser(final String username, final String password) throws ServiceException;
    
    public List<User> getAll() throws ServiceException;
    
	public User add(final User user) throws ServiceException;
    
	public User update(final User user, boolean passwordUpdated) throws ServiceException;
    
	public void delete(final User user) throws ServiceException;
}
