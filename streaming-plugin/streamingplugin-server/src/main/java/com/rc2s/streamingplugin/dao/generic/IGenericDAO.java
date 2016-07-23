package com.rc2s.streamingplugin.dao.generic;

import com.rc2s.common.exceptions.DAOException;
import java.io.Serializable;
import java.util.List;
import com.rc2s.annotations.SourceControl;

//@SourceControl
public interface IGenericDAO<T>
{
	public List<T> getAll() throws DAOException;
    
	public <V extends Serializable> T getById(final V id) throws DAOException;
    
	public T save(final T entity) throws DAOException;
    
	public T update(final T entity) throws DAOException;
    
	public <V extends Serializable> void delete(final V id) throws DAOException;
}
