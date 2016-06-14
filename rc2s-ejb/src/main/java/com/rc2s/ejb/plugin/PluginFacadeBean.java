package com.rc2s.ejb.plugin;

import com.rc2s.application.services.plugin.IPluginService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "PluginEJB")
public class PluginFacadeBean implements PluginFacadeRemote
{
	@EJB
	private IPluginService pluginService;

	@Override
	public List<Plugin> getAll() throws EJBException
	{
		try
		{
			return pluginService.getAll();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public List<Plugin> getAvailables() throws EJBException
	{
		try
		{
			return pluginService.getAvailables();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public Plugin add(Plugin plugin) throws EJBException
	{
		try
		{
			return pluginService.add(plugin);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public Plugin update(Plugin plugin) throws EJBException
	{
		try
		{
			return pluginService.update(plugin);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public void delete(Plugin plugin) throws EJBException
	{
		try
		{
			pluginService.delete(plugin);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}