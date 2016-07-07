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
			//System.setProperty("jna.library.path", "C:\\Program Files\\VideoLAN\\VLC");
			
            // Test Streaming Start
            EJB.initContext("127.0.0.1", null);
            StreamingFacadeRemote streamingEJB = (StreamingFacadeRemote) EJB.lookup("StreamingEJB");
            
            System.err.println("------- Before Streaming -------");
            Streaming stream = new Streaming(streamingEJB, "audio", "D:\\Musique\\AC-DC - Black Ice\\Rock N Roll Train.mp3");
            stream.start();
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
