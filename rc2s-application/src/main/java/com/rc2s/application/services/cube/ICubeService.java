package com.rc2s.application.services.cube;

import com.rc2s.common.vo.Cube;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ICubeService
{
	public List<Cube> getCubes();
}
