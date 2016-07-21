package com.rc2s.ejb.synchronization;

import com.rc2s.application.services.synchronization.ISynchronizationService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * SynchronizationFacadeBean
 * 
 * Synchronization EJB, bridge to SynchronizationService
 * 
 * @author RC2S
 */
@Stateless(mappedName = "SynchronizationEJB")
public class SynchronizationFacadeBean implements SynchronizationFacadeRemote
{
	@EJB
	private ISynchronizationService synchronizationService;
    
    @Inject
    private Logger log;

	/**
	 * getAll
	 * 
	 * Get all the synchronizations in db
	 * 
	 * @return List<Synchronization>
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public List<Synchronization> getAll() throws EJBException
	{
		try
		{
			return synchronizationService.getAll();
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}

	/**
	 * getByUser
	 * 
	 * Get all the synchronization in db for a given user
	 * 
	 * @param user
	 * @return List<Synchronization>
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public List<Synchronization> getByUser(final User user) throws EJBException
	{
		try
		{
			return synchronizationService.getByUser(user);
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
	 * Add the given synchronization to db
	 * 
	 * @param synchronization
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public void add(final Synchronization synchronization) throws EJBException
	{
		try
		{
			synchronizationService.add(synchronization);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
}
