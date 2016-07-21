package com.rc2s.ejb.streaming;

import com.rc2s.application.services.streaming.IStreamingService;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * StreamingFacadeBean
 * 
 * Steaming EJB, bridge to StreamingService
 * 
 * @author RC2S
 */
@Stateless(mappedName = "StreamingEJB")
public class StreamingFacadeBean implements StreamingFacadeRemote
{
    @EJB
    private IStreamingService streamingService;
    
	/**
	 * startStreaming
	 * 
	 * Start the streaming service on the given mrl
	 * 
	 * @param caller
	 * @param mrl 
	 */
    @Override
    @RolesAllowed({"user"})
    public void startStreaming(final User caller, final String mrl)
    {
        streamingService.start(mrl);
    }

	/**
	 * stopStreaming
	 * 
	 * Stops the streaming service if the given user is able to
	 * @param caller 
	 */
    @Override
    @RolesAllowed({"user"})
    public void stopStreaming(final User caller)
    {
        streamingService.stop();
    }

	/**
	 * setSynchronization
	 * 
	 * Sets a synchronization list used for this streaming
	 * 
	 * @param synchronization 
	 */
	@Override
    @RolesAllowed({"user"})
    public void setSynchronization(final Synchronization synchronization)
	{
		streamingService.setSynchronization(synchronization);
	}
}
