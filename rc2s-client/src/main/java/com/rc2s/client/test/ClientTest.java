package com.rc2s.client.test;

import com.rc2s.common.vo.User;
import com.rc2s.ejb.authentication.AuthenticationFacadeRemote;
import com.rc2s.ejb.user.UserFacadeRemote;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClientTest
{
    public static void main(String[] args)
    {
        try
        {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            props.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
            props.put(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.put(Context.SECURITY_PRINCIPAL, "mathieu");
            props.put(Context.SECURITY_CREDENTIALS, "test");
            //props.put(Context.PROVIDER_URL, "127.0.0.1:3700");
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            props.put("org.omg.CORBA.ORBInitialPort", "3700");
            
            InitialContext ctx = new InitialContext(props);
            
            // Test UsersEJB
            UserFacadeRemote userEJB = (UserFacadeRemote) ctx.lookup("UserEJB");
			
            List<User> users = userEJB.getAllUsers();
            for(User user : users) {
                System.out.println(user.getId() + " " + user.getUsername() 
                    + " " + user.getPassword() + " " + user.getCreated());
            }
            
            // Test AuthenticationEJB
            AuthenticationFacadeRemote authenticationEJB = (AuthenticationFacadeRemote) ctx.lookup("AuthenticationEJB");
            //boolean auth = authenticationEJB.login();
        }
        catch(NamingException e)
        {
            e.printStackTrace();
        }
    }
}
