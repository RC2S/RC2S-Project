package com.rc2s.dao.synchronization;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import com.rc2s.dao.GenericDAO;
import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class SynchronizationDAO extends GenericDAO<Synchronization> implements ISynchronizationDAO
{
	@Override
	public List<Synchronization> getByUser(User user) throws DAOException
	{
		try
		{
			Query query = em().createQuery("SELECT s FROM Synchronization AS s JOIN s.users AS u ON u.id = :userId")
							  .setParameter("userId", user.getId());

			List<Synchronization> list = query.getResultList();

			// Lazy loading
			for(Synchronization sync : list)
			{
				sync.getCubes().size(); // Load the Synchronization's Cubes
			}

			return list;
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
}
