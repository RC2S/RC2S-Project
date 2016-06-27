package com.rc2s.ejb.streaming;

import javax.ejb.Remote;

@Remote
public interface StreamingFacadeRemote
{
	public void streamMusic();

    public void startStreaming(String mrl);
    
    public void stopStreaming();
}
