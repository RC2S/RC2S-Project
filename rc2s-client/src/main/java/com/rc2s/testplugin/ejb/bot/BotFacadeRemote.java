package com.rc2s.testplugin.ejb.bot;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface BotFacadeRemote
{
    public List<String> getBots();
}
