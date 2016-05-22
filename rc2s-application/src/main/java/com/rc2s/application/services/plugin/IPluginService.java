package com.rc2s.application.services.plugin;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IPluginService
{
	public List<String> getNames() throws ServiceException;
	
	public Plugin add(Plugin plugin) throws ServiceException;
	public Plugin update(Plugin plugin) throws ServiceException;
	public void delete(Plugin plugin) throws ServiceException;
}
