package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;

public class SourceUtil
{	
	public static void verifySource(ElementMapper mainClass)
	{
		String[] packageParts = mainClass.getPackageName().split("\\.");
		
		try
		{
			// Verify root - shall be com.rc2s.{plugin_name}
			String pluginName = verifyRoot(packageParts);
		
			// Then find class type
			ClassNamesEnum cne = findClassName(packageParts);
		
			// Then verify remaining package parts depending on ClassNamesEnum retrieved
			String entityName = null;
			if (ClassNamesEnum.APPLICATION.equals(cne)
				|| ClassNamesEnum.DAO.equals(cne)
				|| ClassNamesEnum.EJB.equals(cne))
			{
				entityName = verifyPackageEnd(packageParts);
			}
			
			// Then verify necessary class things (name, annotations..)
			verifyClassStandards(mainClass, cne, entityName);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static String verifyRoot(String[] packageParts)
	{
		String name = null;
		
		if (packageParts.length < 3)
			throw new UnsupportedOperationException("Not supported yet.");
		
		// Check lowercase and without lowercase ? "Shall be lowercase"
		if (packageParts[0].toLowerCase().equals("com") && packageParts[1].toLowerCase().equals("rc2s"))
		{
			// Shall check with regex ([a-zA-Z][-_a-zA-Z]+) ?
			name = packageParts[2];
		}
		else
			throw new UnsupportedOperationException("Not supported yet.");
		
		return name;
	}

	private static ClassNamesEnum findClassName(String[] packageParts)
	{
		if (packageParts.length < 4)
			throw new UnsupportedOperationException("Not supported yet.");
		else if (packageParts.length < 5)
			throw new UnsupportedOperationException("Not supported yet.");
		
		// Idem check lowercase ?
		switch (packageParts[3].toLowerCase()) 
		{
			case "ejb":
				return ClassNamesEnum.EJB;
				
			case "application":
				return ClassNamesEnum.APPLICATION;
				
			case "dao":
				return ClassNamesEnum.DAO;
				
			case "common":
				return CommonClassNameFromPackage(packageParts[4]);
				
			case "client":
				return ClientClassNameFromPackage(packageParts[4]);
				
			default:
				throw new UnsupportedOperationException("Not supported yet.");
		}
	}
	
	private static ClassNamesEnum CommonClassNameFromPackage(String packagePart)
	{
		// Idem check lowercase ?
		switch (packagePart.toLowerCase()) 
		{
			case "vo":
				return ClassNamesEnum.VO;
				
			case "sql":
				return ClassNamesEnum.SQL;
				
			default:
				throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	private static ClassNamesEnum ClientClassNameFromPackage(String packagePart)
	{
		// Idem check lowercase ?
		switch (packagePart.toLowerCase()) 
		{
			case "controllers":
				return ClassNamesEnum.CONTROLLERS;
				
			case "views":
				return ClassNamesEnum.VIEWS;
				
			case "css":
				return ClassNamesEnum.CSS;
				
			case "images":
				return ClassNamesEnum.IMAGES;
				
			case "utils":
				return ClassNamesEnum.UTILS;
				
			default:
				throw new UnsupportedOperationException("Not supported yet.");
		}
	}
	
	private static String verifyPackageEnd(String[] packageParts)
	{
		if (packageParts.length < 5)
			throw new UnsupportedOperationException("Not supported yet.");
		
		return packageParts[4];
	}

	private static void verifyClassStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName)
	{
		
	}
}
