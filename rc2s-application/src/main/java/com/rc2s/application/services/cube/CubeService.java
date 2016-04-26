package com.rc2s.application.services.cube;

import com.rc2s.common.vo.Cube;
import com.rc2s.dao.cube.ICubeDAO;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CubeService implements ICubeService
{
	@EJB
    private ICubeDAO cubeDAO;
    
    @Override
    public List<Cube> getCubes()
    {
        return cubeDAO.getCubes();
    }
	
	@Override
	public boolean getStatus(Cube c)
	{
		return false;
	}
}
