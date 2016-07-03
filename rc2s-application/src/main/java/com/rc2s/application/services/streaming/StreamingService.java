package com.rc2s.application.services.streaming;

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

	@Override
	public void musicAlgorithm()
	{
		System.out.println("IN");
		String music = "/media/Data/Datas/Music/Caravan_Palace/LoneDigger.mp3";

		File f = new File(music);
		int size = (int)f.length();
		byte[] soundByteArray = new byte[size];

		try
		{
			FileInputStream fis = new FileInputStream(f);

			fis.read(soundByteArray);

			// Test data
			StringBuilder sb = new StringBuilder();
			int mini = 10000;
			int maxi = -10000;
			int total = 0;
			int count = 0;

			// Coordinates arraylist
			ArrayList<Coordinates3D> coordinatesBufferlist = new ArrayList<>();

			// Buffer length wanted for coordinates retrieving
			final short BUFFER_LENGTH = 300;
			short currentLength = 0;

			// Creating byte buffer
			byte[] byteBuffer = new byte[BUFFER_LENGTH];

			for (byte data : soundByteArray)
			{
				if (currentLength < BUFFER_LENGTH)
				{
					byteBuffer[currentLength] = data;
					currentLength++;
				}
				else
				{
					coordinatesBufferlist.addAll(mapBufferByteArrayToCoordinates3D(byteBuffer));
					currentLength = 0;
				}

				// Test
				count++;
				total += data;

				if (mini > data)
					mini = data;
				if (maxi < data)
					maxi = data;

				sb.append(data).append(", ");
			}

			System.out.println("Count : " + count);
			System.out.println("Total : " + total);
			System.out.println("Mini  : " + mini);
			System.out.println("Maxi  : " + maxi);
			System.out.println(sb.substring(0, 1000));
			// End Test

			/* Classical 3D mapping
			System.out.println("\nPrinting Coordinates");
			for (int index = 300; index < 800; index++)
				System.out.println(coordinatesBufferlist.get(index));
			*/
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 *
	 * CoordinateMapper3D v0.1
	 * [byte, byte, byte] -> coordinate(x, y, z)
	 *
	 */
	private ArrayList<Coordinates3D> mapBufferByteArrayToCoordinates3D(byte[] soundBytes)
	{
		ArrayList<Coordinates3D> mappedSoundData = new ArrayList<>();

		for (int i = 0; i < soundBytes.length; i += 3)
		{
			mappedSoundData.add(
					new Coordinates3D(
							convertByteToPosition3D(soundBytes[i]),
							convertByteToPosition3D(soundBytes[i+1]),
							convertByteToPosition3D(soundBytes[i+2])
					)
			);
		}

		return mappedSoundData;
	}

	private byte convertByteToPosition3D(byte entry)
	{
		return (byte) ((entry / 64) + 2);
	}

	private class Coordinates3D
	{
		private final byte row;
		private final byte column;
		private final byte depth;

		public Coordinates3D(byte row, byte column, byte depth)
		{
			this.row	= row;
			this.column = column;
			this.depth	= depth;
		}

		public byte getRow()
		{
			return row;
		}

		public byte getColumn()
		{
			return column;
		}

		public byte getDepth()
		{
			return depth;
		}

		@Override
		public String toString()
		{
			return "Coord('" + row + "', '" + column + "', '" + depth + "')";
		}
	}
	/* END CoordinateMapper3D */
}