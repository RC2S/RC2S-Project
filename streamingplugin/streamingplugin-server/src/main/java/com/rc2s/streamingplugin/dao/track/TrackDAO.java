package com.rc2s.streamingplugin.dao.track;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.streamingplugin.common.vo.Track;
import com.rc2s.common.vo.User;
import com.rc2s.streamingplugin.dao.generic.GenericDAO;
import com.rc2s.annotations.SourceControl;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * TrackDAO
 * 
 * ITrackDAO implementation, bridge for database tracks management
 * 
 * @author RC2S
 */
@Stateless
@SourceControl
public class TrackDAO extends GenericDAO<Track> implements ITrackDAO
{
	/**
	 * getTracksByUser
	 * 
	 * Get all the tracks for the given user
	 * 
	 * @param user
	 * @return List<Track> the user's tracks
	 * @throws DAOException 
	 */
    @Override
    public List<Track> getTracksByUser(final User user) throws DAOException
    {
        try
        {
            Query query = em().createQuery("SELECT t FROM Track AS t WHERE t.user = :user")
                    .setParameter("user", user);

            return query.getResultList();
        }
        catch(Exception e)
        {
            throw new DAOException(e);
        }
    }
}
