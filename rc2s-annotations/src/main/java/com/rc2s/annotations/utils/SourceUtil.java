package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.mappers.ParameterMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class SourceUtil
{	
	// Is it first verification ?
	private static boolean isFirstCheck = true;
	
	// Name of the plugin, found from the package name
	private static String pluginName = null;
	
	// List of controllers & views within the plugin
	private static ArrayList<String> expectedControllersList = null;
	private static ArrayList<String> controllersList = null;
	private static ArrayList<String> initialControllersList = null;
	private static ArrayList<String> viewsList = null;
	
	// Should contain exactly one MainController & one MainView
	private static boolean hasMainController = false;
	private static boolean hasMainView = false;
	
	// Needed annotations package names & packages paths
	private final static String controllersFolderPath	= "/media/Data/RC2S/rc2s-project/rc2s-client/src/main/java/com/rc2s/client/controllers";
	private final static String viewsFolderPath			= "/media/Data/RC2S/rc2s-project/rc2s-client/src/main/resources/views";
	private final static String statelessPackage		= "javax.ejb.Stateless";
	private final static String statefulPackage			= "javax.ejb.Stateful";
	private final static String remotePackage			= "javax.ejb.Remote";
	private final static String localPackage			= "javax.ejb.Local";
	private final static String entityPackage			= "javax.persistence.Entity";
	
	public SourceUtil()
	{
		if (expectedControllersList == null)
			expectedControllersList = new ArrayList<>();
		
		if (controllersList == null)
			controllersList = new ArrayList<>();
		
		if (initialControllersList == null)
			initialControllersList = new ArrayList<>();
		
		if (viewsList == null)
			viewsList = new ArrayList<>();
	}
	
	public void verifySource(ElementMapper mainClass)
	{
		// First, get all views names and verify there is a MainView.fxml
		if (isFirstCheck)
		{
			try
			{
				getAllViewsNames();
				getAllControllersNames();
				
				for (String controllerName : expectedControllersList)
				{
					if (!initialControllersList.contains(controllerName))
						throw new SourceControlException("Controller " + controllerName + " was expected and wasn't found");
				}
			}
			catch (SourceControlException | ParserConfigurationException | SAXException | IOException ex)
			{
				System.err.println(ex.getMessage());
			}
			
			isFirstCheck = false;
		}
		
		String[] packageParts = mainClass.getPackageName().split("\\.");
		
		try
		{
			// Verify root - shall be com.rc2s.{plugin_name}
			verifyRoot(packageParts);
			
			// Then find class type - see com.rc2s.annotations.utils.ClassNamesEnum
			ClassNamesEnum cne = findClassName(packageParts);
			
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
	* pn.client.controllers		OK -> shall be 'NameController' (shall have an initialize() method with parameters url & rb)
	*							OK -> save controller's name to verify later whether he has a linked view
	*							OK -> shall contain a 'MainController'
	* pn.client.views			OK -> retrieve views and expected controllers
	*							OK -> shall contain a 'MainView'
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
					verifyEjbStandards(mainClass, entityName);
					break;

				case APPLICATION:
					verifyApplicationStandards(mainClass, entityName);
					break;

				case DAO:
					verifyDaoStandards(mainClass, entityName);
					break;

				case VO:
					verifyVoStandards(mainClass);
					break;

				case SQL:
					verifySqlStandards(mainClass, cne, entityName);
					break;
	
				case CONTROLLERS:
					verifyControllerStandards(mainClass);
					break;

				case UTILS:		//void - utils only need @SourceControl
				case CSS:		//void
				case IMAGES:		//void
				default:
					break;
			}
		}
	}

	private void verifyEjbStandards(ElementMapper mainClass, String entityName) throws SourceControlException
	{
		if (mainClass.getName().equals(entityName + "FacadeRemote"))
		{
			if (!mainClass.getAnnotations().contains(remotePackage))
				throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + remotePackage + "'");
		}
		else if (mainClass.getName().equals(entityName + "FacadeBean"))
		{
			checkClassHasStatelessOrStatefulAnnotation(mainClass);
		}
		else
		{
			throw new SourceControlException("Invalid class name - "
				+ "Expected '" + entityName + "FacadeRemote' or '" + entityName + "FacadeBean', got '" + mainClass.getName() + "'");
		}
	}

	private void verifyApplicationStandards(ElementMapper mainClass, String entityName) throws SourceControlException
	{
		if (mainClass.getName().equals("I" + entityName + "Service"))
		{
			if (!mainClass.getAnnotations().contains(localPackage))
				throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + localPackage + "'");
		}
		else if (mainClass.getName().equals(entityName + "Service"))
		{
			checkClassHasStatelessOrStatefulAnnotation(mainClass);
		}
		else
		{
			throw new SourceControlException("Invalid class name - "
				+ "Expected 'I" + entityName + "Service' or '" + entityName + "Service', got '" + mainClass.getName() + "'");
		}
	}

	private void verifyDaoStandards(ElementMapper mainClass, String entityName) throws SourceControlException
	{
		if (mainClass.getName().equals("I" + entityName + "DAO"))
		{
			if (!mainClass.getAnnotations().contains(localPackage))
				throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + localPackage + "'");
		}
		else if (mainClass.getName().equals(entityName + "DAO"))
		{
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

	private void verifyVoStandards(ElementMapper mainClass) throws SourceControlException
	{
		// Compile the regex
		Pattern p = Pattern.compile("(([A-Z]+)([a-z]*))+");
		// Create search engine
		Matcher m = p.matcher(mainClass.getName());
		// Search occurences
		Boolean b = m.matches();
		
		if (!b)
			throw new SourceControlException("Class " + mainClass.getName() + " shoul be CamelCase.");
		
		if (!mainClass.getAnnotations().contains(entityPackage))
			throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + entityPackage + "'");
	}

	private void verifySqlStandards(ElementMapper mainClass, ClassNamesEnum cne, String entityName) 
	{
		// Behaviour to determine
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void verifyControllerStandards(ElementMapper mainClass) throws SourceControlException
	{
		// We shall find initialize(URL, ResourceBundle)
		boolean hasJavaNetURL = false;
		boolean hasResourceBundle = false;
		boolean hasInitializeMethod = false;
		
		String error = mainClass.getName() + "'s initialize method shall contain exactly 2 args : URL & ResourceBundle";
		
		// MainController will be used for MainView.fxml
		if (mainClass.getName().equals("MainController"))
		{
			if (hasMainController)
				throw new SourceControlException("Found two MainController.java, your plugin should only contain one.");
			
			hasMainController = true;
		}
		
		// Iterate over methods
		for (ElementMapper em : mainClass.getMethods())
		{
			// If it's initialize(), check its args
			if (em.getName().equals("initialize"))
			{
				hasInitializeMethod = true;
				
				// Parameters URL & ResourceBundle are needed
				if (em.getParameters().size() == 2)
				{
					for (ParameterMapper pm : em.getParameters())
					{
						if (pm.getType().equals("java.net.URL"))
							hasJavaNetURL = true;
						else if (pm.getType().equals("java.util.ResourceBundle"))
							hasResourceBundle = true;
					}
					
					if (!hasJavaNetURL || !hasResourceBundle)
						throw new SourceControlException(error);
				}
				else
					throw new SourceControlException(error);
			}
		}
		if (!hasInitializeMethod)
			throw new SourceControlException("Controller " + mainClass.getName() + " shall implement javafx.fxml.Initializable initialize() method.");
		
		// Check if it already exists
		if (controllersList.contains(mainClass.getName()))
			throw new SourceControlException("Controller " + mainClass.getName() + " already in controllers list. You might have added two times the same controller.");
		
		controllersList.add(mainClass.getName());
	}
	
	private void getAllControllersNames()
	{
		File testDirectory = new File(controllersFolderPath);
		File[] files = testDirectory.listFiles(
			(File pathname) -> pathname.getName().endsWith(".java") 
							&& pathname.isFile()
		);
		
		// Get all Views names
		for (File file : files)
			initialControllersList.add(file.getName());
	}

	private void getAllViewsNames() throws SourceControlException, IOException, ParserConfigurationException, SAXException
	{
		File testDirectory = new File(viewsFolderPath);
		File[] files = testDirectory.listFiles(
			(File pathname) -> pathname.getName().endsWith(".fxml") 
							&& pathname.isFile()
		);
		
		// Get all Views names
		for (File file : files)
			viewsList.add(file.getName());
		
		// Get associated Controllers
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		for (String fileName : viewsList)
		{
			if (fileName.equals("MainView.fxml"))
			{
				if (hasMainView)
					throw new SourceControlException("Your plugin should only contain one MainView.fxml");
				
				hasMainView = true;
			}
			
			// Parse fxml file
			Document doc = docBuilder.parse(viewsFolderPath + "/" + fileName);
			
			// Retrieve first AnchorPane Node element
			Node anchorNode = doc.getElementsByTagName("AnchorPane").item(0);
			
			if (anchorNode != null
				&& anchorNode.hasAttributes()
				&& anchorNode.getAttributes().getNamedItem("fx:controller") != null)
			{
				expectedControllersList.add(
					anchorNode
					.getAttributes()
					.getNamedItem("fx:controller")
					.getNodeValue()
					.split("controllers.")[1]
					.concat(".java")
				);
			}
			else
				throw new SourceControlException("No fx:controller attribute has been found on main AnchorPane in " + fileName);
		}
		
		if (!hasMainView)
			throw new SourceControlException("MainView.fxml expected in views package");
	}
}
