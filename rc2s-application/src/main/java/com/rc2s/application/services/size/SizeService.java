package com.rc2s.application.services.size;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Size;
import com.rc2s.dao.size.ISizeDAO;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class SizeService implements ISizeService
{
	@EJB
	private ISizeDAO sizeDAO;
	
	@Override
	public List<Size> getAll() throws ServiceException
	{
		try
		{
			return sizeDAO.getAll();
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer add(Size size) throws ServiceException
	{
		try
		{
			size.setCreated(new Date());
			return sizeDAO.add(size);
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
}
