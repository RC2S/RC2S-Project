package com.rc2s.dao.plugin;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.vo.Plugin;
import com.rc2s.dao.IGenericDAO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface IPluginDAO extends IGenericDAO<Plugin>
{
	public List<Plugin> getAvailables() throws DAOException;
	public Plugin getByName(String name) throws DAOException;
}
