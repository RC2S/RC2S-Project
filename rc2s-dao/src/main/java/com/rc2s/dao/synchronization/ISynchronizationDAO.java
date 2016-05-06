package com.rc2s.dao.synchronization;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Synchronization;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ISynchronizationDAO
{
	public List<Synchronization> getAll() throws DAOException;
	public void add(Synchronization synchronization) throws DAOException;
}
