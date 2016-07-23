package com.rc2s.streamingplugin.application.streaming;

import com.rc2s.annotations.SourceControl;

/**
 * AlgoEffectUtils
 * 
 * Association key/value to get the desired effect for the algorithm
 * The value represents the number of LEDs that will be lightened
 * 
 * @author RC2S
 */
//@SourceControl
public enum AlgoEffectUtils
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

	private final int size;
	
	private AlgoEffectUtils(final int size)
	{
		this.size = size;
	}
	
	public int getSize()
	{
		return size;
	}
}
