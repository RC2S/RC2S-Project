package com.rc2s.ejb.role;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.application.services.role.IRoleService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Role;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "RoleEJB")
@Interceptors(SecurityInterceptor.class)
public class RoleFacadeBean implements RoleFacadeRemote
{
	@EJB
	private IRoleService roleService;

	@Override
	public List<Role> getAll(final User caller) throws EJBException
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
