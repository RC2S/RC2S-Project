package com.rc2s.daemon;

import com.rc2s.daemon.hardware.Hardware;
import com.rc2s.daemon.network.Packet;
import com.rc2s.daemon.network.Stage;

public class Daemon implements Runnable
{
    private final Hardware hardware;
    private final StatesProcessor processor;

    private boolean running;

    public static void main(String args[])
    {
            Daemon d = new Daemon();

            d.run();
            d.shutdown();
    }

    public Daemon()
    {		
            this.hardware = new Hardware();
            this.processor = new StatesProcessor(this);
    }

    @Override
    public void run()
    {
        this.running = true;


        Stage s1 = new Stage(new boolean[][] {{true, false, false, false}, {false, false, false, false}, {false, false, false, false}, {false, false, false, false}});
        Stage s2 = new Stage(new boolean[][] {{false, false, true, false}, {false, false, false, false}, {false, false, false, false}, {false, false, false, false}});
        Stage s3 = new Stage(new boolean[][] {{false, false, false, false}, {true, false, false, false}, {false, false, false, false}, {false, false, false, false}});
        Stage s4 = new Stage(new boolean[][] {{false, false, false, false}, {false, false, true, false}, {false, false, false, false}, {false, false, false, false}});
        Stage s5 = new Stage(new boolean[][] {{false, false, false, false}, {false, false, false, false}, {true, false, false, false}, {false, false, false, false}});
        Stage s6 = new Stage(new boolean[][] {{false, false, false, false}, {false, false, false, false}, {false, false, true, false}, {false, false, false, false}});
        Stage s7 = new Stage(new boolean[][] {{false, false, false, false}, {false, false, false, false}, {false, false, false, false}, {true, false, false, false}});
        Stage s8 = new Stage(new boolean[][] {{false, false, false, false}, {false, false, false, false}, {false, false, false, false}, {false, false, true, false}});
        Stage s9 = new Stage(new boolean[][] {{false, false, false, false}, {false, false, false, false}, {false, false, false, false}, {false, false, false, true}});

        processor.add(new Packet(10000l, new Stage[] {s1, s2, s3, s4, s5, s6, s7, s8, s9}));

        processor.run();

        /*GpioPinDigitalOutput gpdo = hardware.bit();
        hardware.shift(gpdo);
        gpdo = hardware.bit();
        hardware.shift(gpdo);
        hardware.send();

        try
        {
                Thread.sleep(3000l);
        } catch (InterruptedException ex) {}

        gpdo = hardware.bit();
        hardware.shift(gpdo);
        gpdo = hardware.bit();
        hardware.shift(gpdo);
        gpdo = hardware.bit();
        hardware.shift(gpdo);
        gpdo = hardware.bit();
        hardware.shift(gpdo);
        hardware.clear();
        hardware.send();

        try
        {
                Thread.sleep(3000l);
        } catch (InterruptedException ex) {}

        gpdo = hardware.bit();
        hardware.shift(gpdo);
        gpdo = hardware.bit();
        hardware.shift(gpdo);
        hardware.send();*/

        /*try
        {
                Thread.sleep(99999999999L);
        } catch (InterruptedException ex) {}*/
    }

    public boolean isRunning()
    {
        return running;
    }

    public Hardware getHardware()
    {
        return hardware;
    }

    public void shutdown()
    {
        this.running = false;

        processor.shutdown();
        hardware.shutdown();
    }
}
