package com.rc2s.ejb.plugin.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Stateless(mappedName = "PluginLoaderEJB")
public class PluginLoaderFacadeBean implements PluginLoaderFacadeRemote
{
	private static final String PLUGINS_LOCATION = "/home/esp010/Bureau/plugin/";
	
    @Override
    public boolean uploadPlugin()
    {
        return false;
    }

	/**
	 * Invoke a plugin's method.
	 * @param pluginName
	 * @param ejbName
	 * @param method
	 * @param args
	 * @return
	 * @throws EJBException 
	 */
    @Override
    public Object invoke(String pluginName, String ejbName, String method, Object... args) throws EJBException
    {
        try
        {
            /*String packagedEjb = "com.rc2s." + pluginName + ".ejb." + ejbName.toLowerCase() + "." + ejbName + "FacadeBean";
            File pluginFile = new File(PluginLoaderFacadeBean.PLUGINS_LOCATION + pluginName.toLowerCase().replace(" ", "") + "_server.jar");

            URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {pluginFile.toURI().toURL()}, this.getClass().getClassLoader());
            Class ejbClass = Class.forName(packagedEjb, true, urlClassLoader);
            Method ejbMethod = ejbClass.getMethod(method);
            Object ejbObj = ejbClass.newInstance();

            InitialContext ctx = new InitialContext();
            ctx.rebind(ejbName, ejbObj);


            Object test = ctx.lookup(ejbName);
            if(test != null)
                System.out.println("--------LOOKUP NOT NULL--------");
            else
                System.out.println("--------LOOKUP NULL--------");*/
            //ctx.lookup("java:global/rc2s/rc2s-ejb-1.0/PluginLoaderFacadeBean!com.rc2s.ejb.plugin.loader.PluginLoaderFacadeRemote");
            String jarLocation = PluginLoaderFacadeBean.PLUGINS_LOCATION + pluginName.toLowerCase().replace(" ", "") + "_server.jar";
            String packagedEjb = "com.rc2s." + pluginName + ".ejb." + ejbName.toLowerCase() + "." + ejbName + "FacadeBean";
            
            URLClassLoader loader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            PluginClassLoader pcl = new PluginClassLoader(loader.getURLs());
            pcl.addURL(new URL("file://" + jarLocation));
            Class c = pcl.loadClass(packagedEjb);
            
            InitialContext ctx = new InitialContext();
            ctx.rebind(ejbName, c.newInstance());
            
            List<String> names = new ArrayList();
            names.add(ejbName);
            names.add(c.getName());
            names.add("" + ctx.lookup(ejbName));
            
            return names;
            //return ejbMethod.invoke(ejbObj, args);
        }
        catch(MalformedURLException
        | ClassNotFoundException
        | IllegalArgumentException
        | NamingException
        | IllegalAccessException
        | InstantiationException
        /*| NoSuchMethodException
        | InvocationTargetException
        | NamingException*/ e)
        {
            throw new EJBException(e);
        }
    }
}
