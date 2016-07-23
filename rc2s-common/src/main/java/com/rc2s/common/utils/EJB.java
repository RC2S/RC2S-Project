package com.rc2s.common.utils;

import com.rc2s.common.exceptions.RC2SException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * EJB
 * 
 * Basic implementation to use EJBs
 * Contains all the methods necessary to EJBs
 * to be initializable, usable and set
 * 
 * @author RC2S
 */
public class EJB
{
	private static final Integer RTSP_PORT = 5555;

	private static String serverIp = "127.0.0.1";
	private static String serverPort = "3700";

	private static InitialContext context;

	/**
	 * initContext
	 * 
	 * Initializes the EJB following the initial context
	 * 
	 * @param ip
	 * @param port
	 * @throws RC2SException 
	 */
	public static void initContext(final String ip, final String port) throws RC2SException
	{
        try
		{
			if (ip != null)
				EJB.setServerAddress(ip);
			if (port != null)
				EJB.setServerPort(port);
			
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
			props.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
			props.put(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			props.put("org.omg.CORBA.ORBInitialHost", EJB.serverIp);
			props.put("org.omg.CORBA.ORBInitialPort", EJB.serverPort);
            
			EJB.context = new InitialContext(props);
		}
		catch (Exception e)
		{
			throw new RC2SException(e);
		}
	}

	/**
	 * lookup
	 * 
	 * Allows to retrieve an EJB by its name
	 * 
	 * @param ejbName
	 * @return Object EJB retrieved 
	 */
	public static Object lookup(final String ejbName)
	{
		try
		{
            return EJB.context != null ? EJB.context.lookup(ejbName) : null;
		} catch (NamingException e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}
    
	/**
	 * closeContext()
	 * 
	 * End context after use
	 */
    public static void closeContext()
    {
        try
		{
            if (context != null)
                context.close();
        }
		catch (NamingException e)
		{
            System.err.println(e.getMessage());
        }
    }
	
	private static void setServerAddress(final String ip)
	{
		EJB.serverIp = ip;
	}
	
	public static String getServerAddress()
	{
		return EJB.serverIp;
	}
	
	private static void setServerPort(final String port)
	{
		EJB.serverPort = port;
	}

	public static InitialContext getContext()
	{
		return EJB.context;
	}

	public static Integer getRtspPort()
	{
		return EJB.RTSP_PORT;
	}
}
