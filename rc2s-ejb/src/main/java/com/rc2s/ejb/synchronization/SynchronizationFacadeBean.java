package com.rc2s.ejb.synchronization;

import com.rc2s.application.services.synchronization.ISynchronizationService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "SynchronizationEJB")
public class SynchronizationFacadeBean implements SynchronizationFacadeRemote
{
	@EJB
	private ISynchronizationService synchronizationService;

	@Override
    @RolesAllowed({"user"})
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
    @RolesAllowed({"user"})
	public List<Synchronization> getByUser(final User user) throws EJBException
	{
		try
		{
			return synchronizationService.getByUser(user);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
    @RolesAllowed({"user"})
	public void add(final Synchronization synchronization) throws EJBException
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
