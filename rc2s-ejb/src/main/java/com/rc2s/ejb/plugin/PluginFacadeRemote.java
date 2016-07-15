package com.rc2s.ejb.plugin;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface PluginFacadeRemote
{
	public List<Plugin> getAll(User caller) throws EJBException;
    
	public List<Plugin> getAvailables(User caller) throws EJBException;
	
	public Plugin add(User caller, Plugin plugin) throws EJBException;
    
	public Plugin update(User caller, Plugin plugin) throws EJBException;
    
	public void delete(User caller, Plugin plugin) throws EJBException;
}
