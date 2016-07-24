package com.rc2s.dao.plugin;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.dao.GenericDAO;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * PluginDAO
 * 
 * IPluginDAO implementation, bridge for database plugins management
 * 
 * @author RC2S
 */
@Stateless
public class PluginDAO extends GenericDAO<Plugin> implements IPluginDAO
{
	/**
	 * getAvailables
	 * 
	 * Get all the available plugins
	 * 
	 * @return List<Plugin> the available plugins
	 * @throws DAOException 
	 */
	@Override
	public List<Plugin> getAvailables() throws DAOException
	{
		try
		{
			Query query = em().createQuery("SELECT p FROM Plugin AS p WHERE p.activated = TRUE");
			return query.getResultList();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	/**
	 * getByName
	 * 
	 * Get a plugin based on its name
	 * 
	 * @param name
	 * @return Plugin
	 * @throws DAOException 
	 */
	@Override
	public Plugin getByName(final String name) throws DAOException
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
