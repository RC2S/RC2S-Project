package com.rc2s.application.services.daemon;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.Size;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
	public void updateState(Cube cube, Long duration, double[] pos, boolean state) throws ServiceException
	{
		byte[] packetContent = createPacket(duration, cube.getSize(), pos, state);		
		sendPacket(cube.getIp(), packetContent, false);
	}
	
	@Override
	public boolean isReachable(String ipAddress) throws ServiceException
	{
		byte[] response = sendPacket(ipAddress, "status".getBytes(), true);
		boolean isUp = false;
		
		if(response != null)
			isUp = new String(response).trim().equalsIgnoreCase("up");
		
		return isUp;
	}
	
	/**
	 * 
	 * @param duration
	 * @param size
	 * @param pos A Double array containing X [0], Y [1] and Z [2] values.
	 * If null, the complete Cube is considered 
	 * @param state
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public byte[] createPacket(Long duration, Size size, double[] pos, boolean state) throws ServiceException
	{
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream())
		{
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.writeLong(duration);
			dos.writeInt(size.getX());
			dos.writeInt(size.getY());
			dos.writeInt(size.getZ());
			
			// Write the Cube's state to the buffer
			for(int i = 0 ; i < size.getY() ; i++)
			{
				for(int j = 0 ; j < size.getZ() ; j++)
				{
					for(int k = 0 ; k < size.getX() ; k++)
					{
						if(pos != null)
						{
							// If we're specifying a LED, all others must be off.
							if(i == pos[1] && j == pos[2] && k == pos[1])
								dos.writeBoolean(state);
							else
								dos.writeBoolean(false);
						}
						else
							dos.writeBoolean(state);
					}
				}
			}
			
			dos.flush(); // Flush stream to write its content
			return bos.toByteArray();
		}
		catch(IOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public byte[] sendPacket(String ipAddress, byte[] data, boolean response) throws ServiceException
	{
		try(DatagramSocket socket = new DatagramSocket())
		{
			InetAddress daemonIp = InetAddress.getByName(ipAddress);
			
			DatagramPacket packet = new DatagramPacket(data, data.length, daemonIp, DAEMON_PORT);
			socket.send(packet);
			
			if(response)
			{
				try
				{
					socket.setSoTimeout(SOCKET_TIMEOUT);
					return getResponse(socket);
				}
				catch(SocketTimeoutException e)
				{
					System.err.println("Reached timeout...");
				}
			}
			
			return null;
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
