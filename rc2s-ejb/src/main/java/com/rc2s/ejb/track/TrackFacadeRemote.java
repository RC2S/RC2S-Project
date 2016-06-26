package com.rc2s.ejb.track;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface TrackFacadeRemote
{
    public Track add(Track track) throws EJBException;
    public Track update(Track track) throws EJBException;
    public void delete(Track track) throws EJBException;

    public List<Track> getTracksByUser(User user) throws EJBException;
}
