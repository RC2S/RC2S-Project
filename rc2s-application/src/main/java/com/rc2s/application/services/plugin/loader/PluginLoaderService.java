package com.rc2s.application.services.plugin.loader;

import com.rc2s.application.services.jnlp.IJnlpService;
import com.rc2s.application.services.plugin.IPluginService;
import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Group;
import com.rc2s.dao.plugin.IPluginDAO;

import java.nio.file.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * PluginLoaderService
 * 
 * Service for plugin loading management
 * Access to database via IPluginService
 * 
 * @author RC2S
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PluginLoaderService implements IPluginLoaderService
{
	@EJB 
    private IPluginService pluginService;
    
	@EJB
    private IPluginDAO pluginDAO;
    
    @EJB
    private IJnlpService jnlpService;
    
    @Inject
    private Logger log;
	
	/**
	 * uploadPlugin
	 * 
	 * Upload the given plugin
	 * 
	 * @param pluginName
	 * @param accessGroup
	 * @param binaryPlugin
	 * @throws ServiceException 
	 */
    @Override
    public void uploadPlugin(final String pluginName, final Group accessGroup, final byte[] binaryPlugin) throws ServiceException
    {
		Path tmpZip = null;
		Path unzipedDir = null;
		
		try
		{
			String simpleName = pluginName.toLowerCase().replace(" ", "");
			
			tmpZip = Files.createTempFile(simpleName, ".zip");
			Files.write(tmpZip, binaryPlugin);
			
			unzipedDir = unzipPlugin(tmpZip.toString());
			
			Path tmpEar = checkServerPlugin(simpleName, unzipedDir.toString());
			Path tmpJar = checkClientPlugin(simpleName, unzipedDir.toString());
			
			if (tmpEar != null && tmpJar != null)
			{
				deployServerPlugin(simpleName, tmpEar);
				deployClientPlugin(simpleName, tmpJar);
				persistPlugin(pluginName, accessGroup);
			}
		}
		catch (Exception e)
		{
			throw new ServiceException(e);
		}
		finally 
		{
			try
			{
				if (tmpZip != null)
					Files.delete(tmpZip);

				if (unzipedDir != null)
				{
					try (DirectoryStream<Path> ds = Files.newDirectoryStream(unzipedDir))
					{
						for (Path file : ds)
							Files.delete(file);
					}
					catch (IOException e) { /* Ignore and try to delete the directory */ }
					
					Files.delete(unzipedDir);
				}
			}
			catch (IOException e)
			{
				throw new ServiceException(e);
			}
		}
    }

	/**
	 * unzipPlugin
	 * 
	 * Unzips a given file that should contain a plugin
	 * 
	 * @param zipFile
	 * @return folderPath
	 * @throws IOException 
	 */
    @Override
    public Path unzipPlugin(final String zipFile) throws IOException
    {
        try
        {
            Path folderPath = Files.createTempDirectory("plugins");
            
            if (!Files.exists(folderPath))
                Files.createDirectory(folderPath);

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
                    Path dir = Paths.get(filePath);
                    Files.createDirectory(dir);
                }
				
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
			
            zipIn.close();
			
			return folderPath;
        }
        catch (IOException e)
        {
		   throw e;
        }
    }

	/**
	 * checkServerPlugin
	 * 
	 * Check if the plugin is present on server side
	 * 
	 * @param simpleName
	 * @param tmpDir
	 * @return earPath
	 * @throws Exception 
	 */
    @Override
    public Path checkServerPlugin(final String simpleName, final String tmpDir) throws Exception
    {
        try
		{
			Path tmpEar = Paths.get(tmpDir, simpleName + "_server.ear");
			
			if (!Files.exists(tmpEar))
				return null;
			
			return tmpEar;
		}
		catch (Exception e)
		{
			throw e;
		}
    }

	/**
	 * checkClientPlugin
	 * 
	 * Check if the plugin is present on client side
	 * 
	 * @param simpleName
	 * @param tmpDir
	 * @return jarPath
	 * @throws Exception 
	 */
    @Override
    public Path checkClientPlugin(final String simpleName, final String tmpDir) throws Exception
    {
        try
		{
			Path tmpJar = Paths.get(tmpDir, simpleName + "_client.jar");
			
			if (!Files.exists(tmpJar))
				return null;
			
			return tmpJar;
		}
		catch (Exception e)
		{
			throw e;
		}
    }

	/**
	 * deployServerPlugin
	 * 
	 * @param simpleName
	 * @param tmpEar
	 * @throws IOException 
	 */
	@Override
    public void deployServerPlugin(final String simpleName, final Path tmpEar) throws IOException
    {		
		String autodeployDir = getDomainRoot() + "autodeploy" + File.separator;
		Path pluginPath = Paths.get(autodeployDir, simpleName + "_server.ear");
		Files.copy(tmpEar, pluginPath, StandardCopyOption.REPLACE_EXISTING);
    }

	/**
	 * deployClientPlugin
	 * 
	 * @param simpleName
	 * @param tmpJar
	 * @throws IOException 
	 */
    @Override
    public void deployClientPlugin(final String simpleName, final Path tmpJar) throws IOException
    {
		String jnlpLibsDir = getDomainRoot() + "applications" + File.separator
				+ "rc2s-jnlp" + File.separator
				+ "libs" + File.separator;
		jnlpService.signJar(tmpJar.toString());
        Path pluginPath = Paths.get(jnlpLibsDir, simpleName + "_client.jar");
		Files.copy(tmpJar, pluginPath, StandardCopyOption.REPLACE_EXISTING);
        jnlpService.updateJNLP(simpleName + "_client.jar", false);
    }
	
	/**
	 * persistPlugin
	 * 
	 * @param pluginName
	 * @param group
	 * @return pluginAddedOrUpdated
	 * @throws ServiceException 
	 */
	@Override
	public Plugin persistPlugin(final String pluginName, final Group group) throws ServiceException
	{
		boolean update = false;
		Plugin plugin;
		
		try
		{
			plugin = pluginDAO.getByName(pluginName);
			update = true;
		}
		catch (DAOException e)
		{
			plugin = new Plugin();
		}
		
		plugin.setName(pluginName);
		plugin.setAccess(group.getName());

		plugin.setVersion("1.0");
		plugin.setAuthor("John Doe");
		plugin.setActivated(true);

		return (update ? pluginService.update(plugin) : pluginService.add(plugin));
	}
	
	/**
	 * deletePlugin
	 * 
	 * Delete the given plugin from db
	 * 
	 * @param plugin
	 * @throws ServiceException 
	 */
	@Override
	public void deletePlugin(final Plugin plugin) throws ServiceException
	{
		String simpleName = plugin.getName().toLowerCase().replace(" ", "");
		
		// Remove Client Plugin
		String jnlpLibsDir = getDomainRoot() + "applications" + File.separator
				+ "rc2s-jnlp" + File.separator
				+ "libs" + File.separator;
		File pluginClient = new File(jnlpLibsDir + simpleName + "_client.jar");
		
		if (pluginClient.exists())
			pluginClient.delete();
        
        jnlpService.updateJNLP(simpleName + "_client.jar", true);
		
		// Remove Server Plugin
		String autodeployDir = getDomainRoot() + "autodeploy" + File.separator;
		File pluginServer = new File(autodeployDir + simpleName + "_server.ear");
		
		if (pluginServer.exists())
			pluginServer.delete();
		
		pluginService.delete(plugin);
	}

	/**
	 * getDomainRoot
	 * 
	 * @return String domainRoot 
	 */
	private String getDomainRoot()
	{
		String domainRoot = System.getProperty("com.sun.aas.instanceRootURI");
		
		if (domainRoot != null)
		{
			if (domainRoot.startsWith("file:\\"))
				domainRoot = domainRoot.replace("file:\\", "");
			else if (domainRoot.startsWith("file://"))
				domainRoot = domainRoot.replace("file://", "");
			else if (domainRoot.startsWith("file:/"))
				domainRoot = domainRoot.replace("file:", "");

			if (System.getProperty("os.name").toLowerCase().contains("windows"))
				domainRoot = domainRoot.substring(1); // Remove leading slash on Windows
		}
		
		return domainRoot;
	}
}
