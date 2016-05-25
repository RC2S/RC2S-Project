package com.rc2s.application.services.plugin.loader;

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
import javax.ejb.Stateless;

@Stateless
public class PluginLoaderService implements IPluginLoaderService
{
    @Override
    public boolean uploadPlugin(String pluginName, byte[] binaryPlugin)
    {
		try
		{
			String simpleName = pluginName.toLowerCase().replace(" ", "");
			File tmpPlugin = File.createTempFile(simpleName, ".zip");
			Files.write(tmpPlugin.toPath(), binaryPlugin);
			
			System.out.println(tmpPlugin.getAbsolutePath());
			
			/*/
			if(deployServerPlugin(simpleName, tmpEar))
			{
				tmpEar.delete();
				
				if(deployClientPlugin(simpleName, tmpJar))
				{
					tmpJar.delete();
				}
			}
			//*/
			
			tmpPlugin.delete();
			return true;
		}
		catch(IOException e)
		{
			return false;
		}
    }

    @Override
    public void unzipPlugin(String zipFile)
    {   	
        try
        {
            Path folderPath = Files.createTempDirectory("plugins");
            File folder = new File(folderPath.toString());
            
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
        }
        catch(IOException e)
        {
           e.printStackTrace(); 
        }
    }

    @Override
    public File checkServerPlugin(File tmpDir) throws Exception
    {
        try
		{
			return null;
		}
		catch(Exception e)
		{
			throw e;
		}
    }

    @Override
    public File checkClientPlugin(File tmpDir) throws Exception
    {
        try
		{
			return null;
		}
		catch(Exception e)
		{
			throw e;
		}
    }

    @Override
    public boolean deployServerPlugin(String simpleName, File tmpEar)
    {
		try
		{
			// System.getProperty("com.sun.aas.instanceRootURI") == payara41/glassfish/domains/rc2s-payara/
			String autodeployDir = System.getProperty("com.sun.aas.instanceRootURI") + "autodeploy/";
			File pluginFile = new File(autodeployDir + simpleName + "_server.ear");
			Files.copy(tmpEar.toPath(), pluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			return true;
		}
		catch(Exception e)
		{
			return false;
		}
    }

    @Override
    public boolean deployClientPlugin(String simpleName, File tmpJar)
    {
		try
		{
			String jnlpLibsDir = System.getProperty("com.sun.aas.instanceRootURI") + "applications/jnlpwar/libs/";
			File pluginFile = new File(jnlpLibsDir + simpleName + "_client.jar");
			Files.copy(tmpJar.toPath(), pluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
    }
}
