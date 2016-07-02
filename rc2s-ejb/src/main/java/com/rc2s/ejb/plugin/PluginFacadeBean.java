package com.rc2s.ejb.plugin;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.application.services.plugin.IPluginService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "PluginEJB")
@Interceptors(SecurityInterceptor.class)
public class PluginFacadeBean implements PluginFacadeRemote
{
	@EJB
	private IPluginService pluginService;

	@Override
	public List<Plugin> getAll(User caller) throws EJBException
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
	public List<Plugin> getAvailables(User caller) throws EJBException
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
	public Plugin add(User caller, Plugin plugin) throws EJBException
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
	public Plugin update(User caller, Plugin plugin) throws EJBException
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
	public void delete(User caller, Plugin plugin) throws EJBException
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
