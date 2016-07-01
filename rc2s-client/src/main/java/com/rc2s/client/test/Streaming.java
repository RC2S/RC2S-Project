package com.rc2s.client.test;

import com.rc2s.ejb.streaming.StreamingFacadeRemote;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

public class Streaming extends Thread
{
	private StreamingFacadeRemote streamingEJB;

	private MediaPlayerFactory mediaPlayerFactory;
	private HeadlessMediaPlayer mediaPlayer;

	private String media = "D:\\Musique\\AC-DC - Black Ice\\Rock N Roll Train.mp3";
	private String options;

    // For Server : sudo apt-get install libvlc-dev libvlccore-dev
    public Streaming(StreamingFacadeRemote streamingEJB) throws Exception
    {
		this.streamingEJB = streamingEJB;

        System.out.println("Streaming '" + media + "' to '" + options + "'");

        mediaPlayerFactory = new MediaPlayerFactory();
        System.err.println("------- Launch Media Player -------");
        mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();

		options = formatRtspStream("127.0.0.1", 5555, "audio");
    }

	@Override
    public void run()
	{
		mediaPlayer.playMedia(media,
				options,
				":no-sout-rtp-sap",
				":no-sout-standard-sap",
				":sout-all",
				":sout-keep"
		);

		System.err.println("------- MRL -------");
		String mrl = new RtspMrl().host("127.0.0.1").port(5555).path("/audio").value();

		System.err.println("------- Start Streaming RMI -------");
		streamingEJB.startStreaming(mrl);
		System.err.println("------- Thread join -------");

		try
		{
			// TODO: Required or the content is not streamed?!
			Thread.currentThread().join(60000L);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		System.err.println("------- Stop Streaming RMI -------");
		streamingEJB.stopStreaming();
		System.err.println("------- Stop Media Player -------");
		mediaPlayer.stop();
		mediaPlayer.release();
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
