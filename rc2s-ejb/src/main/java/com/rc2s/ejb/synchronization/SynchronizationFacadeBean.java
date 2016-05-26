package com.rc2s.ejb.synchronization;

import com.rc2s.application.services.synchronization.ISynchronizationService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Synchronization;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "SynchronizationEJB")
public class SynchronizationFacadeBean implements SynchronizationFacadeRemote
{
	@EJB
	private ISynchronizationService synchronizationService;

	@Override
	public List<Synchronization> getAll() throws EJBException
	{
		try
		{
			return synchronizationService.getAll();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public void add(Synchronization synchronization) throws EJBException
	{
		try
		{
			synchronizationService.add(synchronization);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
