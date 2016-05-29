package com.rc2s.ejb.role;

import com.rc2s.application.services.role.IRoleService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Role;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "RoleEJB")
public class RoleFacadeBean implements RoleFacadeRemote
{
	@EJB
	private IRoleService roleService;

	@Override
	public List<Role> getAll() throws EJBException
	{
		try
		{
			return roleService.getAll();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
