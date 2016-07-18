package com.rc2s.application.services.plugin.loader;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Group;
import java.io.File;
import java.io.IOException;
import javax.ejb.Local;

@Local
public interface IPluginLoaderService
{
    public void uploadPlugin(final String pluginName, final Group accessRole, final byte[] binaryPlugin) throws ServiceException;
    
    public File unzipPlugin(final String zipFile) throws IOException;
    
    public File checkServerPlugin(final String simpleName, final String tmpDir) throws Exception;
    
    public File checkClientPlugin(final String simpleName, final String tmpDir) throws Exception;
    
    public void deployServerPlugin(final String simpleName, final File tmpEar) throws IOException;
    
    public void deployClientPlugin(final String simpleName, final File tmpJar) throws IOException;
	
	public Plugin persistPlugin(final String pluginName, final Group role) throws ServiceException;
	
	public void deletePlugin(final Plugin plugin) throws ServiceException;
}
