package com.rc2s.application.daemon;

import com.rc2s.common.exceptions.ServiceException;
import java.io.IOException;
import java.net.DatagramSocket;
import javax.ejb.Local;

@Local
public interface IDaemonService
{
	public boolean isReachable(String ipAddress) throws ServiceException;
	public byte[] getResponse(DatagramSocket socket) throws IOException;
}
