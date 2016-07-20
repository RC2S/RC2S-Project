package com.rc2s.ejb.synchronization;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface SynchronizationFacadeRemote
{
	public List<Synchronization> getAll() throws EJBException;
    
	public List<Synchronization> getByUser(final User user) throws EJBException;
    
	public void add(Synchronization synchronization) throws EJBException;
}
