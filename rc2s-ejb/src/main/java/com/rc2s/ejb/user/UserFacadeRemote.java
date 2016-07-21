package com.rc2s.ejb.user;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

/**
 * UserFacadeRemote
 * 
 * EJB remote interface for User EJB
 * 
 * @author RC2S
 */
@Remote
public interface UserFacadeRemote
{
    public User getAuthenticatedUser(final String username, final String password) throws EJBException;
    
    public List<User> getAll() throws EJBException;
    
	public User add(final User user) throws EJBException;
    
	public User update(final User user, final boolean passwordUpdated) throws EJBException;
    
	public void delete(final User user) throws EJBException;
}
