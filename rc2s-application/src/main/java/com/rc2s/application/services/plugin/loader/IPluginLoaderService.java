package com.rc2s.application.services.plugin.loader;

import java.io.File;
import javax.ejb.Local;

@Local
public interface IPluginLoaderService
{
    public boolean uploadPlugin(String pluginName, byte[] binaryPlugin);
    
    public void unzipPlugin(String zipFile);
    
    public File checkServerPlugin(File tmpDir) throws Exception;
    
    public File checkClientPlugin(File tmpDir) throws Exception;
    
    public boolean deployServerPlugin(String simpleName, File tmpEar);
    
    public boolean deployClientPlugin(String simpleName, File tmpJar);
}
