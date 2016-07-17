package com.rc2s.ejb.role;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Group;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface RoleFacadeRemote
{
	public List<Group> getAll(final User caller) throws EJBException;
}
