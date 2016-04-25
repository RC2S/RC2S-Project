package com.rc2s.dao.cube;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Cube;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ICubeDAO
{
	public List<Cube> getCubes() throws DAOException;
}
