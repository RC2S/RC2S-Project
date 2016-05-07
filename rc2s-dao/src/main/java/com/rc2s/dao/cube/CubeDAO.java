package com.rc2s.dao.cube;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import com.rc2s.dao.GenericDAO;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class CubeDAO extends GenericDAO<Cube> implements ICubeDAO
{
	@Override
	public List<Cube> getCubes(User user) throws DAOException
	{
		try
		{
			Query query = em().createQuery("SELECT c FROM Cube AS c JOIN c.synchronization AS s JOIN s.users AS u ON u.id = :userId")
							  .setParameter("userId", user.getId());
			return query.getResultList();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
}
