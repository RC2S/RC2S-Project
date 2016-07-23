package com.rc2s.streamingplugin.application.track;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.streamingplugin.common.vo.Track;
import com.rc2s.common.vo.User;
import com.rc2s.streamingplugin.dao.track.ITrackDAO;
import com.rc2s.annotations.SourceControl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TrackService
 * 
 * Service for tracks management
 * Works with the ITrackDAO
 * 
 * @author RC2S
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
//@SourceControl
public class TrackService implements ITrackService
{
    @EJB
    private ITrackDAO trackDAO;
    
    private Logger log = LogManager.getLogger(TrackService.class);

	/**
	 * add
	 * 
	 * Adds a new given track in db
	 * 
	 * @param track
	 * @return Track added
	 * @throws ServiceException 
	 */
    @Override
    public Track add(final Track track) throws ServiceException
    {
        try
        {
            track.setCreated(new Date());
            return trackDAO.save(track);
        }
        catch (DAOException e)
        {
            throw new ServiceException(e);
        }
    }

	/**
	 * update
	 * 
	 * Updates a given track in db
	 * 
	 * @param track
	 * @return Track updated
	 * @throws ServiceException 
	 */
    @Override
    public Track update(final Track track) throws ServiceException
    {
        try
        {
            return trackDAO.update(track);
        }
        catch (DAOException e)
        {
            throw new ServiceException(e);
        }
    }

	/**
	 * delete
	 * 
	 * Delete a given track in db
	 * 
	 * @param track
	 * @throws ServiceException 
	 */
    @Override
    public void delete(final Track track) throws ServiceException
    {
        try
        {
            trackDAO.delete(track.getId());
        }
		catch (DAOException e)
		{
			throw new ServiceException(e);
		}
    }

	/**
	 * getTracksByUser
	 * 
	 * Get all the tracks for the specified user
	 * 
	 * @param user
	 * @return List<Track>
	 * @throws ServiceException 
	 */
    @Override
    public List<Track> getTracksByUser(final User user) throws ServiceException
    {
        try
        {
            return trackDAO.getTracksByUser(user);
        }
        catch (DAOException e)
        {
            throw new ServiceException(e);
        }
    }
}
