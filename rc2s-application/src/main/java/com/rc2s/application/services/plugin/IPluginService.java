package com.rc2s.application.services.plugin;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import java.util.List;
import javax.ejb.Local;

/**
 * IPluginService interface
 * 
 * Service interface for plugin management via IPluginDAO
 * 
 * @author RC2S
 */
@Local
public interface IPluginService
{
	public List<Plugin> getAll() throws ServiceException;
    
	public List<Plugin> getAvailables() throws ServiceException;
	
	public Plugin add(final Plugin plugin) throws ServiceException;
    
	public Plugin update(final Plugin plugin) throws ServiceException;
    
	public void delete(final Plugin plugin) throws ServiceException;
}
