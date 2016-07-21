package com.rc2s.ejb.streaming;

import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import javax.ejb.Remote;

/**
 * StreamingFacadeRemote
 * 
 * EJB remote interface for Streaming EJB
 * 
 * @author RC2S
 */
@Remote
public interface StreamingFacadeRemote
{
    public void startStreaming(final User caller, final String mrl);
    
    public void stopStreaming(final User caller);
    
    public void setSynchronization(final Synchronization synchronization);
}
