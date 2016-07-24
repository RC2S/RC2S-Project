package com.rc2s.streamingplugin.ejb.streaming;

import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import com.rc2s.annotations.SourceControl;
import javax.ejb.Remote;

/**
 * StreamingFacadeRemote
 * 
 * EJB remote interface for Streaming EJB
 * 
 * @author RC2S
 */
@Remote
@SourceControl
public interface StreamingFacadeRemote
{
    public void startStreaming(final String mrl);
    
    public void stopStreaming();
    
    public void setSynchronization(final Synchronization synchronization);
}
