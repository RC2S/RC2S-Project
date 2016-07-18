package com.rc2s.ejb.size;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.application.services.size.ISizeService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Size;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "SizeEJB")
@Interceptors(SecurityInterceptor.class)
public class SizeFacadeBean implements SizeFacadeRemote
{
	@EJB
	private ISizeService sizeService;

	@Override
    @RolesAllowed({"user"})
	public List<Size> getAll(final User caller) throws EJBException
	{
		try
		{
			return sizeService.getAll();
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}

	@Override
    @RolesAllowed({"user"})
	public Size add(final User caller, final Size size) throws EJBException
	{
		try
		{
			return sizeService.add(size);
		}
		catch(ServiceException e)
		{
			throw new EJBException(e);
		}
	}
}
