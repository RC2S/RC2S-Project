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

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PluginService implements IPluginService
{
	@EJB
	private IPluginDAO pluginDAO;

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
	
	@Override
	public List<Plugin> getAvailables() throws ServiceException
	{
		try
		{
			return pluginDAO.getAvailables();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public Plugin add(final Plugin plugin) throws ServiceException
	{
		try
		{
			plugin.setCreated(new Date());
			return pluginDAO.save(plugin);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public Plugin update(final Plugin plugin) throws ServiceException
	{
		try
		{
			plugin.setUpdated(new Date());
			return pluginDAO.update(plugin);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public void delete(final Plugin plugin) throws ServiceException
	{
		try
		{
			pluginDAO.delete(plugin.getId());
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	
}
