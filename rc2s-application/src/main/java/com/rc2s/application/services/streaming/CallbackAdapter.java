package com.rc2s.application.services.streaming;

import java.io.BufferedOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.directaudio.DefaultAudioCallbackAdapter;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

public class CallbackAdapter extends DefaultAudioCallbackAdapter
{
    private final Logger log = LogManager.getLogger(CallbackAdapter.class);
    
    private BufferedOutputStream out = null;
    
    public CallbackAdapter(int blockSize)
    {
        super(blockSize);
        out = new BufferedOutputStream(System.out);
    }

    @Override
    protected void onPlay(DirectAudioPlayer mediaPlayer, byte[] bytes, int sampleCount, long pts)
    {
		try
		{
			out.write(bytes);
			out.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }
    
    @Override
    public void flush(DirectAudioPlayer mediaPlayer, long pts)
    {
		try
		{
			out.flush();
			log.info("flush()");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }
    
    @Override
    public void drain(DirectAudioPlayer mediaPlayer)
    {
		try
		{
			out.close();
			log.info("drain()");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }
}
