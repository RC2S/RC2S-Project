package com.rc2s.client;

import com.rc2s.common.utils.EJB;
import com.rc2s.ejb.plugin.loader.PluginLoaderFacadeRemote;
import java.util.List;
import javax.ejb.EJBException;

public class ClientTest
{
    public static void main(String[] args)
    {
		try
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
		}
    }
}
