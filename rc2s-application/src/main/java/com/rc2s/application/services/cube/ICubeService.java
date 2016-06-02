package com.rc2s.application.services.cube;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ICubeService
{
	public List<Cube> getCubes() throws ServiceException;
	public List<Cube> getCubes(User user) throws ServiceException;
	public void add(Cube c) throws ServiceException;
	public void remove(Cube c) throws ServiceException;
	public Cube update(Cube cube) throws ServiceException;
	
	public boolean getStatus(Cube c) throws ServiceException;
}