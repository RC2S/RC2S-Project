package com.rc2s.annotations.utils;

/**
 * SourceControlException
 * 
 * Exception class used in SourceUtil to control exception
 * during the verifications - Several classes may raise 
 * this exception as it happens during compile time.
 * 
 * @author RC2S
 */
public class SourceControlException extends Exception
{
	public SourceControlException()
	{
		super();
	}
	
	public SourceControlException(final String message)
	{
		super(message);
	}
}
