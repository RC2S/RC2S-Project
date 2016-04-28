package com.rc2s.ejb.cube;

import com.rc2s.application.services.cube.ICubeService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "CubeEJB")
public class CubeFacadeBean implements CubeFacadeRemote
{
	@EJB
	private ICubeService cubeService;
	
	@Override
	public List<Cube> getAllCubes() throws EJBException
	{
		try
		{
			return cubeService.getCubes();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
	
	@Override
	public boolean getStatus(Cube c) throws EJBException
	{
		return cubeService.getStatus(c);
	}
}
