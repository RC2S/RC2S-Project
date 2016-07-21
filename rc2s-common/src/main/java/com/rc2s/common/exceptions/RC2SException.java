package com.rc2s.common.exceptions;

/**
 * RC2SException
 * 
 * Basic RC2S exception
 * 
 * @author RC2S
 */
public class RC2SException extends Exception
{
	public RC2SException(final Throwable t)
	{
		super(t.getMessage(), t);
	}
	
	public RC2SException(final String str)
	{
		super(str);
	}
	
	public RC2SException(final String str, final Throwable t)
	{
		super(str, t);
	}
}
