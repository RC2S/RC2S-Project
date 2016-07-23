package com.rc2s.application.services.size;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Size;
import com.rc2s.dao.size.ISizeDAO;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * SizeService
 * 
 * Service for size management
 * Acces to database via ISizeDAO
 * 
 * @author RC2S
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SizeService implements ISizeService
{
	@EJB
	private ISizeDAO sizeDAO;
    
    @Inject
    private Logger log;
	
	/**
	 * getAll
	 * 
	 * Get all sizes in db
	 * 
	 * @return List<Size>
	 * @throws ServiceException 
	 */
	@Override
	public List<Size> getAll() throws ServiceException
	{
		try
		{
			return sizeDAO.getAll();
		}
		catch (DAOException e)
		{
			throw new ServiceException(e);
		}
	}

	/**
	 * add
	 * 
	 * Adds a new specified cube size in db
	 * 
	 * @param size
	 * @return Size
	 * @throws ServiceException 
	 */
	@Override
	public Size add(final Size size) throws ServiceException
	{
		try
		{
			size.setCreated(new Date());
			return sizeDAO.save(size);
		}
		catch (DAOException e)
		{
			throw new ServiceException(e);
		}
	}
}
