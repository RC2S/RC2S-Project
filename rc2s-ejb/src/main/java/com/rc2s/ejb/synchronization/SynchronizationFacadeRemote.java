package com.rc2s.ejb.synchronization;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface SynchronizationFacadeRemote
{
	public List<Synchronization> getAll(User caller) throws EJBException;
    
	public List<Synchronization> getByUser(User caller) throws EJBException;
    
	public void add(User caller, Synchronization synchronization) throws EJBException;
}
