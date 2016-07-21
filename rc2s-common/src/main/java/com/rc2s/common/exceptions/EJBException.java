package com.rc2s.common.exceptions;

/**
 * EJBException
 * 
 * RC2S exception raised by EJBs
 * 
 * @author RC2S
 */
public class EJBException extends RC2SException
{
	public EJBException(final Throwable t)
	{
		super(t);
	}
}
