package com.rc2s.client;

import com.rc2s.client.utils.EJB;
import com.rc2s.testplugin.ejb.bot.BotFacadeRemote;
import java.util.List;
import javax.ejb.EJBException;

public class ClientTest
{
    public static void main(String[] args)
    {		
		try
		{
			/*PluginLoaderFacadeRemote pluginLoader = (PluginLoaderFacadeRemote)EJB.lookup("PluginLoaderEJB");
			List<String> users = (List<String>)pluginLoader.invoke("testplugin", "User", "getUsers");*/
            BotFacadeRemote botEJB = (BotFacadeRemote) EJB.lookup("Bot");
            List<String> bots = botEJB.getBots();
			
			for(String bot : bots)
			{
				System.out.println(bot);
			}
		}
		catch(EJBException e)
		{
			e.printStackTrace();
		}
    }
}
