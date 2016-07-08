package com.rc2s.application.services.track;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ITrackService
{
    public Track add(final Track track) throws ServiceException;
    
    public Track update(final Track track) throws ServiceException;
    
    public void delete(final Track track) throws ServiceException;
    
    public List<Track> getTracksByUser(final User user) throws ServiceException;
}
