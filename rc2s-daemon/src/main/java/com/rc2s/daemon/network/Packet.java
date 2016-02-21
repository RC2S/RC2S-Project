package com.rc2s.daemon.network;

import java.io.Serializable;

public class Packet implements Serializable
{
	private long duration;
	private Stage[] stages;

	public Packet(long duration, Stage[] states)
	{
		this.duration = duration;
		this.stages = states;
	}
	
	public long getDuration()
	{
		return duration;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	public Stage[] getStages()
	{
		return stages;
	}

	public void setStages(Stage[] stages)
	{
		this.stages = stages;
	}
}
