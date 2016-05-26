package com.rc2s.application.services.streaming;

import java.util.concurrent.Semaphore;
import javafx.scene.media.MediaPlayer;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

@Stateless
public class StreamingService implements IStreamingService
{
    private static final Logger log = LogManager.getLogger(StreamingService.class);
    
    // Synchronisation object to wait for the audio to finish.
    private Semaphore sync = new Semaphore(0);
    
    private MediaPlayerFactory factory;
    
    private DirectAudioPlayer audioPlayer;
    
    public StreamingService()
    {
        factory     = new MediaPlayerFactory();
        
        // newDirectAudioPlayer(format, rate, channel, new callback(blocksize of samples))
        audioPlayer = factory.newDirectAudioPlayer("S16N", 44100, 2, new CallbackAdapter(4));
        
        audioPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            public void playing(MediaPlayer mediaPlayer) {
                log.info("playing()");
            }
            
            public void finished(MediaPlayer mediaPlayer) {
                log.info("finished()");
                log.info("Release waiter...");
                sync.release();
                log.info("After release waiter");
            }

            public void error(MediaPlayer mediaPlayer) {
                log.info("error()");
            }
        });
    }

    @Override
    public void start(String mrl)
    {
        Thread thread = new Thread() {
            public void run() {
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
        };
        thread.start();
    }

    @Override
    public void stop()
    {
        Thread thread = new Thread() {
            public void run() {
                log.info("Stop streaming");
                if(audioPlayer.isPlaying())
                {
                    audioPlayer.stop();

                    sync.release();
                    audioPlayer.release();
                    factory.release();
                }
            }
        };
        thread.start();
    }
}
