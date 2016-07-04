package com.rc2s.ejb.streaming;

import com.rc2s.common.vo.User;
import javax.ejb.Remote;

@Remote
public interface StreamingFacadeRemote
{
    public void startStreaming(User caller, String mrl);
    public void stopStreaming(User caller);
}
