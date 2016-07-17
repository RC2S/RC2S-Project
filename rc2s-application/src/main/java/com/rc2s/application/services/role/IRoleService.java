package com.rc2s.application.services.role;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Group;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IRoleService
{
	public List<Group> getAll() throws ServiceException;
}
