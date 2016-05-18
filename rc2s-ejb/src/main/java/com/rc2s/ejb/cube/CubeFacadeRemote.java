package com.rc2s.ejb.cube;

import com.rc2s.common.vo.Cube;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface CubeFacadeRemote
{
	public List<Cube> getAllCubes();
	public boolean getStatus(Cube c);
}
