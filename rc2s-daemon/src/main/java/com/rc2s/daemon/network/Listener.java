package com.rc2s.daemon.network;

import com.rc2s.daemon.Daemon;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Listener
 * 
 * Daemon's runnable listener used for packets reception
 * 
 * @author captp
 */
public class Listener extends Thread
{
	private static final int BUFFER_LENGHT = 1024;
	
    private final Daemon daemon;
	private final int port;
    private DatagramSocket socket;

	/**
	 * Create a new UDP listener.
	 * @param daemon
	 * @param port 
	 */
    public Listener(final Daemon daemon, final int port)
    {
		this.daemon = daemon;
		this.port = port;
			
		try
		{
			this.socket = new DatagramSocket(port);
		}
		catch(SocketException e)
		{
			e.printStackTrace();
		}
    }

	/**
	 * Run the listener's thread and listen for the application server
	 * packets.
	 */
    @Override
    public synchronized void run()
    {
		try
		{
			// Listener loop
			while(daemon.isRunning())
			{
				System.out.println("Listening on port " + port + "...");
				byte[] buffer = new byte[BUFFER_LENGHT];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet); // Listen for incoming packets
				
				PacketProcessor processor = new PacketProcessor(daemon, socket, packet);
				processor.start();
			}
			
			socket.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }
}
