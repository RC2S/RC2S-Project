package com.rc2s.ejb.plugin;

import com.rc2s.application.services.plugin.IPluginService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "PluginEJB")
public class PluginFacadeBean implements PluginFacadeRemote
{
	@EJB
	private IPluginService pluginService;

	@Override
	public List<String> getNames() throws EJBException
	{
		try
		{
			return pluginService.getNames();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
