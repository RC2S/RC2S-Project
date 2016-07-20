package com.rc2s.ejb.group;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Group;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import com.rc2s.application.services.group.IGroupService;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

@Stateless(mappedName = "GroupEJB")
public class GroupFacadeBean implements GroupFacadeRemote
{
	@EJB
	private IGroupService groupService;
    
    @Inject
    private Logger log;

	@Override
    @RolesAllowed({"admin"})
	public List<Group> getAll() throws EJBException
	{
		try
		{
			return groupService.getAll();
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
}
