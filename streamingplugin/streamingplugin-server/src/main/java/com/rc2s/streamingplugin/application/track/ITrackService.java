package com.rc2s.streamingplugin.application.track;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.streamingplugin.common.vo.Track;
import com.rc2s.common.vo.User;
import com.rc2s.annotations.SourceControl;

import javax.ejb.Local;
import java.util.List;

/**
 * ITrackService interface
 * 
 * Service interface for tracks management
 * 
 * @author RC2S
 */
@Local
@SourceControl
public interface ITrackService
{
    public Track add(final Track track) throws ServiceException;
    
    public Track update(final Track track) throws ServiceException;
    
    public void delete(final Track track) throws ServiceException;
    
    public List<Track> getTracksByUser(final User user) throws ServiceException;
}
