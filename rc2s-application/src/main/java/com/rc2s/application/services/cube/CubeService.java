package com.rc2s.application.services.cube;

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
	@EJB
    private ICubeDAO cubeDAO;
    
    @Override
    public List<Cube> getCubes() throws ServiceException
    {
		try
		{
			return cubeDAO.getCubes();
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
			cubeDAO.add(c);
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
			cubeDAO.remove(c);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public boolean getStatus(Cube c)
	{
		return false;
	}
}
