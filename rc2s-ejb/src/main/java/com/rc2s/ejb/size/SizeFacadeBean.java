package com.rc2s.ejb.size;

import com.rc2s.application.services.size.ISizeService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Size;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "SizeEJB")
public class SizeFacadeBean implements SizeFacadeRemote
{
	@EJB
	private ISizeService sizeService;

	@Override
	public List<Size> getAll() throws EJBException
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
	public Size add(Size size) throws EJBException
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
