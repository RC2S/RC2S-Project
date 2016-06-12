package com.rc2s.ejb.track;

import com.rc2s.application.services.track.ITrackService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless(mappedName = "TrackEJB")
public class TrackFacadeBean implements TrackFacadeRemote
{
    @EJB
    private ITrackService trackService;

    @Override
    public List<Track> getTracksByUser(User user) throws EJBException
    {
        try
        {
            return trackService.getTracksByUser(user);
        }
        catch(ServiceException e)
        {
            throw new EJBException(e);
        }
    }
}
