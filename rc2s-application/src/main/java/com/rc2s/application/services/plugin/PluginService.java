package com.rc2s.application.services.plugin;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.dao.plugin.IPluginDAO;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class PluginService implements IPluginService
{
	@EJB
	private IPluginDAO pluginDAO;

	@Override
	public List<String> getNames() throws ServiceException
	{
		try
		{
			return pluginDAO.getNames();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
}
