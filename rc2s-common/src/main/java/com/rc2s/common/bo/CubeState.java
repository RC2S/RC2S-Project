package com.rc2s.common.bo;

import java.io.Serializable;

public class CubeState implements Serializable
{
	private boolean[][][] states;

	public CubeState() {}

	public CubeState(boolean[][][] states)
	{
		this.states = states;
	}

	public void set(int x, int y, int z, boolean state)
	{
		this.states[x][y][z] = state;
	}

	public boolean get(int x, int y, int z)
	{
		return this.states[x][y][z];
	}

	public boolean[][][] getStates()
	{
		return states;
	}

	public void setStates(boolean[][][] states)
	{
		this.states = states;
	}
}
