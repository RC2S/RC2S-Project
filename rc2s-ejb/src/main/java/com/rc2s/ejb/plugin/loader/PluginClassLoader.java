package com.rc2s.ejb.plugin.loader;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader
{
    public PluginClassLoader(URL[] urls)
    {
        super(urls);
    }

    @Override
    protected void addURL(URL url)
    {
        super.addURL(url);
    }
}
