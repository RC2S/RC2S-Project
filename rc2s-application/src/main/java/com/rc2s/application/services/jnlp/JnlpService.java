package com.rc2s.application.services.jnlp;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * GroupService
 * 
 * Service for group (jaas) retrieving
 * Uses an IGroupDAO for db access
 * 
 * @author RC2S
 */
@Stateless
public class JnlpService implements IJnlpService
{
    @Inject
    private Logger log;
    
    private final String jnlpFilePath = System.getProperty("com.sun.aas.instanceRootURI") + "applications" + File.separator + "rc2s-jnlp" + File.separator + "rc2s-client.jnlp";
    
    private final String jnlpLibsFolder = "libs/";
    
    private final String jarSignerPath = System.getenv("JAVA_HOME") + File.separator + "bin" + File.separator + "jarsigner" + (System.getProperty("os.name").contains("Windows") ? ".exe" : "");
    
    private final String signKeyStore = JnlpService.class.getResource("/RC2S.jks").getPath();
    
    private final String signStorePass = "P@ssword1234";
    
    private final String signAlias = "RC2S";
    
	/**
	 * signJar(jarPath)
	 * 
	 * Signs the jars in jarPath
	 * Used in gradle task sign
	 * 
	 * @param jarPath 
	 */
    @Override
    public void signJar(final String jarPath)
    {        
        Process p;
        
        String args[] = {
            jarSignerPath,
            "-keystore", signKeyStore,
            "-storepass", signStorePass,
            jarPath,
            signAlias
        };
        
        StringBuilder output = new StringBuilder();
        
		try
        {
			p = Runtime.getRuntime().exec(args);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            
			while ((line = reader.readLine())!= null)
				output.append(line + "\n");
		}
        catch (IOException | InterruptedException e)
        {
			log.error(e);
		}
        
        log.info(output.toString());
    }

	/**
	 * updateJNLP(jarName, removeJar)
	 * 
	 * Update the jnlp from jarName
	 * If the jar is not removed, then it is
	 * added in the Jnlp resources
	 * 
	 * @param jarName
	 * @param removeJar 
	 */
    @Override
    public void updateJNLP(final String jarName, final boolean removeJar)
    {
        try
        {
            // Open xml file
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(jnlpFilePath);
            
            Node resources = doc.getElementsByTagName("resources").item(0);
            
            // Search existing jarName
            NodeList searchJar = doc.getElementsByTagName("jar");
            for (int i = 0; i < searchJar.getLength(); i++)
            {
                Element element = (Element) searchJar.item(i);
                if (element.getAttribute("href").equals(jnlpLibsFolder + jarName))
                {
                    if (removeJar)
                        element.getParentNode().removeChild(element);
                    else
                        return;
                }
            }
            
            // Add new Jar in Jnlp resources
            if (!removeJar)
            {
                Element newJar = doc.createElement("jar");
                
                newJar.setAttribute("href", jnlpLibsFolder + jarName);
                resources.appendChild(newJar);
            }
            
            // Save changes
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source        = new DOMSource(doc);

			String filePath 		= jnlpFilePath.substring(jnlpFilePath.indexOf("file:") + 5);
			if(System.getProperty("os.name").toLowerCase().contains("windows"))
				filePath 			= filePath.substring(1); // Remove leading slash on Windows

            StreamResult result     = new StreamResult(Paths.get(filePath).toUri().toASCIIString());

            transformer.transform(source, result);
        }
        catch (ParserConfigurationException
              | SAXException
              | IOException
              | TransformerException e)
        {
            log.error(e);
        }
    }   
}
