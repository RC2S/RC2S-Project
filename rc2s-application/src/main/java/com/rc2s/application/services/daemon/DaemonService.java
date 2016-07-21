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
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * DaemonService
 * 
 * Service for communication with the RC2S daemon
 * 
 * @author RC2S
 */
@Stateless
public class DaemonService implements IDaemonService
{
	@Inject
    private Logger log;
    
    private static final int DAEMON_PORT = 1337;
	private static final int BUFFER_LENGTH = 1024;
	private static final int SOCKET_TIMEOUT = 1;
	private static final int SOCKET_LISTEN_TIMEOUT = 1000;

	/**
	 * sendCubesStates
	 * 
	 * Send a list of states to the daemon
	 * 
	 * A map Cube - CubeState contains the 
	 * informations daemon should receive
	 * in order to manipulate their cube.
	 * 
	 * @param cubesStates
	 * @throws ServiceException 
	 */
	@Override
	public void sendCubesStates(final Map<Cube, CubeState> cubesStates) throws ServiceException
	{
		// Set packet duration to 1 ms: we want it to be played just once!
		final long packetDuration = 1L;

		for (Map.Entry<Cube, CubeState> entry : cubesStates.entrySet())
		{
			Cube cube = entry.getKey();
			CubeState cubeState = entry.getValue();

			updateState(cube, packetDuration, cubeState.getStates());
		}
	}

	/**
	 * updateState(cube, duration, state)
	 * 
	 * Update a Cube's state during a given duration
	 * 
	 * @param cube
	 * @param duration
	 * @param state
	 * @throws ServiceException 
	 */
	@Override
	public void updateState(final Cube cube, final Long duration, final boolean state) throws ServiceException
	{
		boolean[][][] states = generateBooleanArray(cube.getSize(), state);
		updateState(cube, duration, states);
	}

	/**
	 * updateState(cube, duration, states)
	 * 
	 * Update several states in a Cube during a given duration
	 * 
	 * @param cube
	 * @param duration
	 * @param states
	 * @throws ServiceException 
	 */
	@Override
	public void updateState(final Cube cube, final Long duration, final boolean[][][] states) throws ServiceException
	{
		boolean[][][] formatted = formatStatesArray(states);
		byte[] packetContent = createPacket(duration, cube.getSize(), formatted);
		sendPacket(cube.getIp(), packetContent, false);
	}

	/**
	 * generateBooleanArray(size, state)
	 * 
	 * Get a 3D cube representation for a given state
	 * 
	 * @param size
	 * @param state
	 * @return boolean[][][] representing a cube state 
	 */
	@Override
	public boolean[][][] generateBooleanArray(final Size size, final boolean state)
	{
		boolean[][][] states = new boolean[size.getY()][size.getZ()][size.getX()];

		for (int i = 0; i < size.getY(); i++)
		{
			for (int j = 0; j < size.getZ(); j++)
			{
				for (int k = 0; k < size.getX(); k++)
				{
					states[i][j][k] = state;
				}
			}
		}

		return states;
	}

	/**
	 * formatStatesArray(states)
	 * 
	 * Re-order the boolean array to adjust it to daemon's needs
	 * 
	 * @param states
	 * @return boolean[][][] representing a cube state
	 */
	@Override
	public boolean[][][] formatStatesArray(final boolean[][][] states)
	{
		for (int y = 0 ; y < states.length ; y++)
		{
			for (int z = 0; z < states[y].length / 2; z++)
			{
				int zOpposite = states[y].length - 1 - z;

				boolean[] zTmp = states[y][z];
				states[y][z] = states[y][zOpposite];
				states[y][zOpposite] = zTmp;
			}
		}

		return states;
	}
	
	/**
	 * isReachable
	 * 
	 * @param ipAddress
	 * @return boolean daemon is reachable or not
	 * @throws ServiceException 
	 */
	@Override
	public boolean isReachable(final String ipAddress) throws ServiceException
	{
		byte[] response = sendPacket(ipAddress, "status".getBytes(), true);
		boolean isUp = false;
		
		if (response != null)
			isUp = new String(response).trim().equalsIgnoreCase("up");
		
		return isUp;
	}

	/**
	 * createPacket
	 * 
	 * @param duration
	 * @param size
	 * @param states
	 * @return byte[]
	 * @throws ServiceException
     */
	@Override
	public byte[] createPacket(final Long duration, final Size size, final boolean[][][] states) throws ServiceException
	{
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream())
		{
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.writeLong(duration);
			dos.writeInt(size.getX());
			dos.writeInt(size.getY());
			dos.writeInt(size.getZ());
			
			// Write the Cube's state to the buffer
			for (int i = 0; i < size.getY(); i++)
			{
				for (int j = 0; j < size.getZ(); j++)
				{
					for (int k = 0; k < size.getX(); k++)
					{
						dos.writeBoolean(states[i][j][k]);
					}
				}
			}

			dos.flush(); // Flush stream to write its content
			return bos.toByteArray();
		}
		catch (IOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	/**
	 * sendPacket
	 * 
	 * @param ipAddress
	 * @param data
	 * @param response
	 * @return byte[]
	 * @throws ServiceException 
	 */
	@Override
	public byte[] sendPacket(final String ipAddress, final byte[] data, final boolean response) throws ServiceException
	{
		try (DatagramSocket socket = new DatagramSocket())
		{
			InetAddress daemonIp = InetAddress.getByName(ipAddress);
			
			DatagramPacket packet = new DatagramPacket(data, data.length, daemonIp, DAEMON_PORT);
			socket.send(packet);

			try
			{
				// If we're waiting for a response, increase the timeout value from 1 ms to 1 second.
				socket.setSoTimeout(response ? SOCKET_LISTEN_TIMEOUT : SOCKET_TIMEOUT);
				return getResponse(socket);
			}
			catch (SocketTimeoutException e)
			{
				// Do not log the timeout 'cause we don't want to flood the server log file.
			}
			
			return null;
		}
		catch (IOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	/**
	 * getResponse
	 * 
	 * @param socket
	 * @return byte[]
	 * @throws IOException 
	 */
	@Override
	public byte[] getResponse(final DatagramSocket socket) throws IOException
	{
		byte[] buffer = new byte[BUFFER_LENGTH];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		
		return packet.getData();
	}
}
