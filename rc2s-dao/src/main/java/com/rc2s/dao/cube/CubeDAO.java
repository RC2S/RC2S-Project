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
	public List<Cube> getCubes(final User user) throws DAOException
	{
		try
		{
			Query query = em().createQuery("SELECT c FROM Cube AS c JOIN c.synchronization AS s JOIN s.users AS u ON u.id = :userId")
							  .setParameter("userId", user.getId());
			List<Cube> cubes = query.getResultList();
			
			// Lazy loading
			for(Cube c : cubes)
			{
				c.getSynchronization() // Get the Cube's Synchronization
				 .getCubes().size(); // Get the Synchroniszation's Cubes
			}
			
			return cubes; 
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public Cube getByIp(final String ipAddress) throws DAOException
	{
		try
		{
			Query query = em().createQuery("SELECT c FROM Cube AS c WHERE c.ip = :ip")
					 		  .setParameter("ip", ipAddress);
			return (Cube)query.getSingleResult();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
}
