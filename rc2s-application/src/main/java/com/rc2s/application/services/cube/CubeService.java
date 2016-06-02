package com.rc2s.application.services.cube;

import com.rc2s.application.daemon.IDaemonService;
import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import com.rc2s.dao.cube.ICubeDAO;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CubeService implements ICubeService
{
	@EJB private ICubeDAO cubeDAO;
	@EJB private IDaemonService daemonService;
    
    @Override
    public List<Cube> getCubes() throws ServiceException
    {
		try
		{
			return cubeDAO.getAll();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
    }
    
    @Override
    public List<Cube> getCubes(User user) throws ServiceException
    {
		try
		{
			return cubeDAO.getCubes(user);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
    }
	
	@Override
	public void add(Cube c) throws ServiceException
	{
		try
		{
			c.setCreated(new Date());
			cubeDAO.save(c);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void remove(Cube c) throws ServiceException
	{
		try
		{
			cubeDAO.delete(c.getId());
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Cube update(Cube cube) throws ServiceException
	{
		try
		{
			return cubeDAO.update(cube);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public boolean getStatus(Cube c) throws ServiceException
	{
		return daemonService.isReachable(c.getIp());
	}
}