package com.rc2s.ejb.streaming;

import com.rc2s.application.services.streaming.IStreamingService;
import com.rc2s.application.services.authentication.SecurityInterceptor;
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
    public void startStreaming(User caller, String mrl)
    {
        streamingService.start(mrl);
    }

    @Override
    public void stopStreaming(User caller)
    {
        streamingService.stop();
    }
}
