package com.rc2s.application.services.plugin.loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    public void unzipPlugin()
    {
        
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
