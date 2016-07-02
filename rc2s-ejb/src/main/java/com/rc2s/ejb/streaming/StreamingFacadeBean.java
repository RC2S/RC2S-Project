package com.rc2s.ejb.streaming;

import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.application.services.streaming.IStreamingService;
import com.rc2s.common.vo.User;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "StreamingEJB")
@Interceptors(SecurityInterceptor.class)
public class StreamingFacadeBean implements StreamingFacadeRemote
{
	@EJB
	private IStreamingService streamingService;
	
	public void streamMusic(User caller)
	{
		streamingService.streamMusic();
	}
}
