package com.rc2s.ejb.group;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Group;
import java.util.List;
import javax.ejb.Remote;

/**
 * GroupFacadeRemote
 * 
 * EJB remote interface for Group EJB
 * 
 * @author RC2S
 */
@Remote
public interface GroupFacadeRemote
{
	public List<Group> getAll() throws EJBException;
}
