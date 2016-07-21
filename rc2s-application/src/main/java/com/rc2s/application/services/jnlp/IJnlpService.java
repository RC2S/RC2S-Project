package com.rc2s.application.services.jnlp;

import javax.ejb.Local;

/**
 * IJnlpService interface
 * 
 * Service interface for the jnlp
 * Used to sign the jars & update the jnlp
 * 
 * @author RC2S
 */
@Local
public interface IJnlpService
{
    public void signJar(final String jarPath);
    
    public void updateJNLP(final String jarName, final boolean removeJar);
}
