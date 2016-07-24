package com.rc2s.streamingplugin.ejb.track;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.streamingplugin.common.vo.Track;
import com.rc2s.common.vo.User;
import com.rc2s.annotations.SourceControl;

import javax.ejb.Remote;
import java.util.List;

/**
 * TrackFacadeRemote
 * 
 * EJB remote interface for Track EJB
 * 
 * @author RC2S
 */
@Remote
@SourceControl
public interface TrackFacadeRemote
{
    public Track add(final Track track) throws EJBException;
    
    public Track update(final Track track) throws EJBException;
    
    public void delete(final Track track) throws EJBException;

    public List<Track> getTracksByUser(final User user) throws EJBException;
}
