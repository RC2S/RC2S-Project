package com.rc2s.application.services.track;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ITrackService
{
    public List<Track> getTracksByUser(User user) throws ServiceException;
}
