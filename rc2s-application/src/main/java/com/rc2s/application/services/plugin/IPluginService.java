package com.rc2s.application.services.plugin;

import com.rc2s.common.exceptions.ServiceException;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IPluginService
{
	public List<String> getNames() throws ServiceException;
}
