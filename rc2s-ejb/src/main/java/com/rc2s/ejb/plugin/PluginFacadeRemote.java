package com.rc2s.ejb.plugin;

import com.rc2s.common.exceptions.EJBException;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface PluginFacadeRemote
{
	public List<String> getNames() throws EJBException;
}
