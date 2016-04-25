package com.rc2s.application.services.cube;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
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
    public List<Cube> getCubes() throws ServiceException
    {
		try
		{
			return cubeDAO.getCubes();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
    }
	
	@Override
	public boolean getStatus(Cube c)
	{
		return false;
	}
}
