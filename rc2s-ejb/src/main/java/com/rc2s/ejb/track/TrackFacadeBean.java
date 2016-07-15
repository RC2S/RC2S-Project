package com.rc2s.ejb.track;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.application.services.track.ITrackService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "TrackEJB")
@Interceptors(SecurityInterceptor.class)
public class TrackFacadeBean implements TrackFacadeRemote
{
    @EJB
    private ITrackService trackService;

    @Override
    public Track add(User caller, Track track) throws EJBException
    {
        try
        {
            return trackService.add(track);
        }
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
    }

    @Override
    public Track update(User caller, Track track) throws EJBException
	{
        try
		{
			return trackService.update(track);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
    }

    @Override
    public void delete(User caller, Track track) throws EJBException
	{
		try
		{
			trackService.delete(track);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
    }

    @Override
    public List<Track> getTracksByUser(User caller) throws EJBException
    {
        try
        {
            return trackService.getTracksByUser(caller);
        }
        catch(ServiceException e)
        {
            throw new EJBException(e);
        }
    }
}
