package com.rc2s.application.services.plugin.loader;

import com.rc2s.application.services.plugin.IPluginService;
import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Role;
import com.rc2s.dao.plugin.IPluginDAO;
import java.nio.file.StandardCopyOption;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class PluginLoaderService implements IPluginLoaderService
{
	@EJB private IPluginService pluginService;
	@EJB private IPluginDAO pluginDAO;
	
    @Override
    public void uploadPlugin(String pluginName, Role accessRole, byte[] binaryPlugin) throws ServiceException
    {
		File tmpZip = null;
		File unzipedDir = null;
		
		try
		{
			String simpleName = pluginName.toLowerCase().replace(" ", "");
			
			tmpZip = File.createTempFile(simpleName, ".zip");
			Files.write(tmpZip.toPath(), binaryPlugin);
			
			unzipedDir = unzipPlugin(tmpZip.getAbsolutePath());
			
			File tmpEar = checkServerPlugin(simpleName, unzipedDir.getAbsolutePath() + File.separator);
			File tmpJar = checkClientPlugin(simpleName, unzipedDir.getAbsolutePath() + File.separator);
			
			if(tmpEar != null && tmpJar != null)
			{
				deployServerPlugin(simpleName, tmpEar);
				deployClientPlugin(simpleName, tmpJar);
				persistPlugin(pluginName, accessRole);
			}
		}
		catch(Exception e)
		{
			throw new ServiceException(e);
		}
		finally 
		{
			if(tmpZip != null)
				tmpZip.delete();
			
			if(unzipedDir != null)
			{
				for(File tmp : unzipedDir.listFiles())
					tmp.delete();
				unzipedDir.delete();
			}
		}
    }

    @Override
    public File unzipPlugin(String zipFile) throws IOException
    {   	
        try
        {
            Path folderPath = Files.createTempDirectory("plugins");
            File folder = folderPath.toFile();
            
            if (!folder.exists())
                folder.mkdir();

            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry = zipIn.getNextEntry();
            
            while (entry != null)
            {
                String filePath = folderPath.toString() + File.separator + entry.getName();
                
                if (!entry.isDirectory())
                {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    
                    while ((read = zipIn.read(buffer)) != -1)
                        bos.write(buffer, 0, read);
 
                    bos.close();
                }
                else
                {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
			
			return folderPath.toFile();
        }
        catch(IOException e)
        {
		   throw e;
        }
    }

    @Override
    public File checkServerPlugin(String simpleName, String tmpDir) throws Exception
    {
        try
		{
			File tmpEar = new File(tmpDir + simpleName + "_server.ear");
			
			if(!tmpEar.exists())
				return null;
			
			// TODO: Check the EAR content
			
			return tmpEar;
		}
		catch(Exception e)
		{
			throw e;
		}
    }

    @Override
    public File checkClientPlugin(String simpleName, String tmpDir) throws Exception
    {
        try
		{
			File tmpJar = new File(tmpDir + simpleName + "_client.jar");
			
			if(!tmpJar.exists())
				return null;
			
			// TODO: Check the JAR content
			
			return tmpJar;
		}
		catch(Exception e)
		{
			throw e;
		}
    }

	@Override
    public void deployServerPlugin(String simpleName, File tmpEar) throws IOException
    {		
		String autodeployDir = getDomainRoot() + "autodeploy" + File.separator;
		File pluginFile = new File(autodeployDir + simpleName + "_server.ear");
		Files.copy(tmpEar.toPath(), pluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void deployClientPlugin(String simpleName, File tmpJar) throws IOException
    {
		String jnlpLibsDir = getDomainRoot() + "applications" + File.separator + "jnlpwar" + File.separator + "libs" + File.separator;
		File pluginFile = new File(jnlpLibsDir + simpleName + "_client.jar");
		Files.copy(tmpJar.toPath(), pluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
	
	@Override
	public Plugin persistPlugin(String pluginName, Role role) throws ServiceException
	{
		boolean update = false;
		Plugin plugin;
		
		try
		{
			plugin = pluginDAO.getByName(pluginName);
			update = true;
		}
		catch(DAOException e)
		{
			plugin = new Plugin();
		}
		
		plugin.setName(pluginName);
		plugin.setAccess(role.getName());

		plugin.setVersion("1.0");
		plugin.setAuthor("John Doe");
		plugin.setActivated(true);

		return (update ? pluginService.update(plugin) : pluginService.add(plugin));
	}
	
	@Override
	public void deletePlugin(Plugin plugin) throws ServiceException
	{
		String simpleName = plugin.getName().toLowerCase().replace(" ", "");
		
		// Remove Client Plugin
		String jnlpLibsDir = getDomainRoot() + "applications" + File.separator + "jnlpwar" + File.separator + "libs" + File.separator;
		File pluginClient = new File(jnlpLibsDir + simpleName + "_client.jar");
		
		if(pluginClient.exists())
			pluginClient.delete();
		
		// Remove Server Plugin
		String autodeployDir = getDomainRoot() + "autodeploy" + File.separator;
		File pluginServer = new File(autodeployDir + simpleName + "_server.ear");
		
		if(pluginServer.exists())
			pluginServer.delete();
		
		pluginService.delete(plugin);
	}

	private String getDomainRoot()
	{
		String domainRoot = System.getProperty("com.sun.aas.instanceRootURI");
		
		if(domainRoot != null)
		{
			if(domainRoot.startsWith("file:\\"))
				domainRoot = domainRoot.replace("file:\\", "");
			else if(domainRoot.startsWith("file://"))
				domainRoot = domainRoot.replace("file://", "");
			else if(domainRoot.startsWith("file:/"))
				domainRoot = domainRoot.replace("file:", "");
		}
		
		return domainRoot;
	}
}
