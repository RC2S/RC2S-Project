package com.rc2s.ejb.track;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface TrackFacadeRemote
{
    public List<Track> getTracksByUser(User user) throws EJBException;
}
