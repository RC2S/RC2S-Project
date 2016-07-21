package com.rc2s.ejb.size;

import com.rc2s.application.services.size.ISizeService;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Size;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

/**
 * SizeFacadeBean
 * 
 * Size EJB, bridge to SizeService
 * 
 * @author RC2S
 */
@Stateless(mappedName = "SizeEJB")
public class SizeFacadeBean implements SizeFacadeRemote
{
	@EJB
	private ISizeService sizeService;
    
    @Inject
    private Logger log;

	/**
	 * getAll
	 * 
	 * Gets all sizes in db
	 * 
	 * @return List<Size>
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public List<Size> getAll() throws EJBException
	{
		try
		{
			return sizeService.getAll();
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}

	/**
	 * add
	 * 
	 * Add a given size to db
	 * 
	 * @param size
	 * @return Size added
	 * @throws EJBException 
	 */
	@Override
    @RolesAllowed({"user"})
	public Size add(final Size size) throws EJBException
	{
		try
		{
			return sizeService.add(size);
		}
		catch(ServiceException e)
		{
            log.error(e);
			throw new EJBException(e);
		}
	}
}
