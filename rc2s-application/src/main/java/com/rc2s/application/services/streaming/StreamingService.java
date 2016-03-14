package com.rc2s.application.services.streaming;

import java.util.concurrent.Semaphore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

@Service
public class StreamingService implements IStreamingService
{
    private static final Logger log = LogManager.getLogger(StreamingService.class);
    
    // Synchronisation object to wait for the audio to finish.
    private final Semaphore sync = new Semaphore(0);
    
    private final MediaPlayerFactory factory;
    
    private final MediaPlayer audioPlayer;
    
    public StreamingService()
    {
        factory     = new MediaPlayerFactory();
        
        // newDirectAudioPlayer(format, rate, channel, new callback(blocksize of samples))
        audioPlayer = factory.newDirectAudioPlayer("S16N", 44100, 2, new CallbackAdapter(4));
        
        audioPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                log.info("playing()");
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                log.info("finished()");
                log.info("Release waiter...");
                sync.release();
                log.info("After release waiter");
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                log.info("error()");
            }
        });
    }

    @Override
    public void start(String mrl)
    {
        log.info("Begin start " + mrl);
        audioPlayer.playMedia(mrl);
        
        log.info("Waiting for finished...");

        try {
            // Slight race condition in theory possible if the audio finishes immediately
            // (but this is just a test so it's good enough)...
            sync.acquire();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Finished, releasing native resources...");

        audioPlayer.release();
        factory.release();

        log.info("All done");
    }

    @Override
    public void stop()
    {
        log.info("Stop streaming");
        if(audioPlayer.isPlaying())
        {
            audioPlayer.stop();
            
            sync.release();
            audioPlayer.release();
            factory.release();
        }
    }
}
