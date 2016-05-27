package com.rc2s.ejb.plugin.loader;

import com.rc2s.application.services.plugin.loader.IPluginLoaderService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Role;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "PluginLoaderEJB")
public class PluginLoaderFacadeBean implements PluginLoaderFacadeRemote
{
    @EJB
    private IPluginLoaderService pluginLoaderService;
    
    @Override
    public void uploadPlugin(String pluginName, Role accessRole, byte[] binaryPlugin) throws EJBException
    {
		try
		{
			pluginLoaderService.uploadPlugin(pluginName, accessRole, binaryPlugin);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
    }

	@Override
	public void deletePlugin(Plugin plugin) throws EJBException
	{
		try
		{
			pluginLoaderService.deletePlugin(plugin);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
