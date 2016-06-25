package com.rc2s.ejb.streaming;

import javax.ejb.Remote;

@Remote
public interface StreamingFacadeRemote
{
	public void streamMusic();
}
