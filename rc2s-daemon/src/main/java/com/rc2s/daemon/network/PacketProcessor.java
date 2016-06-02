package com.rc2s.daemon.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketProcessor extends Thread
{
	private final DatagramSocket socket;
	private final DatagramPacket packet;
	
	/**
	 * Init a new PacketProcessor in its own thread in order to handle the
	 * packet's data.
	 * @param socket
	 * @param packet 
	 */
	public PacketProcessor(DatagramSocket socket, DatagramPacket packet)
	{
		this.socket = socket;
		this.packet = packet;
	}
	
	/**
	 * Run the thread and process the correct action.
	 */
	@Override
	public synchronized void run()
	{
		String raw = new String(packet.getData());
		System.out.println("RECEIVED: " + raw);
		
		switch(raw)
		{
			case "status": validateStatus(); break;
			default: processPacket(); break;
		}
	}
	
	/**
	 * Process the packet in order to obtain a valid signal.
	 */
	private void processPacket()
	{
		try(ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData()))
		{
			DataInputStream dis = new DataInputStream(bis);
			
			// TODO handle the byte array data
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Notify the application server that the daemon is up and running.
	 */
	private void validateStatus()
	{
		try
		{
			InetAddress clientIp = packet.getAddress();
			int clientPort = packet.getPort();
			byte[] buffer = "up".getBytes();

			DatagramPacket statusPacket = new DatagramPacket(buffer, buffer.length, clientIp, clientPort);
			socket.send(statusPacket);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
