package com.rc2s.ejb.track;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface TrackFacadeRemote
{
    public Track add(User caller, Track track) throws EJBException;
    
    public Track update(User caller, Track track) throws EJBException;
    
    public void delete(User caller, Track track) throws EJBException;

    public List<Track> getTracksByUser(User caller) throws EJBException;
}
