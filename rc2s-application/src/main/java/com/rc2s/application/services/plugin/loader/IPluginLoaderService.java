package com.rc2s.application.services.plugin.loader;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Role;
import java.io.File;
import java.io.IOException;
import javax.ejb.Local;

@Local
public interface IPluginLoaderService
{
    public boolean uploadPlugin(String pluginName, Role accessRole, byte[] binaryPlugin) throws ServiceException;
    
    public File unzipPlugin(String zipFile) throws IOException;
    
    public File checkServerPlugin(String simpleName, String tmpDir) throws Exception;
    
    public File checkClientPlugin(String simpleName, String tmpDir) throws Exception;
    
    public boolean deployServerPlugin(String simpleName, File tmpEar);
    
    public boolean deployClientPlugin(String simpleName, File tmpJar);
	
	public Plugin persistPlugin(String pluginName, Role role) throws ServiceException;
	
	public void deletePlugin(Plugin plugin) throws ServiceException;
}
