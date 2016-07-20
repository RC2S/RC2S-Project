package com.rc2s.application.services.track;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;
import com.rc2s.dao.track.ITrackDAO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrackService implements ITrackService
{
    @EJB
    private ITrackDAO trackDAO;
    
    @Inject
    private Logger log;

    @Override
    public Track add(final Track track) throws ServiceException
    {
        try
        {
            track.setCreated(new Date());
            return trackDAO.save(track);
        }
        catch(DAOException e)
        {
            throw new ServiceException(e);
        }
    }

    @Override
    public Track update(final Track track) throws ServiceException
    {
        try
        {
            return trackDAO.update(track);
        }
        catch(DAOException e)
        {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(final Track track) throws ServiceException
    {
        try
        {
            trackDAO.delete(track.getId());
        }
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
    }

    @Override
    public List<Track> getTracksByUser(final User user) throws ServiceException
    {
        try
        {
            return trackDAO.getTracksByUser(user);
        }
        catch(DAOException e)
        {
            throw new ServiceException(e);
        }
    }
}
