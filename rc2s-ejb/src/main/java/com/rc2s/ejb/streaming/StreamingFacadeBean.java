package com.rc2s.ejb.streaming;

import com.rc2s.application.services.streaming.IStreamingService;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

@Stateless(mappedName = "StreamingEJB")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class StreamingFacadeBean implements StreamingFacadeRemote
{
    @Autowired
    private IStreamingService streamingService;
    
    @Override
    public void startStreaming(String mrl)
    {
        streamingService.start(mrl);
    }

    @Override
    public void stopStreaming()
    {
        streamingService.stop();
    }
}
