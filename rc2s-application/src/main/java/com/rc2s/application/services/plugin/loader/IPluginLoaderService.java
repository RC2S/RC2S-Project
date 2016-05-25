package com.rc2s.application.services.plugin.loader;

import java.io.File;
import javax.ejb.Local;

@Local
public interface IPluginLoaderService
{
    public boolean uploadPlugin(String pluginName, byte[] binaryPlugin);
    
    public void unzipPlugin();
    
    public File checkServerPlugin(File tmpEar);
    
    public void checkClientPlugin();
    
    public boolean deployServerPlugin(String simpleName, File tmpEar);
    
    public boolean deployClientPlugin(String simpleName, File tmpJar);
}
