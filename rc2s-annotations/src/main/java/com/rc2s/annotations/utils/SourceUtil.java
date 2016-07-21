package com.rc2s.annotations.utils;

import com.rc2s.annotations.mappers.ElementMapper;
import com.rc2s.annotations.mappers.ParameterMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SourceUtil
 * 
 * Core of the SourceControl annotation.
 * Processes the content of the class, package included,
 * in order to verify that the class follow the rules 
 * required to integrate a RC2S plugin.
 * 
 * @author RC2S
 */
public class SourceUtil
{	
	// Is it first verification ?
	private static boolean isFirstCheck = true;
	
	// Name of the plugin, found from the package name
	private static String pluginName = null;
	
	// List of controllers & views within the plugin
	private static ArrayList<String> expectedControllersList	= null;
	private static ArrayList<String> initialControllersList		= null;
	private static ArrayList<String> controllersList			= null;
	private static ArrayList<String> viewsList					= null;
	
	// Should contain exactly one MainController & one MainView
	private static boolean hasMainController	= false;
	private static boolean hasMainView			= false;
	
	// Needed annotations package names & packages paths
	private static String CONTROLLERS_FOLDER_PATH		= null;
	private static String VIEWS_FOLDER_PATH				= null;
	private final static String STATELESS_PACKAGE		= "javax.ejb.Stateless";
	private final static String STATEFUL_PACKAGE		= "javax.ejb.Stateful";
	private final static String REMOTE_PACKAGE			= "javax.ejb.Remote";
	private final static String LOCAL_PACKAGE			= "javax.ejb.Local";
	private final static String ENTITY_PACKAGE			= "javax.persistence.Entity";
	private final static String URL_PACKAGE				= "java.net.URL";
	private final static String RESOURCE_BUNDLE_PACKAGE = "java.util.ResourceBundle";
	
	/**
	 * SourceUtil constructor
	 * 
	 * Initializes lists for first check
	 */
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
	
	/**
	 * verifySource
	 * 
	 * Core of the verfication process
	 * 
	 * If it's the first annotated class, we begin to check
	 * expected views and controllers, in order to be able to
	 * couple every each (except controllers than can be alone).
	 * 
	 * A MainView and a MainController are expected
	 * 
	 * Verifications are based on package name and class naming.
	 * Some classes need more verifications such as Controllers.
	 * 
	 * @param mainClass 
	 */
	public void verifySource(ElementMapper mainClass)
	{
		String[] packageParts = mainClass.getPackageName().split("\\.");
		
		// First, get all views names and verify there is a MainView.fxml
		if (isFirstCheck)
		{
			verifyRoot(packageParts);
			
			buildControllersAndViewsFoldersPaths();
			
			try
			{
				getAllViewsNames();
				getAllControllersNames();
				
				for (String expectedControllerName : expectedControllersList)
					if (!initialControllersList.contains(expectedControllerName))
						throw new SourceControlException("Controller " + expectedControllerName + " was expected and wasn't found");
			}
			catch (ParserConfigurationException | SAXException | IOException ex)
			{
				throw new SourceControlException(ex);
			}
			
			isFirstCheck = false;
		}
		
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
	
	/**
	 * builControllersAndViewsFolderPaths
	 * 
	 * Get folder paths for controllers and views
	 * based on the expected path
	 */
	public void buildControllersAndViewsFoldersPaths()
	{	
		String pluginsRoot = System.getenv("RC2S_PLUGINS_ROOT");
		
		// Root is /[plugins-root]/[plugin-name]/[plugin-name]-client/src/main/
		StringBuilder sb = new StringBuilder();
		sb.append(pluginsRoot).append(File.separator)
			.append(pluginName).append(File.separator)
			.append(pluginName).append("-client").append(File.separator)
			.append("src").append(File.separator)
			.append("main").append(File.separator);
		
		// Controllers at /java/com/rc2s/[plugin-name]/controllers/
		CONTROLLERS_FOLDER_PATH = new StringBuilder(sb)
			.append("java").append(File.separator)
			.append("com").append(File.separator)
			.append("rc2s").append(File.separator)
			.append(pluginName).append(File.separator)
			.append("controllers").append(File.separator)
			.toString();
		
		// Views at /resources/views/
		VIEWS_FOLDER_PATH = new StringBuilder(sb)
			.append("resources").append(File.separator)
			.append("views").append(File.separator)
			.toString();
	}

	private void getAllViewsNames() throws SourceControlException, IOException, ParserConfigurationException, SAXException
	{
		File testDirectory = new File(VIEWS_FOLDER_PATH);
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
			Document fxmlDoc = docBuilder.parse(VIEWS_FOLDER_PATH + "/" + fileName);
			
			// Retrieve first AnchorPane Node element
			Node anchorNode = fxmlDoc.getElementsByTagName("AnchorPane").item(0);
			
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

	private void getAllControllersNames()
	{
		File testDirectory = new File(CONTROLLERS_FOLDER_PATH);
		File[] files = testDirectory.listFiles(
			(File pathname) -> pathname.getName().endsWith(".java") 
							&& pathname.isFile()
		);
		
		// Get all Views names
		for (File file : files)
			initialControllersList.add(file.getName());
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
				if (packageParts[4].equals("vo"))
					return ClassNamesEnum.VO;
				else
					throw new SourceControlException("Invalid common package naming - Expected (vo), got '" + packageParts[4] + "'");
				
			case "client":
				if (packageParts[4].equals("controllers"))
					return ClassNamesEnum.CONTROLLERS;
				else
					throw new SourceControlException("Invalid client package naming - Expected (controllers|views|css|images|utils), got '" + packageParts[4] + "'");
				
			default:
				throw new SourceControlException("Invalid package naming after plugin name - Expected (ejb|application|dao|common|client), got '" + packageParts[3] + "'");
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
	 * verifyClassStandards
	 * 
	 * pn.ejb.entityName			-> shall be 'NameFacadeRemote' & 'NameFacadeBean'
	 *								-> annotation @Stateless or @Stateful for 'NameFacadeBean' 
	 *								-> annotation @Remote for 'NameFacadeRemote'
	 *
	 * pn.application.entityName	-> shall be 'INameService' & 'NameService'
	 *								-> annotation @Stateless or @Stateful for 'NameService'
	 *								-> annotation @Local for 'INameService'
	 *
	 * pn.dao.entityName			-> shall be 'INameDAO' & 'NameDAO'
	 *								-> annotation @Stateless or @Stateful for 'NameDAO'
	 *								-> annotation @Local for 'INameDAO'
	 * 
	 * pn.common.vo					-> shall be 'Name' & annotation @Entity
	 * pn.common.sql				-> pluginname.sql - different analysis because it's a file and not a class
	 * 
	 * pn.client.controllers		-> shall be 'NameController' (shall have an initialize() method with parameters url & rb)
	 *								-> save controller's name to verify later whether he has a linked view
	 *								-> shall contain a 'MainController'
	 * pn.client.views				-> shall contain a 'MainView'
	 * pn.client.utils				-> void (utils for plugin creation, shall only have annotation @SourceControl)
	 * pn.client.css				-> void
	 * pn.client.images				-> void
	 * 
	 * @param mainClass
	 * @param cne
	 * @param entityName
	 * @throws SourceControlException 
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
	
				case CONTROLLERS:
					verifyControllerStandards(mainClass);
					break;
					
				default:
					break;
			}
		}
	}

	/**
	 * verifyEjbStandards
	 * 
	 * @param mainClass
	 * @param entityName
	 * @throws SourceControlException 
	 */
	private void verifyEjbStandards(ElementMapper mainClass, String entityName) throws SourceControlException
	{
		if (!checkClassIsUtilComponent(mainClass.getName()))
		{
			if (mainClass.getName().equals(entityName + "FacadeRemote"))
			{
				if (!mainClass.getAnnotations().contains(REMOTE_PACKAGE))
					throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + REMOTE_PACKAGE + "'");
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
	}

	/**
	 * verifyApplicationStandards
	 * 
	 * @param mainClass
	 * @param entityName
	 * @throws SourceControlException 
	 */
	private void verifyApplicationStandards(ElementMapper mainClass, String entityName) throws SourceControlException
	{
		if (!checkClassIsUtilComponent(mainClass.getName()))
		{
			if (mainClass.getName().equals("I" + entityName + "Service"))
			{
				if (!mainClass.getAnnotations().contains(LOCAL_PACKAGE))
					throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + LOCAL_PACKAGE + "'");
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
	}

	/**
	 * verifyDaoStandards
	 * 
	 * @param mainClass
	 * @param entityName
	 * @throws SourceControlException 
	 */
	private void verifyDaoStandards(ElementMapper mainClass, String entityName) throws SourceControlException
	{
		if (!checkClassIsUtilComponent(mainClass.getName()))
		{
			if (mainClass.getName().equals("I" + entityName + "DAO"))
			{
				if (!mainClass.getAnnotations().contains(LOCAL_PACKAGE))
					throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + LOCAL_PACKAGE + "'");
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
	}
	
	/**
	 * checkClassHasStatelessOrStatefulAnnotation
	 * @param mainClass
	 * @throws SourceControlException 
	 */
	private void checkClassHasStatelessOrStatefulAnnotation(ElementMapper mainClass) throws SourceControlException
	{
		Boolean hasStateless	= mainClass.getAnnotations().contains(STATELESS_PACKAGE);
		Boolean hasStateful		= mainClass.getAnnotations().contains(STATEFUL_PACKAGE);
		Boolean hasBoth		= hasStateless && hasStateful;
		Boolean hasNone		= !hasStateless && !hasStateful;

		if (hasBoth)
		{
			throw new SourceControlException("Class " + mainClass.getName() + 
				" has both " + STATELESS_PACKAGE + " & " + STATEFUL_PACKAGE + " annotations : it should only have one");
		}
		else if (hasNone)
		{
			throw new SourceControlException("Class " + mainClass.getName() + " requires " + STATELESS_PACKAGE + " or " + STATEFUL_PACKAGE + " annotation");
		}
	}
	
	private boolean checkClassIsUtilComponent(String className)
	{
		return (className.endsWith("Util") || className.endsWith("Utils"));
	}

	/**
	 * verifyVoStandards
	 * 
	 * @param mainClass
	 * @throws SourceControlException 
	 */
	private void verifyVoStandards(ElementMapper mainClass) throws SourceControlException
	{
		if (!checkClassIsUtilComponent(mainClass.getName()))
		{
			// Compile the regex
			Pattern p = Pattern.compile("(([A-Z]+)([a-z]*))+");
			// Create search engine
			Matcher m = p.matcher(mainClass.getName());
			// Search occurences
			Boolean b = m.matches();

			if (!b)
				throw new SourceControlException("Class " + mainClass.getName() + " shoul be CamelCase.");

			if (!mainClass.getAnnotations().contains(ENTITY_PACKAGE))
				throw new SourceControlException("Class " + mainClass.getName() + " should have annotation '" + ENTITY_PACKAGE + "'");
		}
	}

	/**
	 * verifyControllerStandards
	 * 
	 * @param mainClass
	 * @throws SourceControlException 
	 */
	private void verifyControllerStandards(ElementMapper mainClass) throws SourceControlException
	{
		if (!checkClassIsUtilComponent(mainClass.getName()))
		{
			// We shall find initialize(URL, ResourceBundle)
			boolean hasJavaNetURL = false;
			boolean hasResourceBundle = false;
			boolean hasInitializeMethod = false;

			String error = mainClass.getName() 
					+ "'s initialize method shall contain exactly 2 args : '" 
					+ URL_PACKAGE 
					+ "' & '" 
					+ RESOURCE_BUNDLE_PACKAGE 
					+ "'";

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
							if (pm.getType().equals(URL_PACKAGE))
								hasJavaNetURL = true;
							else if (pm.getType().equals(RESOURCE_BUNDLE_PACKAGE))
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
	}
}
