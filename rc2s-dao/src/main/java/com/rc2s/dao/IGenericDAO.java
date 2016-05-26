package com.rc2s.dao;

import com.rc2s.common.exceptions.DAOException;
import java.io.Serializable;
import java.util.List;

public interface IGenericDAO<T>
{
	public List<T> getAll() throws DAOException;
	public <V extends Serializable> T getById(V id) throws DAOException;
	public T save(T entity) throws DAOException;
	public T update(T entity) throws DAOException;
	public <V extends Serializable> void delete(V id) throws DAOException;
}
