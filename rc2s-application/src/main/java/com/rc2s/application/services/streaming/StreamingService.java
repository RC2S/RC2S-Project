package com.rc2s.application.services.streaming;

import com.rc2s.application.services.daemon.IDaemonService;
import com.rc2s.common.bo.CubeState;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.Synchronization;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * StreamingService
 * 
 * Service for streaming
 * Works with the IDaemonService
 * 
 * @author RC2S
 */
@Stateful
public class StreamingService implements IStreamingService
{
    private static final Logger log = LogManager.getLogger(StreamingService.class);

	@EJB
	private IDaemonService daemonService;

	private Synchronization synchronization;
    
    // Synchronisation object to wait for the audio to finish.
    private Semaphore sync = new Semaphore(0);
	private CallbackAdapter callbackAdapter;

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
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("jna.library.path", "C:\\Program Files\\VideoLAN\\VLC");
	}

	/**
	 * start
	 * 
	 * Start streaming on the given mrl
	 * 
	 * @param mrl 
	 */
    @Override
    public void start(final String mrl)
    {
		factory = new MediaPlayerFactory();
		callbackAdapter = new CallbackAdapter(this, 4, getSyncSize());

		// newDirectAudioPlayer(format, rate, channel, new callback(blocksize of samples))
		audioPlayer = factory.newDirectAudioPlayer("S16N", 44100, 2, callbackAdapter);

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
					log.error(e);
				}

				log.info("Finished, releasing native resources...");

				if (audioPlayer.isPlaying())
				{
					audioPlayer.release();
					factory.release();
				}

				log.info("All done");
			}
		};
		thread.start();
    }

	/**
	 * stop
	 * 
	 * Stop streaming
	 */
    @Override
    public void stop()
    {
		synchronized (factory) {
			log.info("Stop streaming");
			
            if (audioPlayer.isPlaying()) {
				audioPlayer.stop();

				sync.release();
				audioPlayer.release();
				factory.release();
			}
		}
    }

	/**
	 * processCoordinates
	 * 
	 * Process coordinates retrieved from the sound algorithm
	 * and send them to the daemon service
	 * 
	 * @param coordinates 
	 */
	@Override
	public void processCoordinates(final int[][] coordinates)
	{
		if(synchronization == null)
			return;

		Map<Cube, CubeState> cubesStates = new HashMap<>();

		for (Cube cube : synchronization.getCubes())
		{
			CubeState cubeState = new CubeState(daemonService.generateBooleanArray(cube.getSize(), false));

			for (int[] position : coordinates)
			{
				// Skip negative values
				if (position[0] < 0)
					continue;

				// If the position belongs to another Cube
				if (position[0] >= cube.getSize().getX())
				{
					position[0] -= cube.getSize().getX();
					continue;
				}

				cubeState.set(position[0], position[1], position[2], true);
				position[0] = -1;
			}

			cubesStates.put(cube, cubeState);
		}

		try
		{
			daemonService.sendCubesStates(cubesStates);
		}
		catch (ServiceException e)
		{
			log.error(e);
		}
	}

	/**
	 * getSyncSize
	 * 
	 * Get the synchronisation (x, y, z) size in order to be able
	 * to compute the algorithm on several cubes
	 * 
	 * Also gets the number of cubes
	 * 
	 * @return int[4] containing the synchronisation list size
	 * & number of cubes 
	 */
	@Override
	public int[] getSyncSize()
	{
		int[] syncSize = new int[] {0, 0, 0, 0};
		
		if (synchronization != null)
		{
			boolean first = true;

			for (Cube cube : synchronization.getCubes())
			{
				System.out.println("LOG : ncubes + 1");
				syncSize[3]++;
				
				syncSize[0] += cube.getSize().getX();

				if (first)
				{
					first = false;

					syncSize[1] = cube.getSize().getY();
					syncSize[2] = cube.getSize().getZ();
				}
				else
				{
					syncSize[1] = Math.min(cube.getSize().getY(), syncSize[1]);
					syncSize[2] = Math.min(cube.getSize().getZ(), syncSize[2]);
				}
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
	public void setSynchronization(final Synchronization synchronization)
	{
		this.synchronization = synchronization;

		if (callbackAdapter != null)
			callbackAdapter.setDimensions(getSyncSize());
	}
}