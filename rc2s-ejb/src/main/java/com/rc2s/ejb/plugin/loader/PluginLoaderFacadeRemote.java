package com.rc2s.ejb.plugin.loader;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Role;
import com.rc2s.common.vo.User;
import javax.ejb.Remote;

@Remote
public interface PluginLoaderFacadeRemote
{
    public void uploadPlugin(User caller, String pluginName, Role accessRole, byte[] binaryPlugin) throws EJBException;
    
	public void deletePlugin(User caller, Plugin plugin) throws EJBException;
}
