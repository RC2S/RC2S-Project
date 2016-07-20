package com.rc2s.ejb.plugin.loader;

import com.rc2s.application.services.plugin.loader.IPluginLoaderService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Group;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

@Stateless(mappedName = "PluginLoaderEJB")
public class PluginLoaderFacadeBean implements PluginLoaderFacadeRemote
{
    @EJB
    private IPluginLoaderService pluginLoaderService;
    
    @Inject
    private Logger log;
    
    @Override
    @RolesAllowed({"admin"})
    public void uploadPlugin(final String pluginName, final Group accessRole, final byte[] binaryPlugin) throws EJBException
    {
		try
		{
			pluginLoaderService.uploadPlugin(pluginName, accessRole, binaryPlugin);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
    }

	@Override
    @RolesAllowed({"admin"})
	public void deletePlugin(final Plugin plugin) throws EJBException
	{
		try
		{
			pluginLoaderService.deletePlugin(plugin);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
}
