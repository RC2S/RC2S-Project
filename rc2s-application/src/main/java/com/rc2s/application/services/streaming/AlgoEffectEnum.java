package com.rc2s.application.services.streaming;

public enum AlgoEffectEnum
{
	// With non-absolute point
	CLASSIC(2),
	
	// Apply mirror on absolute point
	MIRROR_WIDTH(2),
	MIRROR_DEPTH(2),
	MIRROR_HEIGHT(2),
	
	// Apply all mirrors on absolute point
	MIRROR_TRIPLE(4),
	
	// Triple Symettry
	EIGHTIFY(8);

	private int size;
	
	private AlgoEffectEnum(final int size)
	{
		this.size = size;
	}
	
	public int getSize()
	{
		return size;
	}
}
