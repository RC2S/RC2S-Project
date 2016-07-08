package com.rc2s.ejb.streaming;

import com.rc2s.application.services.streaming.IStreamingService;
import com.rc2s.application.services.authentication.SecurityInterceptor;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "StreamingEJB")
//@Interceptors(SecurityInterceptor.class)
public class StreamingFacadeBean implements StreamingFacadeRemote
{
    @EJB
    private IStreamingService streamingService;
    
    @Override
    public void startStreaming(final User caller, final String mrl)
    {
        streamingService.start(mrl);
    }

    @Override
    public void stopStreaming(final User caller)
    {
        streamingService.stop();
    }

	@Override
    public void setSynchronization(final Synchronization synchronization)
	{
		streamingService.setSynchronization(synchronization);
	}
}
