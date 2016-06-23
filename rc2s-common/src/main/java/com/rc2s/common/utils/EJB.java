package com.rc2s.common.utils;

import com.rc2s.common.exceptions.RC2SException;
import com.sun.enterprise.security.ee.auth.login.ProgrammaticLogin;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJB
{
	private static String serverIp = "127.0.0.1";
	private static String serverPort = "3700";

	private static InitialContext context;

	public static void initContext(String ip, String port, String username, String password) throws RC2SException
	{
        try
		{
			if(ip != null)
				EJB.setServerAddress(ip);
			if(port != null)
				EJB.setServerPort(port);
            
            System.setProperty("java.security.auth.login.config", EJB.class.getResource("/jaas.config").toString());
			
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
			props.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
			props.put(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.put(Context.SECURITY_PRINCIPAL, username);
            props.put(Context.SECURITY_CREDENTIALS, password);
			props.put("org.omg.CORBA.ORBInitialHost", (EJB.serverIp));
			props.put("org.omg.CORBA.ORBInitialPort", (EJB.serverPort));
            
            /*ProgrammaticLogin pl = new ProgrammaticLogin();
            pl.login(username, password.toCharArray());*/
            
			EJB.context = new InitialContext(props);
		}
		catch (Exception e)
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
	
	private static void setServerAddress(String ip)
	{
		EJB.serverIp = ip;
	}
	
	public static String getServerAddress()
	{
		return EJB.serverIp;
	}
	
	private static void setServerPort(String port)
	{
		EJB.serverPort = port;
	}

	public static InitialContext getContext()
	{
		return EJB.context;
	}
}
