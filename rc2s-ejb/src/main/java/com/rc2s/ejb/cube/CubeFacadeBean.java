package com.rc2s.ejb.cube;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.application.services.daemon.IDaemonService;
import com.rc2s.application.services.cube.ICubeService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "CubeEJB")
@Interceptors(SecurityInterceptor.class)
public class CubeFacadeBean implements CubeFacadeRemote
{
	@EJB private ICubeService cubeService;
	@EJB private IDaemonService daemonService;
    
    @Resource
    private SessionContext context;
	
	@Override
	public List<Cube> getAllCubes(User caller) throws EJBException
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
	public List<Cube> getCubes(User caller) throws EJBException
	{
		try
		{
			return cubeService.getCubes(caller);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
	
	@Override
	public void add(User caller, Cube c) throws EJBException
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
	public void remove(User caller, Cube c) throws EJBException
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
	public Cube update(User caller, Cube cube) throws EJBException
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
	public boolean getStatus(User caller, Cube c) throws EJBException
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
	public void updateAllLed(User caller, Cube c, boolean state) throws EJBException
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
	public void updateAllLed(User caller, Cube c, boolean[][][] states) throws EJBException
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