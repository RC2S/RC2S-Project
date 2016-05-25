package com.rc2s.application.services.plugin.loader;

import static com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler.BUFFER_SIZE;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.ejb.Stateless;

@Stateless
public class PluginLoaderService implements IPluginLoaderService
{
    @Override
    public void uploadPlugin()
    {
        
    }

    @Override
    public void unzipPlugin(String zipFile)
    {   	
        try
        {
            Path folderPath = Files.createTempDirectory("plugins");
            File folder = new File(folderPath.toString());
            
            if (!folder.exists())
                folder.mkdir();

            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry = zipIn.getNextEntry();
            
            while (entry != null)
            {
                String filePath = folderPath.toString() + File.separator + entry.getName();
                
                if (!entry.isDirectory())
                {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    
                    while ((read = zipIn.read(buffer)) != -1)
                        bos.write(buffer, 0, read);
 
                    bos.close();
                }
                else
                {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        }
        catch(IOException e)
        {
           e.printStackTrace(); 
        }
    }

    @Override
    public void checkServerPlugin()
    {
        
    }

    @Override
    public void checkClientPlugin()
    {
        
    }

    @Override
    public void deployServerPlugin()
    {
        // Files.copy(source, target, REPLACE_EXISTING);
        // System.getProperty("com.sun.aas.instanceRootURI")  <- payara41/glassfish/domains/rc2s-payara/
    }

    @Override
    public void deployClientPlugin()
    {
        // Files.copy(source, target, REPLACE_EXISTING);
    }
}
