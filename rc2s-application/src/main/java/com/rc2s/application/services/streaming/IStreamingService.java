package com.rc2s.application.services.streaming;

import com.rc2s.common.vo.Synchronization;

import javax.ejb.Local;

@Local
public interface IStreamingService
{
    public void start(final String mrl);
    
	public void stop();

	public void processCoordinates(final int[][] coordinates);

	public int[] getSyncSize();
    
	public Synchronization getSynchronization();
    
	public void setSynchronization(final Synchronization synchronization);
}
