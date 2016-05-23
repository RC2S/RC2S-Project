package com.rc2s.common.utils;

import com.rc2s.common.exceptions.RC2SException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJB
{
	private static final String DEFAULT_IP = "127.0.0.1";
	private static final String DEFAULT_PORT = "3700";

	private static InitialContext context;

	public static void initContext(String ip, String port) throws RC2SException
	{
		try
		{
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
			props.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
			props.put(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			props.put("org.omg.CORBA.ORBInitialHost", (ip != null ? ip : EJB.DEFAULT_IP)); // Default 127.0.0.1
			props.put("org.omg.CORBA.ORBInitialPort", (port != null ? port : EJB.DEFAULT_PORT)); // Default 

			EJB.context = new InitialContext(props);
		} catch (NamingException e)
		{
			throw new RC2SException(e);
		}
	}

	public static Object lookup(String ejbName)
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

	public static InitialContext getContext()
	{
		return EJB.context;
	}
}
