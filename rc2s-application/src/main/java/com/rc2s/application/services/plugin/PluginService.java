package com.rc2s.application.services.plugin;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.dao.plugin.IPluginDAO;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * PluginService
 * 
 * Service for plugin management
 * Acces to database via IPluginDAO
 * 
 * @author RC2S
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PluginService implements IPluginService
{
	@EJB
	private IPluginDAO pluginDAO;
    
    @Inject
    private Logger log;

	/**
	 * getAll
	 * 
	 * Retrieve all the plugins from db
	 * 
	 * @return List<Plugin>
	 * @throws ServiceException 
	 */
	@Override
	public List<Plugin> getAll() throws ServiceException
	{
		try
		{
			return pluginDAO.getAll();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	/**
	 * getAvailables
	 * 
	 * Get the list of all available plugins
	 * 
	 * @return List<Plugin>
	 * @throws ServiceException 
	 */
	@Override
	public List<Plugin> getAvailables() throws ServiceException
	{
		try
		{
			return pluginDAO.getAvailables();
		}
		catch (DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	/**
	 * add
	 * 
	 * Add a new plugin to db
	 * 
	 * @param plugin
	 * @return Plugin create
	 * @throws ServiceException 
	 */
	@Override
	public Plugin add(final Plugin plugin) throws ServiceException
	{
		try
		{
			plugin.setCreated(new Date());
			return pluginDAO.save(plugin);
		}
		catch (DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	/**
	 * update
	 * 
	 * Update a specified plugin in db
	 * 
	 * @param plugin
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public Plugin update(final Plugin plugin) throws ServiceException
	{
		try
		{
			plugin.setUpdated(new Date());
			return pluginDAO.update(plugin);
		}
		catch (DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	/**
	 * delete
	 * 
	 * Delete a specified plugin in db
	 * 
	 * @param plugin
	 * @throws ServiceException 
	 */
	@Override
	public void delete(final Plugin plugin) throws ServiceException
	{
		try
		{
			pluginDAO.delete(plugin.getId());
		}
		catch (DAOException e)
		{
			throw new ServiceException(e);
		}
	}
}
