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

/**
 * CubeService
 * 
 * Service for cube management
 * Acces to database via ICubeDAO
 * 
 * @author RC2S
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CubeService implements ICubeService
{
	@EJB
	private ICubeDAO cubeDAO;
    
    @Inject
    private Logger log;
    
	/**
	 * getCubes()
	 * 
	 * Get all cubes
	 * 
	 * @return List<Cube>
	 * @throws ServiceException 
	 */
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
    
	/**
	 * getCube(User)
	 * 
	 * Get a user's cubes
	 * 
	 * @param user
	 * @return List<Cube>
	 * @throws ServiceException 
	 */
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
	
	/**
	 * add(Cube)
	 * 
	 * Add a new cube to db
	 * 
	 * @param cube
	 * @throws ServiceException 
	 */
	@Override
	public void add(final Cube cube) throws ServiceException
	{
		try
		{
			try
			{
				Cube existing = cubeDAO.getByIp(cube.getIp());

				if (existing != null)
					throw new ServiceException("Cube " + existing.getName() + " already uses the IP address " + existing.getIp());
			}
			catch(DAOException e) { /* Ignore getSingleResult() exception */ }

			cube.setCreated(new Date());
			cubeDAO.save(cube);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	/**
	 * remove(Cube)
	 * 
	 * Remove a cube from db
	 * 
	 * @param cube
	 * @throws ServiceException 
	 */
	@Override
	public void remove(final Cube cube) throws ServiceException
	{
		try
		{
			cubeDAO.delete(cube.getId());
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
	/**
	 * update(Cube)
	 * 
	 * Update a cube from db
	 * 
	 * @param cube
	 * @return Cube (updated)
	 * @throws ServiceException 
	 */
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