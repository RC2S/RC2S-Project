package com.rc2s.ejb.plugin.loader;

import javax.ejb.EJBException;
import javax.ejb.Remote;

@Remote
public interface PluginLoaderFacadeRemote
{
    public boolean uploadPlugin();
    
    public Object invoke(String pluginName, String ejb, String method, Object... args) throws EJBException;
}
