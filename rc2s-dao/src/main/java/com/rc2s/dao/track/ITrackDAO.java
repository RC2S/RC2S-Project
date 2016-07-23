package com.rc2s.dao.track;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;
import com.rc2s.dao.IGenericDAO;

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
public interface ITrackDAO extends IGenericDAO<Track>
{
    public List<Track> getTracksByUser(final User user) throws DAOException;
}
