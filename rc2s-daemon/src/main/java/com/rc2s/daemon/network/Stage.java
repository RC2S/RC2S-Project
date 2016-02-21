package com.rc2s.daemon.network;

public class Stage
{
	private boolean[][] states;

	public Stage(boolean[][] states)
	{
		this.states = states;
	}
	
	public boolean[][] getStates()
	{
		return states;
	}

	public void setStates(boolean[][] states)
	{
		this.states = states;
	}
}
