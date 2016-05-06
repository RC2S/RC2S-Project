package com.rc2s.ejb.cube;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface CubeFacadeRemote
{
	public List<Cube> getAllCubes() throws EJBException;
	public List<Cube> getCubes(User user) throws EJBException;
	public void add(Cube c) throws EJBException;
	public void remove(Cube c) throws EJBException;
	
	public boolean getStatus(Cube c) throws EJBException;
}
