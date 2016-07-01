package com.rc2s.client.test;

import com.rc2s.ejb.streaming.StreamingFacadeRemote;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

public class Streaming
{
    // For Server : sudo apt-get install libvlc-dev libvlccore-dev
    public Streaming(StreamingFacadeRemote streamingEJB) throws Exception
    {
        String media = "/media/Data/Datas/Music/Caravan_Palace/LoneDigger.mp3";
        String options = formatRtspStream("127.0.0.1", 5555, "audio");

        System.out.println("Streaming '" + media + "' to '" + options + "'");

        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        System.err.println("------- Launch Media Player -------");
        HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
        mediaPlayer.playMedia(media,
            options,
            ":no-sout-rtp-sap",
            ":no-sout-standard-sap",
            ":sout-all",
            ":sout-keep"
        );
        
        System.err.println("------- MRL -------");
        String mrl = new RtspMrl().host("127.0.0.1").port(5555).path("/audio").value();
        System.err.println("--- MRL Found : " + mrl + " ---");
		
        System.err.println("------- Start Streaming RMI -------");
        streamingEJB.startStreaming(mrl);
        System.err.println("------- Thread join -------");
        Thread.currentThread().join(60000l);
        
        System.err.println("------- Stop Streaming RMI -------");
        streamingEJB.stopStreaming();
        System.err.println("------- Stop Media Player -------");
        mediaPlayer.stop();
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
