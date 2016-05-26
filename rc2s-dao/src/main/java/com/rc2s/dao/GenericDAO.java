package com.rc2s.dao;

import com.rc2s.common.exceptions.DAOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericDAO<T extends Serializable> implements IGenericDAO<T>
{
	@PersistenceContext
    private EntityManager em;
	private final Class<T> type;
	
	@SuppressWarnings("unchecked")
	public GenericDAO()
	{
		this.type = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public List<T> getAll() throws DAOException
	{
		try
		{
			return em().createQuery("SELECT e FROM " + type.getName() + " e ORDER BY 1").getResultList();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public <V extends Serializable> T getById(V id) throws DAOException
	{
		try
		{
			return (id != null) ? em().find(type, id) : null;
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public T save(T entity) throws DAOException
	{
		try
		{
			em().persist(entity);
			em().flush(); // Flush the entity manager in order to initialize the entity auto-generated ID
			return entity; // Return the entity with its ID
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public T update(T entity) throws DAOException
	{
		try
		{
			em().merge(entity);
			em().flush();
			return entity;
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public <V extends Serializable> void delete(V id) throws DAOException
	{
		try
		{
			T entity = getById(id);
			em().remove(entity);
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	/**
	 * This method is not specified in the IGenericDAO interface because we
	 * want to keep it protected: another class must not be allowed to access
	 * the persistence entity manager.
	 * @return 
	 */
	protected EntityManager em()
	{
		return em;
	}
}
