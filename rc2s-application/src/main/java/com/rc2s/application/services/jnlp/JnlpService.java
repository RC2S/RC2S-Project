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
import java.net.URISyntaxException;
import java.nio.file.Paths;

@Stateless
public class JnlpService implements IJnlpService
{
    @Inject
    private Logger log;
    
    private final String jnlpFilePath = System.getProperty("com.sun.aas.instanceRootURI") + "applications" + File.separator + "rc2s-jnlp" + File.separator + "rc2s-client.jnlp";
    
    private final String jnlpLibsFolder = "libs" + File.separator;
    
    private final String jarSignerPath = System.getenv("JAVA_HOME") + File.separator + "bin" + File.separator + "jarsigner" + (System.getProperty("os.name").contains("Windows") ? ".exe" : "");
    
    private final String signKeyStore = JnlpService.class.getResource("/RC2S.jks").getPath();
    
    private final String signStorePass = "P@ssword1234";
    
    private final String signAlias = "RC2S";
    
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
            StreamResult result     = new StreamResult(Paths.get(jnlpFilePath.substring(jnlpFilePath.indexOf("file:") + 5)).toUri().toASCIIString());

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
