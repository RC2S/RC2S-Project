package com.rc2s.dao.cube;

import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.Size;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CubeDAO implements ICubeDAO
{
	private final Size cubeSize = new Size(1, "Carré 4x4", 4, 4, 4, new Date(), null);
	private final List<Cube> cubes = Arrays.asList(new Cube[] {
		new Cube(1, "Cube 1", "192.168.100.1", "red", cubeSize, new Date(), null),
		new Cube(1, "Cube 2", "192.168.100.2", "blue", cubeSize, new Date(), null),
		new Cube(1, "Cube 3", "192.168.100.3", "green", cubeSize, new Date(), null),
		new Cube(1, "Cube 4", "192.168.100.4", "green", cubeSize, new Date(), null),
		new Cube(1, "Cube 5", "192.168.100.5", "red", cubeSize, new Date(), null),
	});
	
	@PersistenceContext
    private EntityManager em;
    
	@Override
    public List<Cube> getCubes()
    {
        /*Query query = em.createQuery("SELECT c from Cube as c");
        return query.getResultList();*/
		return cubes;
    }
}
