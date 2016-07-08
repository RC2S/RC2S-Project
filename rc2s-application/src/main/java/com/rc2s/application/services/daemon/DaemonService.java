package com.rc2s.application.services.daemon;

import com.rc2s.common.bo.CubeState;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.Size;

import javax.ejb.Stateless;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Map;

@Stateless
public class DaemonService implements IDaemonService
{
	private static final int DAEMON_PORT = 1337;
	private static final int BUFFER_LENGTH = 1024;
	private static final int SOCKET_TIMEOUT = 1000; // Timeout in milliseconds

	@Override
	public void sendCubesStates(final Map<Cube, CubeState> cubesStates) throws ServiceException
	{
		for(Map.Entry<Cube, CubeState> entry : cubesStates.entrySet())
		{
			Cube cube = entry.getKey();
			CubeState cubeState = entry.getValue();

			updateState(cube, 0L, cubeState.getStates());
		}
	}

	@Override
	public void updateState(final Cube cube, final Long duration, final boolean state) throws ServiceException
	{
		boolean[][][] states = generateBooleanArray(cube.getSize(), state);
		updateState(cube, duration, states);
	}

	@Override
	public void updateState(final Cube cube, final Long duration, final boolean[][][] states) throws ServiceException
	{
		boolean[][][] formatted = formatStatesArray(states);
		byte[] packetContent = createPacket(duration, cube.getSize(), formatted);
		sendPacket(cube.getIp(), packetContent, false);
	}

	@Override
	public boolean[][][] generateBooleanArray(final Size size, final boolean state)
	{
		boolean[][][] states = new boolean[size.getY()][size.getZ()][size.getX()];

		for(int i = 0; i < size.getY(); i++)
		{
			for(int j = 0; j < size.getZ(); j++)
			{
				for(int k = 0; k < size.getX(); k++)
				{
					states[i][j][k] = state;
				}
			}
		}

		return states;
	}

	@Override
	public boolean[][][] formatStatesArray(final boolean[][][] states)
	{
		for(int y = 0 ; y < states.length ; y++)
		{
			for(int z = 0 ; z < states[y].length / 2 ; z++)
			{
				int zOpposite = states[y].length - 1 - z;

				boolean[] zTmp = states[y][z];
				states[y][z] = states[y][zOpposite];
				states[y][zOpposite] = zTmp;
			}
		}

		return states;
	}
	
	@Override
	public boolean isReachable(final String ipAddress) throws ServiceException
	{
		byte[] response = sendPacket(ipAddress, "status".getBytes(), true);
		boolean isUp = false;
		
		if(response != null)
			isUp = new String(response).trim().equalsIgnoreCase("up");
		
		return isUp;
	}

	/**
	 * @param duration
	 * @param size
	 * @param states
	 * @return
	 * @throws ServiceException
     */
	@Override
	public byte[] createPacket(final Long duration, final Size size, final boolean[][][] states) throws ServiceException
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
						dos.writeBoolean(states[i][j][k]);
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
	public byte[] sendPacket(final String ipAddress, final byte[] data, final boolean response) throws ServiceException
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
	public byte[] getResponse(final DatagramSocket socket) throws IOException
	{
		byte[] buffer = new byte[BUFFER_LENGTH];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		
		return packet.getData();
	}
}
