package com.rc2s.ejb.user;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface UserFacadeRemote
{
    public List<User> getAll(User caller) throws EJBException;
    
	public User add(User caller, User user) throws EJBException;
    
	public User update(User caller, User user, boolean passwordUpdated) throws EJBException;
    
	public void delete(User caller, User user) throws EJBException;
}
