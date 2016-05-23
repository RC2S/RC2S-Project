package com.rc2s.ejb.plugin.loader;

import javax.ejb.Stateless;

@Stateless(mappedName = "PluginLoaderEJB")
public class PluginLoaderFacadeBean implements PluginLoaderFacadeRemote
{
    @Override
    public boolean uploadPlugin()
    {
        return false;
    }

    @Override
    public <T> T loadPlugin(String pluginName, String method)
    {
        return null;
    }
}
