package com.rc2s.ejb.daemon;

import com.rc2s.application.services.daemon.IDaemonService;
import com.rc2s.common.bo.CubeState;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.Size;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "DaemonEJB")
public class DaemonFacadeBean implements DaemonFacadeRemote
{
    @EJB
    private IDaemonService daemonService;
    
    @Override
    public void sendCubesStates(Map<Cube, CubeState> cubesStates) throws ServiceException
    {
        daemonService.sendCubesStates(cubesStates);
    }

    @Override
    public void updateState(Cube cube, Long duration, boolean state) throws ServiceException
    {
        daemonService.updateState(cube, duration, state);
    }

    @Override
    public void updateState(Cube cube, Long duration, boolean[][][] states) throws ServiceException
    {
        daemonService.updateState(cube, duration, states);
    }

    @Override
    public boolean[][][] generateBooleanArray(Size size, boolean state)
    {
        return daemonService.generateBooleanArray(size, state);
    }

    @Override
    public boolean[][][] formatStatesArray(boolean[][][] states)
    {
        return daemonService.formatStatesArray(states);
    }

    @Override
    public boolean isReachable(String ipAddress) throws ServiceException
    {
        return daemonService.isReachable(ipAddress);
    }

    @Override
    public byte[] createPacket(Long duration, Size size, boolean[][][] states) throws ServiceException
    {
        return daemonService.createPacket(duration, size, states);
    }

    @Override
    public byte[] sendPacket(String ipAddress, byte[] data, boolean response) throws ServiceException
    {
        return daemonService.sendPacket(ipAddress, data, response);
    }

    @Override
    public byte[] getResponse(DatagramSocket socket) throws IOException
    {
        return daemonService.getResponse(socket);
    }
}
