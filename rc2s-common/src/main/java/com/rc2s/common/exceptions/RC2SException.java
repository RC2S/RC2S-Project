package com.rc2s.common.exceptions;

public class RC2SException extends Exception
{
	public RC2SException(Throwable t)
	{
		super(t.getMessage(), t);
	}
	
	public RC2SException(String str)
	{
		super(str);
	}
	
	public RC2SException(String str, Throwable t)
	{
		super(str, t);
	}
}
