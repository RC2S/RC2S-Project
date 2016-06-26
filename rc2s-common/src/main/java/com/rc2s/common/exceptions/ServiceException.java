package com.rc2s.common.exceptions;

public class ServiceException extends RC2SException
{
	public ServiceException(Throwable t)
	{
		super(t);
	}

	public ServiceException(String msg)
	{
		super(msg);
	}
}
