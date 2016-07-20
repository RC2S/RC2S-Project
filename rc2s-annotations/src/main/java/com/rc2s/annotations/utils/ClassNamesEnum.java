package com.rc2s.annotations.utils;

/**
 * ClassNamesEnum
 * 
 * Classes allowed (expected) to have the @SourceControl annotation,
 * used for plugin code construction recognition.
 * 
 * @author RC2S
 */
public enum ClassNamesEnum
{
	// EJB Class
	EJB,
	
	// Application Class
	APPLICATION,
	
	// DAO Class
	DAO,
	
	// Common Classes
	VO,
	
	// Client Classes
	CONTROLLERS,
}
