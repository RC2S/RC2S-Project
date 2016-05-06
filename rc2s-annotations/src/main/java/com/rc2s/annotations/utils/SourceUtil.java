package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class SourceUtil
{	
	private static Messager messager;
	
	public static void verifySource(ElementMapper mainClass)
	{
		String[] packageParts = mainClass.getPackageName().split("\\.");
		
		try
		{
			// Verify root - shall be com.rc2s.{plugin_name}
			String pluginName = verifyRoot(packageParts);
			
			messager.printMessage(Diagnostic.Kind.NOTE, "PluginName found : " + pluginName);
		
			// Then find class type - see com.rc2s.annotations.utils.ClassNamesEnum
			ClassNamesEnum cne = findClassName(packageParts);
			
			messager.printMessage(Diagnostic.Kind.NOTE, "Class type found : " + cne.name());
		
			/**
			 * Then verify remaining package parts depending on ClassNamesEnum retrieved
			 * Those parts shall be :
			 * (ejb | application | dao).name
			 * Others types are already verified within ClassNames Enum
			 */
			String entityName = null;
			
			if (ClassNamesEnum.APPLICATION.equals(cne)
				|| ClassNamesEnum.DAO.equals(cne)
				|| ClassNamesEnum.EJB.equals(cne))
			{
				entityName = verifyPackageEnd(packageParts);
				
				messager.printMessage(Diagnostic.Kind.NOTE, "Entity found : " + entityName);
			}
			
			// Then verify class compulsorys' (name, annotations..)
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
			throw new UnsupportedOperationException("Not supported yet - verifyRoot() - packageParts.length < 3");
		
		// Check lowercase and without lowercase ? "Shall be lowercase"
		if (packageParts[0].toLowerCase().equals("com") && packageParts[1].toLowerCase().equals("rc2s"))
		{
			// Shall check with regex ([a-zA-Z][-_a-zA-Z]+) ?
			name = packageParts[2];
		}
		else
			throw new UnsupportedOperationException("Not supported yet - verifyRoot() - package not com rc2s");
		
		return name;
	}

	private static ClassNamesEnum findClassName(String[] packageParts)
	{
		if (packageParts.length < 4)
			throw new UnsupportedOperationException("Not supported yet - findClassName() - packageParts.length < 4");
		else if (packageParts.length < 5)
			throw new UnsupportedOperationException("Not supported yet - findClassName() - packageParts.length < 5");
		
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
				return commonClassNameFromPackage(packageParts[4]);
				
			case "client":
				return clientClassNameFromPackage(packageParts[4]);
				
			default:
				throw new UnsupportedOperationException("Not supported yet - findClassName() - package naming incorrect");
		}
	}
	
	private static ClassNamesEnum commonClassNameFromPackage(String packagePart)
	{
		// Idem check lowercase ?
		switch (packagePart.toLowerCase()) 
		{
			case "vo":
				return ClassNamesEnum.VO;
				
			case "sql":
				return ClassNamesEnum.SQL;
				
			default:
				throw new UnsupportedOperationException("Not supported yet - commonClassNameFromPackage() - package naming incorrect");
		}
	}

	private static ClassNamesEnum clientClassNameFromPackage(String packagePart)
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
				throw new UnsupportedOperationException("Not supported yet - clientClassNameFromPackage() - package naming incorrect");
		}
	}
	
	private static String verifyPackageEnd(String[] packageParts)
	{
		if (packageParts.length < 5)
			throw new UnsupportedOperationException("Not supported yet - verifyPackageEnd() - packageParts.length < 5");
		
		return packageParts[4];
	}

	/**
	* pn.ejb.entityName			-> shall be 'NameFacadeRemote' & 'NameFacadeBean'
	*							-> annotation @Stateless or @Stateful for 'NameFacadeBean' 
	*							-> annotation @Remote for 'NameFacadeRemote'
	*
	* pn.application.entityName	-> shall be 'INameService' & 'NameService'
	*							-> annotation @Stateless or @Stateful for 'NameService'
	*							-> annotation @Local for 'INameService'
	*
	* pn.dao.entityName			-> shall be 'INameDao' & 'NameDao'
	*							-> annotation @Stateless or @Stateful for 'NameDao'
	*							-> annotation @Local for 'INameDao'
	* 
	* pn.common.vo				-> shall be 'Name' & annotation @Entity
	* pn.common.sql				-> pluginname.sql - different analysis because it's a file and not a class
	* 
	* pn.client.controllers		-> shall be 'NameController' (shall have an initialize() method with parameters url & rb)
	*							-> save controller's name to verify later whether he has a linked view
	* pn.client.views			-> analysing the controller, check in package views if NameView.fxml exists
	* pn.client.utils			-> void (utils for plugin creation, shall only have annotation @SourceControl)
	* pn.client.css				-> void
	* pn.client.images			-> void
	*/
	private static void verifyClassStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName)
	{	
		if (null != cne)
		{
			switch (cne)
			{
				case APPLICATION:
					if (!mainClass.getName().equals(entityName + "FacadeRemote") || !mainClass.getName().equals(entityName + "FacadeBean"))
					{
						throw new UnsupportedOperationException("Not supported yet - Invalid Application class name");
					}
					break;

				case DAO:
					if (!mainClass.getName().equals("I" + entityName + "Service") || !mainClass.getName().equals(entityName + "Service"))
					{
						throw new UnsupportedOperationException("Not supported yet - Invalid DAO class name");
					}
					break;

				case EJB:
					if (!mainClass.getName().equals("I" + entityName + "Dao") || !mainClass.getName().equals(entityName + "Dao"))
					{
						throw new UnsupportedOperationException("Not supported yet - Invalid EJB class name");
					}
					break;

				default:
					break;
			}
		}
	}
}
