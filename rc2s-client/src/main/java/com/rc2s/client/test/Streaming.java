package com.rc2s.client.test;

import com.rc2s.client.Main;
import com.rc2s.common.utils.EJB;
import com.rc2s.ejb.streaming.StreamingFacadeRemote;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

public class Streaming extends Thread
{
	public static enum StreamingState
	{
		INIT, PLAY, PAUSE, STOP;
	}

	private StreamingFacadeRemote streamingEJB;

	private MediaPlayerFactory mediaPlayerFactory;
	private HeadlessMediaPlayer mediaPlayer;

	private StreamingState state;

	private String id;
	private String media;
	private String options;

    // For Server : sudo apt-get install libvlc-dev libvlccore-dev
    public Streaming(StreamingFacadeRemote streamingEJB, String id, String media) throws Exception
    {
		this.streamingEJB = streamingEJB;

		this.id = id;

		if(System.getProperty("os.name").toLowerCase().contains("windows"))
			this.media = media.replace('/', '\\');
		else
			this.media = media;

		this.options = formatRtspStream(EJB.getServerAddress(), EJB.getRtspPort(), id);

        System.out.println("Streaming '" + media + "' to '" + options + "'");

        mediaPlayerFactory = new MediaPlayerFactory();
        System.err.println("------- Launch Media Player -------");
        mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();

		setStreamingState(StreamingState.INIT);
    }

	@Override
	public synchronized void run()
	{
		mediaPlayer.playMedia(
			media,
			options,
			":no-sout-rtp-sap",
			":no-sout-standard-sap",
			":sout-all",
			":sout-keep"
		);

		System.err.println("------- MRL -------");
		String mrl = new RtspMrl().host(EJB.getServerAddress()).port(EJB.getRtspPort()).path("/" + id).value();

		System.err.println("------- Start Streaming RMI -------");
		streamingEJB.startStreaming(Main.getAuthenticatedUser(), mrl);
		setStreamingState(StreamingState.PLAY);
		System.err.println("------- Thread join -------");

		try
		{
			do
			{
				wait();
				System.out.println("---------------- After wait, state is: " + state.toString() + " -----------------");

				switch(state)
				{
					case PLAY:
						if(!mediaPlayer.isPlaying())
						{
							mediaPlayer.play();
							System.out.println("------ MediaPlayer resumed ------");
						}
						break;

					case PAUSE:
						if(mediaPlayer.isPlaying())
						{
							mediaPlayer.pause();
							System.out.println("------ MediaPlayer paused ------");
						}
						break;
				}

			} while(state != StreamingState.STOP);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		System.err.println("------- Stop Streaming RMI -------");
		streamingEJB.stopStreaming(Main.getAuthenticatedUser());
		System.err.println("------- Stop Media Player -------");
		mediaPlayer.stop();
		mediaPlayer.release();
	}

	public synchronized void setStreamingState(StreamingState state)
	{
		this.state = state;
	}

	public StreamingState getStreamingState()
	{
		return state;
	}

	public boolean isPlaying()
	{
		return mediaPlayer.isPlaying();
	}

	private String formatRtspStream(String serverAddress, int serverPort, String id)
	{
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{sdp=rtsp://@");
		sb.append(serverAddress);
		sb.append(':');
		sb.append(serverPort);
		sb.append('/');
		sb.append(id);
		sb.append("}");
		return sb.toString();
	}
}
