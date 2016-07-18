package com.rc2s.ejb.group;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Group;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface GroupFacadeRemote
{
	public List<Group> getAll(final User caller) throws EJBException;
}
