package com.rc2s.daemon.hardware;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import java.util.HashMap;
import java.util.Map;

/**
 * Hardware
 * 
 * Class used for GPIO manipulation
 * GPIOs need specific informations process in order to send data
 * 
 * @author RC2S
 */
public class Hardware
{
    // Create a unique instance of GpioController
    private final GpioController gpioc;
    private final Map<GPIOEnum, GpioPinDigitalOutput> mGpio;

    private GpioPinDigitalOutput lock;

    public Hardware()
    {
        this.gpioc = GpioFactory.getInstance();
        this.mGpio = new HashMap<>();
        
        resetState(false);
    }

	/**
	 * getStageGpio
	 * 
	 * Retrieve GPIO for a specific stage
	 * 
	 * @param level
	 * @return GPIOEnum the gpio for that stage 
	 */
    private GPIOEnum getStageGpio(final int level)
    {
        switch (level)
        {
            case 0: return GPIOEnum.STAGE0;
            case 1: return GPIOEnum.STAGE1;
            case 2: return GPIOEnum.STAGE2;
            case 3: return GPIOEnum.STAGE3;
            case 4: return GPIOEnum.STAGE4;
            case 5: return GPIOEnum.STAGE5;
            case 6: return GPIOEnum.STAGE6;
            case 7: return GPIOEnum.STAGE7;
            case 8: return GPIOEnum.STAGE8;
            default: return null;
        }
    }

	/**
	 * pulse
	 * 
	 * Change the gpio state
	 * 0 -> 1 | 1 -> 0
	 * 
	 * @param pin
	 * @return 
	 */
    public GpioPinDigitalOutput pulse(final GPIOEnum pin)
    {
        return pulse(pin, true);
    }

    /**
	 * pulse
	 * 
	 * If revert is true, the pin state is reversed
	 * two times to come back to initial state
     * 
     * @param pin
     * @param revert
     * @return 
     */
    public GpioPinDigitalOutput pulse(final GPIOEnum pin, final boolean revert)
    {
        GpioPinDigitalOutput gpdo = mGpio.get(pin);
        
        if (gpdo != null)
        {
            gpdo.toggle();

            if (revert)
                gpdo.toggle();
        }

        return gpdo;
    }

    public void sendStage(final int i, final int maxStage)
    {
        if(i == 0)
            mGpio.get(getStageGpio(i)).high();
        else
        {
            mGpio.get(getStageGpio(i - 1)).low();
            
            mGpio.get(getStageGpio(i)).high();
        }
    }

    public GpioPinDigitalOutput bit()
    {
        return pulse(GPIOEnum.DATA_IN, false);
    }

	/**
	 * shift
	 * 
	 * Lower the given pin
	 * 
	 * @param gpdo 
	 */
    public void shift(GpioPinDigitalOutput gpdo)
    {
        pulse(GPIOEnum.CLOCK);

        if (gpdo != null)
            gpdo.low();
    }

	/**
	 * send
	 * 
	 * Sends the buffer
	 * The buffer is free, if there where datas in it,
	 * leds are lightened
	 */
    public void send()
    {
        pulse(GPIOEnum.LATCH);
    }

	/**
	 * clear
	 * 
	 * Clear the action put in buffer
	 */
    public void clear()
    {
        pulse(GPIOEnum.CLEAR);
    }

	/**
	 * lock
	 * 
	 * Lock the buffer for future actions
	 */
    public void lock()
    {
        lock = pulse(GPIOEnum.BLANK, false);
    }

	/**
	 * unlock
	 * 
	 * Unlocks the buffer
	 */
    public void unlock()
    {
        if(lock != null)
        {
            lock.low();
            lock = null;
        }
    }
    
    public final void resetState(final boolean hardReset)
    {        
        for(GPIOEnum pin : GPIOEnum.values())
        {
            GpioPinDigitalOutput gpdo = mGpio.get(pin);
            
            if (gpdo == null)
            {
                gpdo = gpioc.provisionDigitalOutputPin(pin.getPin(), pin.getInfo(), PinState.LOW);
                gpdo.setShutdownOptions(true, PinState.LOW);
                mGpio.put(pin, gpdo);
                
                if (pin.isInverted())
                    gpdo.high();
            }
            else
            {
                if (hardReset || !pin.isInverted())
                    gpdo.low();
                else
                    gpdo.high();
            }                
        }
		
        send();
    }
	
    public void shutdown()
    {
        resetState(true);        
        gpioc.shutdown();
    }
}