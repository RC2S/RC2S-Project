package com.rc2s.application.services.synchronization;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;

import java.util.List;
import javax.ejb.Local;

@Local
public interface ISynchronizationService
{
	public List<Synchronization> getAll() throws ServiceException;
	public List<Synchronization> getByUser(User user) throws ServiceException;
	public void add(Synchronization synchronization) throws ServiceException;
}
