package com.rc2s.ejb.track;

import com.rc2s.application.services.track.ITrackService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * TrackFacadeBean
 * 
 * Track EJB, bridge to TrackService
 * 
 * @author RC2S
 */
@Stateless(mappedName = "TrackEJB")
public class TrackFacadeBean implements TrackFacadeRemote
{
    @EJB
    private ITrackService trackService;
    
    @Inject
    private Logger log;

	/**
	 * add
	 * 
	 * Add a given track to db
	 * 
	 * @param track
	 * @return Track added
	 * @throws EJBException 
	 */
    @Override
    @RolesAllowed({"user"})
    public Track add(final Track track) throws EJBException
    {
        try
        {
            return trackService.add(track);
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
	 * Updates the given track in db
	 * 
	 * @param track
	 * @return Track updated
	 * @throws EJBException 
	 */
    @Override
    @RolesAllowed({"user"})
    public Track update(final Track track) throws EJBException
	{
        try
		{
			return trackService.update(track);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
    }

	/**
	 * delete
	 * 
	 * Deletes the given track in db
	 * 
	 * @param track
	 * @throws EJBException 
	 */
    @Override
    @RolesAllowed({"user"})
    public void delete(final Track track) throws EJBException
	{
		try
		{
			trackService.delete(track);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
    }

	/**
	 * getTracksByUser
	 * 
	 * Gets all the tracks in db for the given user
	 * 
	 * @param user
	 * @return List<Track> the user's tracks
	 * @throws EJBException 
	 */
    @Override
    @RolesAllowed({"user"})
    public List<Track> getTracksByUser(final User user) throws EJBException
    {
        try
        {
            return trackService.getTracksByUser(user);
        }
        catch(ServiceException e)
        {
            log.error(e);
            throw new EJBException(e);
        }
    }
}
