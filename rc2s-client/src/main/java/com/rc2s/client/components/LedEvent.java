package com.rc2s.client.components;

public abstract class LedEvent implements Runnable
{
	private Led led;
	
	@Override
	public abstract void run();

	public Led getLed()
	{
		return led;
	}

	public void setLed(Led led)
	{
		this.led = led;
	}
}
