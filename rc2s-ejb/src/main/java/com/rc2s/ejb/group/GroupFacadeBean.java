package com.rc2s.ejb.group;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Group;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import com.rc2s.application.services.group.IGroupService;
import javax.annotation.security.RolesAllowed;

@Stateless(mappedName = "GroupEJB")
@Interceptors(SecurityInterceptor.class)
public class GroupFacadeBean implements GroupFacadeRemote
{
	@EJB
	private IGroupService groupService;

	@Override
    @RolesAllowed({"admin"})
	public List<Group> getAll(final User caller) throws EJBException
	{
		try
		{
			return groupService.getAll();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
