package com.rc2s.ejb.cube;

import com.rc2s.application.services.daemon.IDaemonService;
import com.rc2s.application.services.cube.ICubeService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "CubeEJB")
public class CubeFacadeBean implements CubeFacadeRemote
{
	@EJB private ICubeService cubeService;
	@EJB private IDaemonService daemonService;
	
	@Override
	public List<Cube> getAllCubes() throws EJBException
	{
		try
		{
			return cubeService.getCubes();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
	
	@Override
	public List<Cube> getCubes(User user) throws EJBException
	{
		try
		{
			return cubeService.getCubes(user);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
	
	@Override
	public void add(Cube c) throws EJBException
	{
		try
		{
			cubeService.add(c);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
	
	@Override
	public void remove(Cube c) throws EJBException
	{
		try
		{
			cubeService.remove(c);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
	
	@Override
	public Cube update(Cube cube) throws EJBException
	{
		try
		{
			return cubeService.update(cube);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
	
	@Override
	public boolean getStatus(Cube c) throws EJBException
	{
		try
		{
			return daemonService.isReachable(c.getIp());
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public void updateAllLed(Cube c, boolean state) throws EJBException
	{
		try
		{
			daemonService.updateState(c, 0L, state);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
	public void updateAllLed(Cube c, boolean[][][] states) throws EJBException
	{
		try
		{
			daemonService.updateState(c, 0L, states);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}