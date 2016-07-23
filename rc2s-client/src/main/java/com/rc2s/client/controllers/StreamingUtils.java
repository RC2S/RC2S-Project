package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import com.rc2s.client.utils.Tools;
import com.rc2s.common.utils.EJB;
import com.rc2s.ejb.streaming.StreamingFacadeRemote;
import java.io.File;
import java.net.UnknownHostException;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

public class StreamingUtils extends Thread
{
	public static enum StreamingState {
		INIT, PLAY, PAUSE, STOP;
	}

	private StreamingFacadeRemote streamingEJB;

	private MediaPlayerFactory mediaPlayerFactory;
	private HeadlessMediaPlayer mediaPlayer;

	private StreamingState state;

	private String id;
	private String media;

	private String options;

    // To use Streaming feature : sudo apt-get install vlc libvlc-dev libvlccore-dev
    public StreamingUtils(final StreamingFacadeRemote streamingEJB,
            final String id, final String media) throws Exception
    {
		this.streamingEJB = streamingEJB;

		this.id = id;

		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			this.media = media.replace('/', File.separatorChar);
		else
			this.media = media;

		this.options = formatRtspStream(Tools.getIPAdress(), EJB.getRtspPort(), id);

        System.out.println("StreamingUtils '" + media + "' to '" + options + "'");

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

        try
		{
            System.err.println("------- MRL -------");
            String mrl = new RtspMrl().host(Tools.getIPAdress()).port(EJB.getRtspPort()).path("/" + id).value();

            System.err.println("------- Start StreamingUtils RMI -------");
			System.err.println("User : " + EJB.getAuthenticatedUser().toString());
			System.err.println("MRL : " + mrl);
            streamingEJB.startStreaming(EJB.getAuthenticatedUser(), mrl);
            setStreamingState(StreamingState.PLAY);
            System.err.println("------- Thread join -------");

			do
			{
				wait();
				System.err.println("------ After wait, state is: " + state.toString() + " ------");

				switch (state)
				{
					case PLAY:
						if (!mediaPlayer.isPlaying())
						{
							mediaPlayer.play();
							System.err.println("------ MediaPlayer resumed ------");
						}
						break;

					case PAUSE:
						if (mediaPlayer.isPlaying())
						{
							mediaPlayer.pause();
							System.err.println("------ MediaPlayer paused ------");
						}
						break;
				}

			} while (state != StreamingState.STOP);
		}
		catch (InterruptedException | UnknownHostException e) {}
        
		System.err.println("------- Stop StreamingUtils RMI -------");
		streamingEJB.stopStreaming(EJB.getAuthenticatedUser());
		System.err.println("------- Stop Media Player -------");
		mediaPlayer.stop();
		mediaPlayer.release();
	}

	public synchronized void setStreamingState(final StreamingState state)
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

	private String formatRtspStream(final String serverAddress,
            final int serverPort, final String id)
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
