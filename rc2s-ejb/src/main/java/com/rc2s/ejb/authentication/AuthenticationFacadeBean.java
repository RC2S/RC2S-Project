package com.rc2s.ejb.authentication;

import com.rc2s.application.services.authentication.IAuthenticationService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.User;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "AuthenticationEJB")
public class AuthenticationFacadeBean implements AuthenticationFacadeRemote
{
    @EJB
    IAuthenticationService authenticationService;
    
    @Override
    @RolesAllowed({"admin"})
	public User login(final String username, final String password) throws EJBException
	{
		try
		{
			return authenticationService.login(username, password);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
