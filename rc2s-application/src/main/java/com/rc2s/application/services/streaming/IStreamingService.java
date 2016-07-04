package com.rc2s.application.services.streaming;

import javax.ejb.Local;

@Local
public interface IStreamingService
{
    public void start(String mrl);
	public void resume();
	public void pause();
	public void stop();

    public void musicAlgorithm();
}
