package com.rc2s.dao.track;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Track;
import com.rc2s.common.vo.User;
import com.rc2s.dao.GenericDAO;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class TrackDAO extends GenericDAO<Track> implements ITrackDAO
{
    @Override
    public List<Track> getTracksByUser(User user) throws DAOException
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
