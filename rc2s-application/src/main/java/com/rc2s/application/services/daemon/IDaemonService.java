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
	public void sendCubesStates(Map<Cube, CubeState> cubesStates) throws ServiceException;
	public void updateState(Cube cube, Long duration, boolean state) throws ServiceException;
	public void updateState(Cube cube, Long duration, boolean[][][] states) throws ServiceException;

	public boolean[][][] generateBooleanArray(Size size, boolean state);
    
	public boolean isReachable(String ipAddress) throws ServiceException;
	
	public byte[] createPacket(Long duration, Size size, boolean[][][] states) throws ServiceException;
    
	public byte[] sendPacket(String ipAddress, byte[] data, boolean response) throws ServiceException;
    
	public byte[] getResponse(DatagramSocket socket) throws IOException;
}
