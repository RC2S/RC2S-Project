package com.rc2s.ejb.streaming;

import com.rc2s.application.services.streaming.IStreamingService;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "StreamingEJB")
public class StreamingFacadeBean implements StreamingFacadeRemote
{
	@EJB
	private IStreamingService streamingService;
	
	public void streamMusic()
	{
		streamingService.streamMusic();
	}
}
