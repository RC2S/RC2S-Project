package com.rc2s.dao.cube;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CubeDAO implements ICubeDAO
{
	@PersistenceContext
    private EntityManager em;
    
	@Override
    public List<Cube> getCubes() throws DAOException
    {
		try
		{
			Query query = em.createQuery("SELECT c FROM Cube AS c");
			return query.getResultList();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
    }
	
	@Override
	public List<Cube> getCubes(User user) throws DAOException
	{
		try
		{
			Query query = em.createQuery("SELECT c FROM Cube AS c JOIN c.synchronization AS s JOIN s.users AS u ON u.id = :userId")
							.setParameter("userId", user.getId());
			return query.getResultList();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public void add(Cube cube) throws DAOException
	{
		try
		{
			em.persist(cube);
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
	
	@Override
	public void remove(Cube cube) throws DAOException
	{
		try
		{
			Cube tmp = em.find(Cube.class, cube.getId());
			em.remove(tmp);
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
}
