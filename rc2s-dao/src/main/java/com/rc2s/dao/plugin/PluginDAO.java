package com.rc2s.dao.plugin;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.dao.GenericDAO;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class PluginDAO extends GenericDAO<Plugin> implements IPluginDAO
{
	@Override
	public List<String> getAvailables() throws DAOException
	{
		try
		{
			Query query = em().createQuery("SELECT p.name FROM Plugin AS p WHERE p.activated = TRUE");
			return query.getResultList();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public Plugin getByName(String name) throws DAOException
	{
		try
		{
			Query query = em().createQuery("SELECT p FROM Plugin AS p WHERE p.name = :name")
							  .setParameter("name", name);
			
			return (Plugin)query.getSingleResult();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
}
