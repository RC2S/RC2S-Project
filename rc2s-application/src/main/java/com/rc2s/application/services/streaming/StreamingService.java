package com.rc2s.application.services.streaming;

import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.Synchronization;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

import javax.ejb.Stateful;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

@Stateful
public class StreamingService implements IStreamingService
{
    private static final Logger log = LogManager.getLogger(StreamingService.class);

	private Synchronization synchronization;
    
    // Synchronisation object to wait for the audio to finish.
    private Semaphore sync = new Semaphore(0);

    private MediaPlayerFactory factory;
    private DirectAudioPlayer audioPlayer;

	/**
	 * 1. The attributes initialization was moved in the start method in order to be able to stream
	 * multiple tracks one after another.
	 * The major problem here is that when a new track is being played, it will overwrite the previous
	 * one's attribute values, leading to unknown behaviour.
	 * In addition, the streamed content is read from a static URL : 127.0.0.1:5555/audio. What happens
	 * if two people are streaming at the same time?
	 *
	 * 2. It is *NOT* possible to bundle the VLC native libs (libvlc, libvlccore) for one simple reason:
	 * it requires to have access to the "plugins" directory content from VLC installation folder
	 * in the same directory as the currently running native library (i.e. the temp directory).
	 */
	public StreamingService()
    {
		if(System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("jna.library.path", "C:\\Program Files\\VideoLAN\\VLC");
	}

    @Override
    public void start(String mrl)
    {
		factory = new MediaPlayerFactory();
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

		Thread thread = new Thread() {
			public void run() {
				synchronized (audioPlayer) {
					log.info("Begin start " + mrl);
					audioPlayer.playMedia(mrl);
				}

				log.info("Waiting for finished...");

				try {
					// Slight race condition in theory possible if the audio finishes immediately
					// (but this is just a test so it's good enough)...
					sync.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				log.info("Finished, releasing native resources...");

				if(audioPlayer.isPlaying())
				{
					audioPlayer.release();
					factory.release();
				}

				log.info("All done");
			}
		};
		thread.start();
    }

    @Override
    public void stop()
    {
		synchronized (audioPlayer) {
			log.info("Stop streaming");
			if (audioPlayer.isPlaying()) {
				audioPlayer.stop();

				sync.release();
				audioPlayer.release();
				factory.release();
			}
		}
    }

	@Override
	public int[] getSyncSize()
	{
		int[] syncSize = new int[] {0, 0, 0};

		if(synchronization != null)
		{
			for(Cube cube : synchronization.getCubes())
			{
				syncSize[0] = cube.getSize().getX();
				syncSize[1] = cube.getSize().getY();
				syncSize[2] = cube.getSize().getZ();
			}
		}

		return syncSize;
	}

	@Override
	public Synchronization getSynchronization()
	{
		return synchronization;
	}

	@Override
	public void setSynchronization(Synchronization synchronization)
	{
		this.synchronization = synchronization;
	}
}