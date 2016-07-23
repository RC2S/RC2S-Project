package com.rc2s.dao;

import com.rc2s.common.exceptions.DAOException;
import java.io.Serializable;
import java.util.List;

/**
 * IGenericDAO
 * 
 * Generic DAO for DAO interface implementations
 * 
 * @author RC2S
 * @param <T> 
 */
public interface IGenericDAO<T>
{
	public List<T> getAll() throws DAOException;
    
	public <V extends Serializable> T getById(final V id) throws DAOException;
    
	public T save(final T entity) throws DAOException;
    
	public T update(final T entity) throws DAOException;
    
	public <V extends Serializable> void delete(final V id) throws DAOException;
}
