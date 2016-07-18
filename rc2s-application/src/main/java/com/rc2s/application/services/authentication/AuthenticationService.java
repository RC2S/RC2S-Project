package com.rc2s.application.services.authentication;

import com.rc2s.common.exceptions.DAOException;
import com.rc2s.common.exceptions.ServiceException;
import com.rc2s.common.utils.Hash;
import com.rc2s.common.vo.User;
import com.rc2s.dao.user.IUserDAO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AuthenticationService implements IAuthenticationService
{  
    @EJB
    private IUserDAO userDAO;
    
    private final String SALT = "c33A0{-LO;<#CB `k:^+8DnxAa.BX74H07z:Qn+U0yD$3ar+.:ge[nc>Trs|Fxy";
	private final String PEPPER = ">m9I}JqHTg:VZ}XISdcG;)yGu)t]7Qv5YT:ZWI^#]f06Aq<c]n7a? x+ZEl#pt:";

    @Override
	public User login(final String username, final String password) throws ServiceException
	{
		try
		{
			User user = null;
			
			if(username != null && password != null)
			{
				String securedPassword = Hash.sha1(SALT + password + PEPPER);
				user = userDAO.getAuthenticatedUser(username, securedPassword);
				
				if(user != null)
				{
					int code = userDAO.setLastLogin(user);
				
					if(code != 1)
					{
						System.err.println("Unable to update user's last IP address. Return code is: " + code);
					}
				}
			}
			
			return user;
		}
		catch(DAOException e)
		{
			throw new ServiceException(e);
		}
	}
}