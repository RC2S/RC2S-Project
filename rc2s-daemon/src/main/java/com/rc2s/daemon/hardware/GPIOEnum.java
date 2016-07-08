package com.rc2s.daemon.hardware;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public enum GPIOEnum
{
    // GPIO i/o
    DATA_IN (RaspiPin.GPIO_00, "Data In"),
    CLOCK (RaspiPin.GPIO_01, "Clock"),
    LATCH (RaspiPin.GPIO_02, "Latch"),
    CLEAR (RaspiPin.GPIO_03, "Clear", true),
    BLANK (RaspiPin.GPIO_04, "Security GPIO"),
    // LED stages
    STAGE0 (RaspiPin.GPIO_21, "Stage 0 (floor)"),
    STAGE1 (RaspiPin.GPIO_22, "Stage 1"),
    STAGE2 (RaspiPin.GPIO_23, "Stage 2"),
    STAGE3 (RaspiPin.GPIO_24, "Stage 3"),
    STAGE4 (RaspiPin.GPIO_25, "Stage 4"),
    STAGE5 (RaspiPin.GPIO_26, "Stage 5"),
    STAGE6 (RaspiPin.GPIO_27, "Stage 6"),
    STAGE7 (RaspiPin.GPIO_28, "Stage 7"),
    STAGE8 (RaspiPin.GPIO_29, "Stage 8");

    private final Pin pin;
    private final String info;
    private final boolean invertedState;

    private GPIOEnum(final Pin pin, final String info)
    {
        this(pin, info, false);
    }
    
    private GPIOEnum(final Pin pin, final String info, final boolean invertedState)
    {
        this.pin = pin;
        this.info = info;
        this.invertedState = invertedState;
    }

    public Pin getPin()
    {
        return pin;
    }

    public String getInfo()
    {
        return info;
    }
    
    public boolean isInverted()
    {
        return invertedState;
    }
}
