package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;

public class SourceUtil
{	
	public static void verifySource(ElementMapper mainClass)
	{
		// Verify package construction
		String[] packageParts = mainClass.getPackageName().split(".");
		
		System.err.println("Package parts :");
		for (String part : packageParts)
		{
			System.err.println("  -> " + part.toString());
		}
		
		/*
		try
		{
			verifyPackageNaming(packageParts);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		*/
		
		// Verify class name
		/*
		try
		{
			verifyClassName(mainClass.name);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		*/
	}
}
