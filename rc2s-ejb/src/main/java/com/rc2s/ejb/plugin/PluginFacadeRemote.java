package com.rc2s.ejb.plugin;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface PluginFacadeRemote
{
	public List<Plugin> getAll(final User caller) throws EJBException;
    
	public List<Plugin> getAvailables(final User caller) throws EJBException;
	
	public Plugin add(final User caller, final Plugin plugin) throws EJBException;
    
	public Plugin update(final User caller, final Plugin plugin) throws EJBException;
    
	public void delete(final User caller, final Plugin plugin) throws EJBException;
}
