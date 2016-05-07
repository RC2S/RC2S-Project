package com.rc2s.application.services;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.dao.IGenericDAO;
import java.io.Serializable;
import java.util.List;

/**
 * Marked as @Deprecated since this generic class is not yet usable
 * (EJB injection fails when using a GenericService class).
 * @param <T>
 * @param <D>
 * @deprecated
 */
@Deprecated
public interface IGenericService<T extends Serializable, D extends IGenericDAO<T>>
{
	public List<T> getAll() throws ServiceException;
	public <V extends Serializable> T getById(V id) throws ServiceException;
	public T save(T entity) throws ServiceException;
	public T update(T entity) throws ServiceException;
	public <V extends Serializable> void delete(V id) throws ServiceException;
}
