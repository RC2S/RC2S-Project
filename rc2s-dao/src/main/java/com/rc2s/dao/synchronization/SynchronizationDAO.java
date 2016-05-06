package com.rc2s.dao.synchronization;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Synchronization;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SynchronizationDAO implements ISynchronizationDAO
{
	@PersistenceContext
    private EntityManager em;

	@Override
	public List<Synchronization> getAll() throws DAOException
	{
		try
		{
			Query query = em.createQuery("SELECT s from Synchronization as s");
			return query.getResultList();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public void add(Synchronization synchronization) throws DAOException
	{
		try
		{
			em.persist(synchronization);
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}	
}
