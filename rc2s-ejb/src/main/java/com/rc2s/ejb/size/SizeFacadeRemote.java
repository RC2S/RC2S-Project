package com.rc2s.ejb.size;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Size;
import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface SizeFacadeRemote
{
	public List<Size> getAll(final User caller) throws EJBException;
    
	public Size add(final User caller, final Size size) throws EJBException;
}
