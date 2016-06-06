package com.rc2s.daemon.network;

import com.rc2s.daemon.Daemon;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class PacketProcessor extends Thread
{
	private final Daemon daemon;
	
	private final DatagramSocket socket;
	private final DatagramPacket packet;
	
	/**
	 * Init a new PacketProcessor in its own thread in order to handle the
	 * packet's data.
	 * @param daemon
	 * @param socket
	 * @param packet 
	 */
	public PacketProcessor(Daemon daemon, DatagramSocket socket, DatagramPacket packet)
	{
		this.daemon = daemon;
		
		this.socket = socket;
		this.packet = packet;
	}
	
	/**
	 * Run the thread and process the correct action.
	 */
	@Override
	public synchronized void run()
	{
		String raw = new String(packet.getData()).trim();
		System.out.println("Received: |" + raw + "|");
		
		if(raw.equals("status"))
			validateStatus();
		else
			processPacket();
	}
	
	/**
	 * Process the packet in order to obtain a valid signal.
	 */
	private void processPacket()
	{
		try(ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData()))
		{
			DataInputStream dis = new DataInputStream(bis);

			long duration = dis.readLong();

			int x = dis.readInt();
			int y = dis.readInt();
			int z = dis.readInt();

			List<Stage> stages = new ArrayList<>();
			
			for(int i = 0 ; i < y ; i++)
			{
				boolean[][] stageData = new boolean[x][z];
				
				for(int j = 0 ; j < x ; j++)
				{
					for(int k = 0 ; k < z ; k++)
					{
						stageData[j][k] = dis.readBoolean();
					}
				}
				
				stages.add(new Stage(stageData));
			}
			
			Packet processorPacket = new Packet(duration, stages.toArray(new Stage[]{}));
			daemon.getProcessor().add(processorPacket);
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
