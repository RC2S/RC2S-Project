package com.rc2s.application.services.plugin.loader;

import javax.ejb.Local;

@Local
public interface IPluginLoaderService
{
    public void uploadPlugin();
    
    public void unzipPlugin();
    
    public void checkServerPlugin();
    
    public void checkClientPlugin();
    
    public void deployServerPlugin();
    
    public void deployClientPlugin();
}
