package com.rc2s.ejb.plugin.loader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

@Stateless(mappedName = "PluginLoaderEJB")
public class PluginLoaderFacadeBean implements PluginLoaderFacadeRemote
{
	private static final String PLUGINS_LOCATION = "D:\\rc2s-plugins\\";
	
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
			String packagedEjb = "com.rc2s." + pluginName + ".ejb." + ejbName.toLowerCase() + "." + ejbName + "FacadeBean";
			File pluginFile = new File(PluginLoaderFacadeBean.PLUGINS_LOCATION + pluginName.toLowerCase().replace(" ", "") + "_server.jar");
			
			URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {pluginFile.toURI().toURL()}, this.getClass().getClassLoader());
			Class ejbClass = Class.forName(packagedEjb, true, urlClassLoader);
			Method ejbMethod = ejbClass.getMethod(method);
			Object ejbObj = ejbClass.newInstance();
			
			return ejbMethod.invoke(ejbObj, args);
		}
		catch(MalformedURLException
			| ClassNotFoundException
			| NoSuchMethodException
			| InstantiationException
			| IllegalAccessException
			| IllegalArgumentException
			| InvocationTargetException e)
		{
			throw new EJBException(e);
		}
    }
}
