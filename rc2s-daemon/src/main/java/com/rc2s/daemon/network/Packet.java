package com.rc2s.daemon.network;

import java.io.Serializable;

public class Packet implements Serializable
{
    private long duration;
    private Stage[] stages;

    public Packet(final long duration, final Stage[] states)
    {
        this.duration = duration;
        this.stages = states;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(final long duration)
    {
        this.duration = duration;
    }

    public Stage[] getStages()
    {
        return stages;
    }

    public void setStages(final Stage[] stages)
    {
        this.stages = stages;
    }
}
