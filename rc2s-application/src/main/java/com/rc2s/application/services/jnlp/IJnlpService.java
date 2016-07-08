package com.rc2s.application.services.jnlp;

import javax.ejb.Local;

@Local
public interface IJnlpService
{
    public void signJar(final String jarPath);
    
    public void updateJNLP(final String jarName, final boolean removeJar);
}
