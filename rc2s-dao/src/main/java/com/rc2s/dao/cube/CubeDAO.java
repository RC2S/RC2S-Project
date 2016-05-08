package com.rc2s.dao.cube;

import com.rc2s.common.vo.Cube;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CubeDAO implements ICubeDAO
{
	@PersistenceContext(unitName="rc2s")
    private EntityManager em;
    
	@Override
    public List<Cube> getCubes()
    {
        Query query = em.createQuery("SELECT c from Cube as c");
        return query.getResultList();
    }
}
