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
import javax.annotation.security.RolesAllowed;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "TrackEJB")
@Interceptors(SecurityInterceptor.class)
public class TrackFacadeBean implements TrackFacadeRemote
{
    @EJB
    private ITrackService trackService;

    @Override
    @RolesAllowed({"user"})
    public Track add(final User caller, final Track track) throws EJBException
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
    @RolesAllowed({"user"})
    public Track update(final User caller, final Track track) throws EJBException
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
    @RolesAllowed({"user"})
    public void delete(final User caller, final Track track) throws EJBException
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
    @RolesAllowed({"user"})
    public List<Track> getTracksByUser(final User caller) throws EJBException
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
