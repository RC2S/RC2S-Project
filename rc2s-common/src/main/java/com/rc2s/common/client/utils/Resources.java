package com.rc2s.common.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class Resources
{
    private static String viewsPackage          = "/views/";
    private static String cssPackage            = "/css/";
    private static String resourcesPackage      = "/resources/";
    
    public static void setViewsPackage(final String pkg)
    {
        viewsPackage = setPackage(pkg);
    }
    
    public static void setCssPackage(final String pkg)
    {
        cssPackage = setPackage(pkg);
    }
    
    public static void setResourcesPackage(final String pkg)
    {
        resourcesPackage = setPackage(pkg);
    }
    
    private static String setPackage(final String pkg)
    {
        String newPkg = pkg.replace(".", "/");

        if(!newPkg.startsWith("/"))
            newPkg = "/".concat(pkg);
        if(!newPkg.endsWith("/"))
            newPkg = newPkg.concat("/");

        return newPkg;
    }
    
    public static URL getResource(final String name)
    {       
        return Resources.class.getResource(name);
    }
    
    public static InputStream getStreamResource(final String name)
    {
        return Resources.class.getResourceAsStream(name);
    }
    
    public static boolean resourceExists(final String name)
    {
        return getResource(name) != null;
    }
    
    public static FXMLLoader loadFxml(final String fxml)
    {
        StringBuilder   fxmlFile;
        FXMLLoader      loader;
		
        fxmlFile = new StringBuilder();
        
        if(!fxml.contains(".fxml"))
            fxmlFile.append(viewsPackage).append(fxml).append(".fxml");   
        else
            fxmlFile.append(fxml);
        
        loader = new FXMLLoader();
		
        try
        {
			if(resourceExists(fxmlFile.toString()))
			{
				loader.setLocation(getResource(fxmlFile.toString()));
				loader.load(getResource(fxmlFile.toString()).openStream());

				return loader;
			}
			else
				return null;
        }
        catch(IOException e)
        {
            throw new IllegalStateException("Cannot load FXML file", e);
        }
    }
    
    public static void loadCss(final Scene scene, final String css)
    {
        StringBuilder cssFile = new StringBuilder();
        
        if(!css.contains(".css"))
            cssFile.append(cssPackage).append(css).append(".css");   
        else
            cssFile.append(css);
        
        if(resourceExists(cssFile.toString()))
            scene.getStylesheets().add(getResource(cssFile.toString()).toExternalForm());
    }
    
    public static InputStream loadResource(final String file)
    {
        if(resourceExists(resourcesPackage + file))
            return getStreamResource(resourcesPackage + file);
        else
            return null;
    }
}
