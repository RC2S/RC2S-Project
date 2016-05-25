package com.rc2s.ejb.plugin.loader;

import com.rc2s.application.services.plugin.loader.IPluginLoaderService;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "PluginLoaderEJB")
public class PluginLoaderFacadeBean implements PluginLoaderFacadeRemote
{
    @EJB
    private IPluginLoaderService pluginLoaderService;
    
    @Override
    public void uploadPlugin()
    {
        pluginLoaderService.uploadPlugin();
    }
}
