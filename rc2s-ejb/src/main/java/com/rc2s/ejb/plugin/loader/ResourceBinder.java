
package com.rc2s.ejb.plugin.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;

@Startup
@Singleton
public class ResourceBinder
{
    @PostConstruct
    public void bindResources() {
        /*try
        {
            String pluginName = "testplugin";
            String ejbName = "Bot";
            String jarLocation = "/home/esp010/Bureau/plugin/" + pluginName +"_server.jar";
            String packagedEjb = "com.rc2s." + pluginName + ".ejb." + ejbName.toLowerCase() + "." + ejbName + "FacadeBean";
            
            //URLClassLoader loader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            //PluginClassLoader pcl = new PluginClassLoader(loader.getURLs());
            //pcl.addURL(new URL("file://" + jarLocation));
            //Class c = pcl.loadClass(packagedEjb);
            //System.out.println(c.getName());
            Class c = getClass().getClassLoader().loadClass(packagedEjb);
            System.out.println("---- Name --- " + c.getName());
            
            InitialContext ctx = new InitialContext();
            ctx.rebind("Bot#com.rc2s.testplugin.ejb.bot.BotFacadeBean", new Reference(c.getName()));
            ctx.rebind("Bot", new Reference(c.getName()));
            ctx.rebind("Bot__3_x_Internal_RemoteBusinessHome__", new Reference(c.getName()));
            System.out.println("After rebind");
        } catch (NamingException
            //| InstantiationException
            //| MalformedURLException
            | ClassNotFoundException
            | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }*/
        System.out.println("------ In Bind Resources -----");
    }
}
