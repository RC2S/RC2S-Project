package com.rc2s.common.exceptions;

/**
 * ServiceException
 * 
 * RC2S exception raised by services
 * 
 * @author RC2S
 */
public class ServiceException extends RC2SException
{
	public ServiceException(final Throwable t)
	{
		super(t);
	}

	public ServiceException(final String msg)
	{
		super(msg);
	}
}
