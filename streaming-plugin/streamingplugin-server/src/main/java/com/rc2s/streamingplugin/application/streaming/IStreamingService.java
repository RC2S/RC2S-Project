package com.rc2s.streamingplugin.application.streaming;

import com.rc2s.common.vo.Synchronization;
import com.rc2s.annotations.SourceControl;

import javax.ejb.Local;

/**
 * IStreamingService interface
 * 
 * Service interface for streaming via IDaemonService
 * 
 * @author RC2S
 */
@Local
//@SourceControl
public interface IStreamingService
{
    public void start(final String mrl);
    
	public void stop();

	public void processCoordinates(final int[][] coordinates);

	public int[] getSyncSize();
    
	public Synchronization getSynchronization();
    
	public void setSynchronization(final Synchronization synchronization);
}
