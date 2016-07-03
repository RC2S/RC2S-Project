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

@Stateless
public class TrackService implements ITrackService
{
    @EJB
    private ITrackDAO trackDAO;

    @Override
    public Track add(Track track) throws ServiceException
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
    public Track update(Track track) throws ServiceException
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
    public void delete(Track track) throws ServiceException
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
    public List<Track> getTracksByUser(User user) throws ServiceException
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
