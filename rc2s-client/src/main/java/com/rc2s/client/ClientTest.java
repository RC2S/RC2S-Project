package com.rc2s.client;

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
