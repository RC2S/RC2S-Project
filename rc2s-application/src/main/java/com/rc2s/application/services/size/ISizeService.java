package com.rc2s.application.services.size;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Size;
import java.util.List;
import javax.ejb.Local;

/**
 * ISizeService interface
 * 
 * Service interface for size manipulation via ISizeDAO
 * 
 * @author RC2S
 */
@Local
public interface ISizeService
{
	public List<Size> getAll() throws ServiceException;
    
	public Size add(final Size size) throws ServiceException;
}
