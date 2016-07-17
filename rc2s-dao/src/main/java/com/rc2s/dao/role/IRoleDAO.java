package com.rc2s.dao.role;

import com.rc2s.common.vo.Group;
import com.rc2s.dao.IGenericDAO;
import javax.ejb.Local;

@Local
public interface IRoleDAO extends IGenericDAO<Group>
{
	
}
