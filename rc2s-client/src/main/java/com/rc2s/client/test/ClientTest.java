package com.rc2s.client.test;

import com.rc2s.common.vo.User;
import com.rc2s.ejb.streaming.StreamingFacadeRemote;
import com.rc2s.ejb.user.UserFacadeRemote;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClientTest
{
    public static void main(String[] args)
    {
        try
        {
            // Context
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            props.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
            props.put(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            props.put("org.omg.CORBA.ORBInitialPort", "3700");
            //props.put(Context.PROVIDER_URL, "192.168.1.107:3700");
            //System.setProperty("org.omg.CORBA.ORBInitialHost", "192.168.1.107");
            //System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
            
            InitialContext ctx = new InitialContext(props);
            
            // Test Get Users
            UserFacadeRemote userEJB = (UserFacadeRemote) ctx.lookup("UserEJB");
            ArrayList<User> users = userEJB.getAllUsers();
            
            for(User usr : users) {
                System.out.println(usr.getUsername() + " " + usr.getPassword());
            }
            
            // Test Streaming Start
            StreamingFacadeRemote streamingEJB = (StreamingFacadeRemote) ctx.lookup("StreamingEJB");
            
            Thread thread = new Thread() {
                public void run(){
                    try {
                        Streaming stream = new Streaming(streamingEJB);
                    } catch (Exception ex) {
                        Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE,
                            null, ex);
                    }
                }
            };
            thread.start();
        }
        catch(NamingException e)
        {
            e.printStackTrace();
        }
    }
}
