package com.rc2s.daemon.network;

import com.rc2s.daemon.Daemon;
import java.net.DatagramSocket;

public class Listener extends Thread
{
    private Daemon daemon;
    private DatagramSocket socket;

    public Listener(Daemon daemon)
    {
        this.daemon = daemon;
    }

    @Override
    public synchronized void start()
    {
        // https://systembash.com/a-simple-java-udp-server-and-udp-client/
    }
}
