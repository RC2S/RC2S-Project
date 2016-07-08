package com.rc2s.ejb.cube;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface CubeFacadeRemote
{
	public List<Cube> getAllCubes(final User caller) throws EJBException;
    
	public List<Cube> getCubes(final User caller) throws EJBException;
    
	public void add(final User caller, final Cube c) throws EJBException;
    
	public void remove(final User caller, final Cube c) throws EJBException;
    
	public Cube update(final User caller, final Cube cube) throws EJBException;
	
	public boolean getStatus(final User caller, final Cube c) throws EJBException;
    
	public void updateAllLed(final User caller, final Cube c, final boolean state) throws EJBException;
    
	public void updateAllLed(final User caller, final Cube c, final boolean[][][] states) throws EJBException;
}
