package com.rc2s.application.services.group;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Group;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import com.rc2s.dao.group.IGroupDAO;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GroupService implements IGroupService
{
	@EJB
	private IGroupDAO roleDAO;
	
	@Override
	public List<Group> getAll() throws ServiceException
	{
		try
		{
			return roleDAO.getAll();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
	
}
