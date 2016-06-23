package com.rc2s.daemon.hardware;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import java.util.HashMap;
import java.util.Map;

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

    private GPIOEnum getStageGpio(int level)
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

    public GpioPinDigitalOutput pulse(GPIOEnum pin)
    {
        return pulse(pin, true);
    }

    /**
     * Si revert = true l'état du pin est inversé 2 fois pour revenir à l'état initial
     * 
     * @param pin
     * @param revert
     * @return 
     */
    public GpioPinDigitalOutput pulse(GPIOEnum pin, boolean revert)
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

    public void sendStage(int i)
    {
        pulse(getStageGpio(i));
    }

    public GpioPinDigitalOutput bit()
    {
        return pulse(GPIOEnum.DATA_IN, false);
    }

    public void shift(GpioPinDigitalOutput gpdo)
    {
        pulse(GPIOEnum.CLOCK);

        if (gpdo != null)
            gpdo.low();
    }

    public void send()
    {
        pulse(GPIOEnum.LATCH);
    }

    public void clear()
    {
        pulse(GPIOEnum.CLEAR);
    }

    public void lock()
    {
        lock = pulse(GPIOEnum.BLANK, false);
    }

    public void unlock()
    {
        if(lock != null)
        {
            lock.low();
            lock = null;
        }
    }
    
    public final void resetState(boolean hardReset)
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