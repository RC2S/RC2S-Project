package com.rc2s.application.daemon;

import com.rc2s.common.exceptions.ServiceException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import javax.ejb.Stateless;

@Stateless
public class DaemonService implements IDaemonService
{
	private static final int DAEMON_PORT = 1337;
	private static final int BUFFER_LENGTH = 1024;
	private static final int SOCKET_TIMEOUT = 1000; // Timeout in milliseconds
	
	@Override
	public boolean isReachable(String ipAddress) throws ServiceException
	{
		try(DatagramSocket socket = new DatagramSocket())
		{
			InetAddress daemonIp = InetAddress.getByName(ipAddress);
			byte[] data = new String("status").getBytes();
			
			DatagramPacket packet = new DatagramPacket(data, data.length, daemonIp, DAEMON_PORT);
			socket.send(packet);
			
			boolean isUp = false;
			
			try
			{
				socket.setSoTimeout(SOCKET_TIMEOUT);
				byte[] response = getResponse(socket);
				isUp = new String(response).trim().equalsIgnoreCase("up");
			}
			catch(SocketTimeoutException e)
			{
				System.err.println("Reached timeout...");
			}
			
			return isUp;
		}
		catch(IOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public byte[] getResponse(DatagramSocket socket) throws IOException
	{
		byte[] buffer = new byte[BUFFER_LENGTH];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		
		return packet.getData();
	}
}
