package com.rc2s.ejb.plugin;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Plugin;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface PluginFacadeRemote
{
	public List<Plugin> getAll() throws EJBException;
	public List<String> getAvailables() throws EJBException;
	
	public Plugin add(Plugin plugin) throws EJBException;
	public Plugin update(Plugin plugin) throws EJBException;
	public void delete(Plugin plugin) throws EJBException;
}
