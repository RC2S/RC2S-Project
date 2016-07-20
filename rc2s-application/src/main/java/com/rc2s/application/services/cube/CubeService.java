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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CubeService implements ICubeService
{
	@EJB
	private ICubeDAO cubeDAO;
    
    @Inject
    private Logger log;
    
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
    public List<Cube> getCubes(final User user) throws ServiceException
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
	public void add(final Cube c) throws ServiceException
	{
		try
		{
			try
			{
				Cube existing = cubeDAO.getByIp(c.getIp());

				if (existing != null)
					throw new ServiceException("Cube " + existing.getName() + " already uses the IP address " + existing.getIp());
			}
			catch(DAOException e) { /* Ignore getSingleResult() exception */ }

			c.setCreated(new Date());
			cubeDAO.save(c);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void remove(final Cube c) throws ServiceException
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
	public Cube update(final Cube cube) throws ServiceException
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
}