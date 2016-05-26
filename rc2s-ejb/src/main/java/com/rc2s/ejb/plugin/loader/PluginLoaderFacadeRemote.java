package com.rc2s.ejb.plugin.loader;

import javax.ejb.Remote;

@Remote
public interface PluginLoaderFacadeRemote
{
    public boolean uploadPlugin(String pluginName, byte[] binaryPlugin);
}
