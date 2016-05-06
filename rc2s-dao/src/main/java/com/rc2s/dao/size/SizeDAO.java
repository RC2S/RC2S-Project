package com.rc2s.dao.size;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Size;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SizeDAO implements ISizeDAO
{
	@PersistenceContext
    private EntityManager em;
	
	@Override
	public List<Size> getAll() throws DAOException
	{
		try
		{
			Query query = em.createQuery("SELECT s from Size as s");
			return query.getResultList();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}

	@Override
	public Integer add(Size size) throws DAOException
	{
		try
		{
			em.persist(size);
			em.flush();
			return size.getId();
		}
		catch(Exception e)
		{
			throw new DAOException(e);
		}
	}
}
