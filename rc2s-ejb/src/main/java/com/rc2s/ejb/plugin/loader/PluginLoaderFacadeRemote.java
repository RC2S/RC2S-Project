package com.rc2s.ejb.plugin.loader;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Group;
import com.rc2s.common.vo.User;
import javax.ejb.Remote;

@Remote
public interface PluginLoaderFacadeRemote
{
    public void uploadPlugin(final String pluginName, final Group accessRole, final byte[] binaryPlugin) throws EJBException;
    
	public void deletePlugin(final Plugin plugin) throws EJBException;
}
