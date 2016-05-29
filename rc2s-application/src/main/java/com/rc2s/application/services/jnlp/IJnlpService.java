package com.rc2s.application.services.jnlp;

import javax.ejb.Local;

@Local
public interface IJnlpService
{
    public void signJar(String jarPath);
    
    public void updateJNLP(String jarName, boolean removeJar);
}
