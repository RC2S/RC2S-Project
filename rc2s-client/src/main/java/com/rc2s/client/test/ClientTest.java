package com.rc2s.client.test;

import com.rc2s.common.vo.User;
import com.rc2s.ejb.user.UserFacadeRemote;
import java.util.ArrayList;
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
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            props.put("org.omg.CORBA.ORBInitialPort", "3700");
            
            InitialContext ctx = new InitialContext(props);
            UserFacadeRemote test = (UserFacadeRemote) ctx.lookup("UserEJB");
            ArrayList<User> tst = test.getAllUsers();
            
            for(User usr : tst) {
                System.out.println(usr.getUsername() + " " + usr.getPassword());
            }
        }
        catch(NamingException e)
        {
            e.printStackTrace();
        }
    }
}
