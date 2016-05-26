package com.rc2s.ejb.streaming;

import com.rc2s.application.services.streaming.IStreamingService;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "StreamingEJB")
public class StreamingFacadeBean implements StreamingFacadeRemote
{
    @EJB
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
