package com.rc2s.streamingplugin.dao.track;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.streamingplugin.common.vo.Track;
import com.rc2s.common.vo.User;
import com.rc2s.streamingplugin.dao.generic.IGenericDAO;
import com.rc2s.annotations.SourceControl;

import javax.ejb.Local;
import java.util.List;

/**
 * ITrackDAO
 * 
 * Interface for tracks management
 * 
 * @author RC2S
 */
@Local
//@SourceControl
public interface ITrackDAO extends IGenericDAO<Track>
{
    public List<Track> getTracksByUser(final User user) throws DAOException;
}
