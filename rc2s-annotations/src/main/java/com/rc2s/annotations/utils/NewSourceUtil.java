package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;
import java.util.ArrayList;

public class NewSourceUtil
{	
	private final ArrayList<String> controllersList = null;
	
	private String pluginName = null;
	
	private final String statelessPackage	= "javax.ejb.Stateless";
	private final String statefulPackage	= "javax.ejb.Stateful";
	private final String remotePackage		= "javax.ejb.Remote";
	private final String localPackage		= "javax.ejb.Local";
	private final String entityPackage		= "javax.persistence.Entity";
	
	private ElementMapper mainClass = null;
	
	public NewSourceUtil(ElementMapper mainClass)
	{
		this.mainClass = mainClass;
	}
	
	public void verifySource()
	{
		String[] packageParts = mainClass.getPackageName().split("\\.");
		
		try
		{
			// Verify root - shall be com.rc2s.{plugin_name}
			verifyRoot(packageParts);
			
			System.err.println("\nPLUGIN NAME : " + pluginName);
			
			// Then find class type - see com.rc2s.annotations.utils.ClassNamesEnum
			ClassNamesEnum cne = findClassName(packageParts);
			
			System.err.println("CLASS TYPE : " + cne.name());
			
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
				
				System.err.println("ENTITY : " + entityName);
			}
			
			// Then verify class compulsorys' (name, annotations..)
			verifyClassStandards(mainClass, cne, entityName);
		}
		catch (SourceControlException scex)
		{
			System.err.println(scex.getMessage());
		}
	}

	private void verifyRoot(String[] packageParts) throws SourceControlException
	{
		String name = null;
		
		if (packageParts.length < 3)
			throw new SourceControlException("Invalid package naming - Too short (expected : more than 2 parts)");
		
		if (!packageParts[0].equals("com"))
			throw new SourceControlException("Invalid package naming - Should begin with 'com', have '" + packageParts[0] + "'");
		
		if (!packageParts[1].equals("rc2s"))
			throw new SourceControlException("Invalid package naming - Should begin with 'com.rc2s', have 'com." + packageParts[1] + "'");
		
		boolean hasUppers = !packageParts[2].equals(packageParts[2].toLowerCase()); 

		if (hasUppers)
			throw new SourceControlException("Invalid plugin naming - Should be lowercase");
		else if (null == pluginName)
			pluginName = packageParts[2];
		else if (!pluginName.equals(packageParts[2]))
			throw new SourceControlException("Invalid plugin naming - Already retrieved '" + pluginName + "' and found '" + packageParts[2] + "' in this class");
	}

	private ClassNamesEnum findClassName(String[] packageParts) throws SourceControlException
	{
		if (packageParts.length < 5)
			throw new SourceControlException("Invalid package naming - Too short (expected : more than 4 parts)");
		
		switch (packageParts[3]) 
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
				throw new SourceControlException("Invalid package naming after plugin name - Expected (ejb|application|dao|common|client), got '" + packageParts[3] + "'");
		}
	}
	
	private ClassNamesEnum commonClassNameFromPackage(String packagePart) throws SourceControlException
	{
		switch (packagePart) 
		{
			case "vo":
				return ClassNamesEnum.VO;
				
			case "sql":
				return ClassNamesEnum.SQL;
				
			default:
				throw new SourceControlException("Invalid common package naming - Expected (vo|sql), got '" + packagePart + "'");
		}
	}

	private ClassNamesEnum clientClassNameFromPackage(String packagePart) throws SourceControlException
	{
		switch (packagePart) 
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
				throw new SourceControlException("Invalid client package naming - Expected (controllers|views|css|images|utils), got '" + packagePart + "'");
		}
	}
	
	private String verifyPackageEnd(String[] packageParts) throws SourceControlException
	{
		boolean hasUppers = !packageParts[4].equals(packageParts[4].toLowerCase());
		
		if (packageParts.length < 5)
			throw new SourceControlException("Invalid package naming - Too short (expected : more than 4 parts)");
		else if (hasUppers)
			throw new SourceControlException("Invalid " + packageParts[3] + " package naming - Shall be lowercase");
		else
			packageParts[4] = Character.toUpperCase(packageParts[4].charAt(0)) + packageParts[4].substring(1);
		
		return packageParts[4];
	}

	/**
	* void verifyClassStandards(mainClass, cne, entityName)
	* 
	* pn.ejb.entityName			OK -> shall be 'NameFacadeRemote' & 'NameFacadeBean'
	*							OK -> annotation @Stateless or @Stateful for 'NameFacadeBean' 
	*							OK -> annotation @Remote for 'NameFacadeRemote'
	*
	* pn.application.entityName	OK -> shall be 'INameService' & 'NameService'
	*							OK -> annotation @Stateless or @Stateful for 'NameService'
	*							OK -> annotation @Local for 'INameService'
	*
	* pn.dao.entityName			OK -> shall be 'INameDAO' & 'NameDAO'
	*							OK -> annotation @Stateless or @Stateful for 'NameDAO'
	*							OK -> annotation @Local for 'INameDAO'
	* 
	* pn.common.vo				OK -> shall be 'Name' & annotation @Entity
	* pn.common.sql				-> pluginname.sql - different analysis because it's a file and not a class
	* 
	* pn.client.controllers		-> shall be 'NameController' (shall have an initialize() method with parameters url & rb)
	*							OK -> save controller's name to verify later whether he has a linked view
	* pn.client.views			-> analysing the controller, check in package views if NameView.fxml exists
	* pn.client.utils			OK -> void (utils for plugin creation, shall only have annotation @SourceControl)
	* pn.client.css				OK -> void
	* pn.client.images			OK -> void
	*/
	private void verifyClassStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName) throws SourceControlException
	{
		if (cne != null)
		{
			switch (cne)
			{
				case EJB:
					verifyEjbStandards(mainClass, cne, entityName);
					break;

				case APPLICATION:
					verifyApplicationStandards(mainClass, cne, entityName);
					break;

				case DAO:
					verifyDaoStandards(mainClass, cne, entityName);
					break;
					
				case VO:
					verifyVoStandards(mainClass, cne, entityName);
					break;

				case SQL:
					verifySqlStandards(mainClass, cne, entityName);
					break;
						
				case CONTROLLERS:
					verifyControllersStandards(mainClass, cne, entityName);
					break;
						
				case VIEWS:
					verifyViewsStandards(mainClass, cne, entityName);
					break;
						
				case UTILS:		//void - utils only need @SourceControl
				case CSS:		//void
				case IMAGES:		//void
				default:
					break;
			}
		}
		
		System.err.println("");
	}

	private void verifyEjbStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName) throws SourceControlException
	{
		if (mainClass.getName().equals(entityName + "FacadeRemote"))
		{
			System.err.println("CLASS NAME : " + mainClass.getName());
			
			if (!mainClass.getAnnotations().contains(remotePackage))
				throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + remotePackage + "'");
		}
		else if (mainClass.getName().equals(entityName + "FacadeBean"))
		{
			System.err.println("CLASS NAME : " + mainClass.getName());
			
			checkClassHasStatelessOrStatefulAnnotation(mainClass);
		}
		else
		{
			throw new SourceControlException("Invalid class name - "
				+ "Expected '" + entityName + "FacadeRemote' or '" + entityName + "FacadeBean', got '" + mainClass.getName() + "'");
		}
	}

	private void verifyApplicationStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName) throws SourceControlException
	{
		if (mainClass.getName().equals("I" + entityName + "Service"))
		{
			System.err.println("CLASS NAME : " + mainClass.getName());
			
			if (!mainClass.getAnnotations().contains(localPackage))
				throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + localPackage + "'");
		}
		else if (mainClass.getName().equals(entityName + "Service"))
		{
			System.err.println("CLASS NAME : " + mainClass.getName());
			
			checkClassHasStatelessOrStatefulAnnotation(mainClass);
		}
		else
		{
			throw new SourceControlException("Invalid class name - "
				+ "Expected 'I" + entityName + "Service' or '" + entityName + "Service', got '" + mainClass.getName() + "'");
		}
	}

	private void verifyDaoStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName) throws SourceControlException
	{
		if (mainClass.getName().equals("I" + entityName + "DAO"))
		{
			System.err.println("CLASS NAME : " + mainClass.getName());
			
			if (!mainClass.getAnnotations().contains(localPackage))
				throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + localPackage + "'");
		}
		else if (mainClass.getName().equals(entityName + "DAO"))
		{
			System.err.println("CLASS NAME : " + mainClass.getName());
			
			checkClassHasStatelessOrStatefulAnnotation(mainClass);
		}
		else
		{
			throw new SourceControlException("Invalid class name - "
				+ "Expected 'I" + entityName + "DAO' or '" + entityName + "DAO', got '" + mainClass.getName() + "'");
		}
	}
	
	private void checkClassHasStatelessOrStatefulAnnotation(ElementMapper mainClass) throws SourceControlException
	{
		Boolean hasStateless	= mainClass.getAnnotations().contains(statelessPackage);
		Boolean hasStateful		= mainClass.getAnnotations().contains(statefulPackage);
		Boolean hasBoth		= hasStateless && hasStateful;
		Boolean hasNone		= !hasStateless && !hasStateful;

		if (hasBoth)
		{
			throw new SourceControlException("Class " + mainClass.getName() + 
				" has both " + statelessPackage + " & " + statefulPackage + " annotations : it should only have one");
		}
		else if (hasNone)
		{
			throw new SourceControlException("Class " + mainClass.getName() + " requires " + statelessPackage + " or " + statefulPackage + " annotation");
		}
	}

	private void verifyVoStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName) throws SourceControlException
	{
		// Regex for name ? [A-Z]([a-z])+
		System.err.println("CLASS NAME : " + mainClass.getName());
		
		if (!mainClass.getAnnotations().contains(entityPackage))
			throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + entityPackage + "'");
	}

	private void verifySqlStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName) 
	{
		// SHEAA WATTODO HERE ??
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void verifyControllersStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName) throws SourceControlException
	{
		if (controllersList.contains(mainClass.getName()))
			throw new SourceControlException("Controller " + mainClass.getName() + " already in controllers list. You might have added two times the same controller.");
		
		controllersList.add(mainClass.getName());
	}

	private void verifyViewsStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
