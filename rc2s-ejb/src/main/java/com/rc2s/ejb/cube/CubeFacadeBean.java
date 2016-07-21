package com.rc2s.ejb.cube;

import com.rc2s.application.services.daemon.IDaemonService;
import com.rc2s.application.services.cube.ICubeService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * CubeFacadeBean
 * 
 * Cube EJB, bridge to CubeService
 * Uses ICubeService & IDaemonService
 * 
 * @author RC2S
 */
@Stateless(mappedName = "CubeEJB")
public class CubeFacadeBean implements CubeFacadeRemote
{
	@EJB
    private ICubeService cubeService;
    
	@EJB 
    private IDaemonService daemonService;
    
    @Inject
    private Logger log;
	
	/**
	 * getAllCubes
	 * 
	 * Get all cubes in db
	 * 
	 * @return List<Cube>
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public List<Cube> getAllCubes() throws EJBException
	{ 
		try
		{
			return cubeService.getCubes();
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
	
	/**
	 * getCubes
	 * 
	 * Get a given user's cubes
	 * 
	 * @param user
	 * @return List<Cube>
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public List<Cube> getCubes(final User user) throws EJBException
	{
		try
		{
			return cubeService.getCubes(user);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
	
	/**
	 * add
	 * 
	 * Adds a new cube to db
	 * 
	 * @param cube
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public void add(final Cube cube) throws EJBException
	{
		try
		{
			cubeService.add(cube);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
	
	/**
	 * remove
	 * 
	 * Remove a given cube from db
	 * 
	 * @param cube
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public void remove(final Cube cube) throws EJBException
	{
		try
		{
			cubeService.remove(cube);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
	
	/**
	 * update
	 * 
	 * Updates a given cube from db
	 * 
	 * @param cube
	 * @return
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public Cube update(final Cube cube) throws EJBException
	{
		try
		{
			return cubeService.update(cube);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
	
	/**
	 * getStatus
	 * 
	 * Gets a given cube's status
	 * 
	 * @param cube
	 * @return boolean status
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public boolean getStatus(final Cube cube) throws EJBException
	{
		try
		{
			return daemonService.isReachable(cube.getIp());
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}

	/**
	 * updateAllLed
	 * 
	 * Update all the leds of a given cube 
	 * to a given state
	 * 
	 * @param cube
	 * @param state
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public void updateAllLed(final Cube cube, final boolean state) throws EJBException
	{
		try
		{
			daemonService.updateState(cube, 0L, state);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}

	/**
	 * updateAllLed
	 * 
	 * Update all the leds of a given cube
	 * to a given 3D global state
	 * 
	 * @param cube
	 * @param states
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public void updateAllLed(final Cube cube, final boolean[][][] states) throws EJBException
	{
		try
		{
			daemonService.updateState(cube, 0L, states);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
}