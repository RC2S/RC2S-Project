package com.rc2s.common.exceptions;

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
