package com.rc2s.application.services.streaming;

import com.rc2s.common.vo.Synchronization;

import javax.ejb.Local;

@Local
public interface IStreamingService
{
    public void start(String mrl);
	public void stop();

    public void musicAlgorithm();

	public Synchronization getSynchronization();
	public void setSynchronization(Synchronization synchronization);
}
