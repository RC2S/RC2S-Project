package com.rc2s.client.utils;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJB
{
	private final static InitialContext context = EJB.initContext();
	
	private static InitialContext initContext()
	{
		InitialContext ctx = null;
		
		try
		{
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
			props.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
			props.setProperty(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			props.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
			props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
            
			ctx = new InitialContext(props);
		}
		catch (NamingException ex)
		{
			System.err.println(ex);
		}
		
		return ctx;
	}
	
	public static Object lookup(String ejbName)
	{
		try
		{
			return EJB.context.lookup(ejbName);
		}
		catch(NamingException e)
		{
			System.err.println(e);
			return null;
		}
	}
	
	public static InitialContext getContext()
	{
		return EJB.context;
	}
}
