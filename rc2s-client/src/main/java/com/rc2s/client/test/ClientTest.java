package com.rc2s.client.test;

import com.rc2s.common.utils.EJB;
import com.rc2s.ejb.streaming.StreamingFacadeRemote;
import javax.naming.NamingException;

public class ClientTest
{
    public static void main(String[] args)
    {
        try
        {
            // Test Streaming Start
            EJB.initContext("127.0.0.1", null);
            StreamingFacadeRemote streamingEJB = (StreamingFacadeRemote) EJB.lookup("StreamingEJB");
            
            System.err.println("------- Before Streaming -------");
            Streaming stream = new Streaming(streamingEJB);
            stream = null;
            System.err.println("------- After Streaming -------");
        }
        catch(NamingException e)
        {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
