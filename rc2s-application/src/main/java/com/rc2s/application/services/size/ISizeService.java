package com.rc2s.application.services.size;

import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.vo.Size;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ISizeService
{
	public List<Size> getAll() throws ServiceException;
	public Integer add(Size size) throws ServiceException;
}
