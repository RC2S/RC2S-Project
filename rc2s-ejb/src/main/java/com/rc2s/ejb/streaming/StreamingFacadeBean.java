package com.rc2s.ejb.streaming;

import com.rc2s.application.services.streaming.IStreamingService;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "StreamingEJB")
public class StreamingFacadeBean implements StreamingFacadeRemote
{
    @EJB
    private IStreamingService streamingService;
    
    @Override
    @RolesAllowed({"user"})
    public void startStreaming(final User caller, final String mrl)
    {
        streamingService.start(mrl);
    }

    @Override
    @RolesAllowed({"user"})
    public void stopStreaming(final User caller)
    {
        streamingService.stop();
    }

	@Override
    @RolesAllowed({"user"})
    public void setSynchronization(final Synchronization synchronization)
	{
		streamingService.setSynchronization(synchronization);
	}
}
