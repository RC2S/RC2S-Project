package com.rc2s.application.services.synchronization;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import com.rc2s.dao.synchronization.ISynchronizationDAO;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SynchronizationService implements ISynchronizationService
{
	@EJB
	private ISynchronizationDAO synchronizationDAO;

	@Override
	public List<Synchronization> getAll() throws ServiceException
	{
		try
		{
			return synchronizationDAO.getAll();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Synchronization> getByUser(final User user) throws ServiceException
	{
		try
		{
			return synchronizationDAO.getByUser(user);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public void add(final Synchronization synchronization) throws ServiceException
	{
		try
		{
			synchronization.setCreated(new Date());
			synchronizationDAO.save(synchronization);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
}
