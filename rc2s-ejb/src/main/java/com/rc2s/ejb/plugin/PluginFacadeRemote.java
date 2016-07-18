package com.rc2s.ejb.plugin;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface PluginFacadeRemote
{
	public List<Plugin> getAll() throws EJBException;
    
	public List<Plugin> getAvailables() throws EJBException;
	
	public Plugin add(final Plugin plugin) throws EJBException;
    
	public Plugin update(final Plugin plugin) throws EJBException;
    
	public void delete(final Plugin plugin) throws EJBException;
}
