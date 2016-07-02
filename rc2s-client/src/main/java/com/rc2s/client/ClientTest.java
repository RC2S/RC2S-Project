package com.rc2s.client;

import com.rc2s.common.exceptions.RC2SException;
import com.rc2s.common.utils.EJB;
import com.rc2s.ejb.streaming.StreamingFacadeRemote;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTest
{
    public static void main(String[] args)
    {
		try
		{
			EJB.initContext("127.0.0.1", "3700");
		
			StreamingFacadeRemote streamingLoader = (StreamingFacadeRemote)EJB.lookup("StreamingEJB");

			streamingLoader.streamMusic();
		}
		catch (RC2SException ex)
		{
			Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		
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
		
		/*try
		{
			PluginLoaderFacadeRemote pluginLoader = (PluginLoaderFacadeRemote)EJB.lookup("PluginLoaderEJB");
			boolean uploaded = pluginLoader.uploadPlugin("Test Plugin", Files.readAllBytes(new File("D:\\testplugin.zip").toPath()));
			
			if(uploaded)
			{
				System.out.println("Upload success!");
				BotFacadeRemote bot = (BotFacadeRemote)EJB.lookup("BotEJB");
				
				for(String b : bot.getBots())
					System.out.println(b);
			}
			else
				System.out.println("Upload failed...");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
    }
}