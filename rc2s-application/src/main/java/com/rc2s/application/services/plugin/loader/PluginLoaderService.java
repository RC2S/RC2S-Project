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
			
			return (tmpEar != null && tmpJar != null
				&& deployServerPlugin(simpleName, tmpEar)
				&& deployClientPlugin(simpleName, tmpJar));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
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
           e.printStackTrace(); 
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
    public boolean deployServerPlugin(String simpleName, File tmpEar)
    {
		try
		{			
			String autodeployDir = getDomainRoot() + "autodeploy" + File.separator;
			File pluginFile = new File(autodeployDir + simpleName + "_server.ear");
			Files.copy(tmpEar.toPath(), pluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
    }

    @Override
    public boolean deployClientPlugin(String simpleName, File tmpJar)
    {
		try
		{
			String jnlpLibsDir = getDomainRoot() + "applications" + File.separator + "jnlpwar" + File.separator + "libs" + File.separator;
			File pluginFile = new File(jnlpLibsDir + simpleName + "_client.jar");
			Files.copy(tmpJar.toPath(), pluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
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
				domainRoot = domainRoot.replace("file:/", "");
		}
		
		return domainRoot;
	}
}
