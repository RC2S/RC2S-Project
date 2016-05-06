package com.rc2s.dao.size;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Size;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ISizeDAO
{
	public List<Size> getAll() throws DAOException;
	public Integer add(Size size) throws DAOException;
}
