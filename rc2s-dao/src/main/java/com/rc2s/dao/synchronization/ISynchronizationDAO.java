package com.rc2s.dao.synchronization;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import com.rc2s.dao.IGenericDAO;
import javax.ejb.Local;
import java.util.List;

@Local
public interface ISynchronizationDAO extends IGenericDAO<Synchronization>
{
	public List<Synchronization> getByUser(User user) throws DAOException;
}
