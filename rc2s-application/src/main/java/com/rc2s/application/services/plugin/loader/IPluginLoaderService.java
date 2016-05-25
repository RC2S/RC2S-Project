package com.rc2s.application.services.plugin.loader;

import java.io.File;
import java.io.IOException;
import javax.ejb.Local;

@Local
public interface IPluginLoaderService
{
    public boolean uploadPlugin(String pluginName, byte[] binaryPlugin);
    
    public File unzipPlugin(String zipFile) throws IOException;
    
    public File checkServerPlugin(String simpleName, String tmpDir) throws Exception;
    
    public File checkClientPlugin(String simpleName, String tmpDir) throws Exception;
    
    public boolean deployServerPlugin(String simpleName, File tmpEar);
    
    public boolean deployClientPlugin(String simpleName, File tmpJar);
}
