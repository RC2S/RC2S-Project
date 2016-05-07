package com.rc2s.application.services;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.dao.IGenericDAO;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;

@Deprecated
public class GenericService<T extends Serializable, D extends IGenericDAO<T>> implements IGenericService<T, D>
{
	@EJB
	private D dao;
	
	@Override
	public List<T> getAll() throws ServiceException
	{
		try
		{
			return dao().getAll();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public <V extends Serializable> T getById(V id) throws ServiceException
	{
		try
		{
			return dao().getById(id);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public T save(T entity) throws ServiceException
	{
		try
		{
			return dao().save(entity);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public T update(T entity) throws ServiceException
	{
		try
		{
			return dao().update(entity);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public <V extends Serializable> void delete(V id) throws ServiceException
	{
		try
		{
			dao().delete(id);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	/**
	 * This method is not specified in the IGenericService interface because we
	 * want to keep it protected: another class must not be allowed to access
	 * the entity's Data Access Object.
	 * @return 
	 */
	protected D dao()
	{
		return dao;
	}	
}
