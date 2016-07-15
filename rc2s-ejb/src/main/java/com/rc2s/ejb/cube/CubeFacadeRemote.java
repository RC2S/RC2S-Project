package com.rc2s.ejb.cube;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface CubeFacadeRemote
{
	public List<Cube> getAllCubes(User caller) throws EJBException;
    
	public List<Cube> getCubes(User caller) throws EJBException;
    
	public void add(User caller, Cube c) throws EJBException;
    
	public void remove(User caller, Cube c) throws EJBException;
    
	public Cube update(User caller, Cube cube) throws EJBException;
	
	public boolean getStatus(User caller, Cube c) throws EJBException;
    
	public void updateAllLed(User caller, Cube c, boolean state) throws EJBException;
    
	public void updateAllLed(User caller, Cube c, boolean[][][] states) throws EJBException;
}
