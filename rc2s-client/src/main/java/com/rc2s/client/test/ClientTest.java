package com.rc2s.client.test;

import com.rc2s.common.vo.User;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

public class ClientTest
{
	public static void main(String[] args)
	{
		try
		{
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
			props.put(Context.PROVIDER_URL, "localhost:3700"); // GlassFish default port is 3700
		    InitialContext ctx = new InitialContext(props);
			
			
			NamingEnumeration<NameClassPair> list = ctx.list("");
			//Object obj = ctx.lookup("");
			while(list.hasMore()) 
			{
				System.out.println(list.next().getName());
			}
			
			/*
			com.rc2s.ejb.user.UserFacadeRemote#com.rc2s.ejb.user.UserFacadeRemote
			com.rc2s.ejb.user.UserFacadeRemote__3_x_Internal_RemoteBusinessHome__
			com.rc2s.ejb.user.UserFacadeRemote
			*/
			
			/*UserFacadeRemote userBean = (UserFacadeRemote)ctx.lookup("com.rc2s.ejb.user.UserFacadeRemote");
			List<User> users = userBean.getAllUsers();
			
			users.stream().forEach((user) -> {
				System.out.println(user.getUsername());
			});*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
