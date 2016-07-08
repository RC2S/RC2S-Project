package com.rc2s.application.services.daemon;

import com.rc2s.common.bo.CubeState;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.Size;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Map;
import javax.ejb.Local;

@Local
public interface IDaemonService
{
    public void sendCubesStates(final Map<Cube, CubeState> cubesStates) throws ServiceException;
    
	public void updateState(final Cube cube, final Long duration, final boolean state) throws ServiceException;
    
	public void updateState(final Cube cube, final Long duration, final boolean[][][] states) throws ServiceException;

	public boolean[][][] generateBooleanArray(final Size size, final boolean state);
    
	public boolean[][][] formatStatesArray(final boolean[][][] states);
    
	public boolean isReachable(final String ipAddress) throws ServiceException;
	
	public byte[] createPacket(final Long duration, final Size size, final boolean[][][] states) throws ServiceException;
    
	public byte[] sendPacket(final String ipAddress, final byte[] data, final boolean response) throws ServiceException;
    
	public byte[] getResponse(final DatagramSocket socket) throws IOException;
}
