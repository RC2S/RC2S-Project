package com.rc2s.dao.cube;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Cube;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ICubeDAO
{
	public List<Cube> getCubes() throws DAOException;
	public List<Cube> getCubes(User user) throws DAOException;
	public void add(Cube cube) throws DAOException;
	public void remove(Cube cube) throws DAOException;
}
