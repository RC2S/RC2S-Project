package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class SourceUtil
{	
	private static Messager messager;
	
	private static String pluginName = null;
	
	public static void verifySource(ElementMapper mainClass)
	{
		String[] packageParts = mainClass.getPackageName().split("\\.");
		
		try
		{
			// Verify root - shall be com.rc2s.{plugin_name}
			verifyRoot(packageParts);
			
			System.err.println("PLUGIN NAME : " + pluginName);
			
			//if (pluginName != null)
			//	messager.printMessage(Diagnostic.Kind.NOTE, "PluginName found : " + pluginName);

			// Then find class type - see com.rc2s.annotations.utils.ClassNamesEnum
			ClassNamesEnum cne = findClassName(packageParts);
			
			System.err.println("CLASS TYPE : " + cne.name());
			
			//if (cne.name() != null)
			//	messager.printMessage(Diagnostic.Kind.NOTE, "Class type found : " + cne.name());
		
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
				
				//messager.printMessage(Diagnostic.Kind.NOTE, "Entity found : " + entityName);
			}
			
			// Then verify class compulsorys' (name, annotations..)
			verifyClassStandards(mainClass, cne, entityName);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static void verifyRoot(String[] packageParts)
	{
		String name = null;
		
		if (packageParts.length < 3)
			throw new UnsupportedOperationException("Not supported yet - verifyRoot() - packageParts.length < 3");
		
		if (packageParts[0].equals("com") && packageParts[1].equals("rc2s"))
		{
			boolean hasUppers = !packageParts[2].equals(packageParts[2].toLowerCase()); 
			
			if (hasUppers)
				throw new UnsupportedOperationException("Not supported yet - verifyRoot() - plugin name shall be lowercase");
			else if (null == pluginName)
				pluginName = packageParts[2];
			else if (!pluginName.equals(packageParts[2]))
				throw new UnsupportedOperationException("Not supported yet - verifyRoot() - plugin name found differs from origin");
		}
		else
			throw new UnsupportedOperationException("Not supported yet - verifyRoot() - package not com rc2s");
	}

	private static ClassNamesEnum findClassName(String[] packageParts)
	{
		if (packageParts.length < 5)
			throw new UnsupportedOperationException("Not supported yet - findClassName() - packageParts.length < 5");
		
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
				throw new UnsupportedOperationException("Not supported yet - findClassName() - package naming incorrect");
		}
	}
	
	private static ClassNamesEnum commonClassNameFromPackage(String packagePart)
	{
		switch (packagePart) 
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
				throw new UnsupportedOperationException("Not supported yet - clientClassNameFromPackage() - package naming incorrect");
		}
	}
	
	private static String verifyPackageEnd(String[] packageParts)
	{
		boolean hasUppers = !packageParts[4].equals(packageParts[4].toLowerCase());
		
		if (packageParts.length < 5)
			throw new UnsupportedOperationException("Not supported yet - verifyPackageEnd() - packageParts.length < 5");
		else if (hasUppers)
			throw new UnsupportedOperationException("Not supported yet - verifyPackageEnd() - classname in package shall be lowercase");
		else
			packageParts[4] = Character.toUpperCase(packageParts[4].charAt(0)) + packageParts[4].substring(1);
		
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
		if (cne != null)
		{
			switch (cne)
			{
				case EJB:
					System.err.println("MainClass : " + mainClass.getName() + ", Built entity : " + entityName + "Facade(...)");
					if (!mainClass.getName().equals(entityName + "FacadeRemote") && !mainClass.getName().equals(entityName + "FacadeBean"))
					{
						throw new UnsupportedOperationException("Not supported yet - Invalid Application class name");
					}
					else
					{
						System.err.println("CLASS NAME : " + mainClass.getName());
						
						//messager.printMessage(Diagnostic.Kind.NOTE, "Class name found : " + mainClass.getName());
					}
					break;

				case APPLICATION:
					System.err.println("MainClass : " + mainClass.getName() + ", Built entity : " + entityName + "Service");
					if (!mainClass.getName().equals("I" + entityName + "Service") && !mainClass.getName().equals(entityName + "Service"))
					{
						throw new UnsupportedOperationException("Not supported yet - Invalid DAO class name");
					}
					else
					{
						System.err.println("CLASS NAME : " + mainClass.getName());
						
						//messager.printMessage(Diagnostic.Kind.NOTE, "Class name found : " + mainClass.getName());
					}
					break;

				case DAO:
					System.err.println("MainClass : " + mainClass.getName() + ", Built entity : " + entityName + "Dao");
					if (!mainClass.getName().equals("I" + entityName + "Dao") && !mainClass.getName().equals(entityName + "Dao"))
					{
						throw new UnsupportedOperationException("Not supported yet - Invalid EJB class name");
					}
					else
					{
						System.err.println("CLASS NAME : " + mainClass.getName());
						
						//messager.printMessage(Diagnostic.Kind.NOTE, "Class name found : " + mainClass.getName());
					}
					break;

				default:
					break;
			}
		}
		
		System.err.println("");
	}
}
