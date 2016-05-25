package com.rc2s.client;

import com.rc2s.client.utils.EJB;
import com.rc2s.ejb.plugin.loader.PluginLoaderFacadeRemote;
import java.io.File;
import java.nio.file.Files;
import javax.ejb.EJBException;

public class ClientTest
{
    public static void main(String[] args)
    {
		/*try
		{
			PluginLoaderFacadeRemote pluginLoader = (PluginLoaderFacadeRemote)EJB.lookup("PluginLoaderEJB");
			List<String> users = (List<String>)pluginLoader.invoke("testplugin", "User", "getUsers");
			
			for(String user : users)
			{
				System.out.println(user);
			}
		}
		catch(EJBException e)
		{
			e.printStackTrace();
		}*/
		
		try
		{
			PluginLoaderFacadeRemote pluginLoader = (PluginLoaderFacadeRemote)EJB.lookup("PluginLoaderEJB");
			boolean uploaded = pluginLoader.uploadPlugin("Test Plugin", Files.readAllBytes(new File("C:\\Users\\MrKloan\\Desktop\\rc2s-plugins\\testplugin_server.jar").toPath()));
			System.out.println(uploaded ? "Upload success!" : "Upload failed...");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
}
