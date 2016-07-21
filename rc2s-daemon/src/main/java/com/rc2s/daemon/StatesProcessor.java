package com.rc2s.daemon;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.rc2s.daemon.network.Packet;
import com.rc2s.daemon.network.Stage;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

public class StatesProcessor implements Runnable
{
    private static final int RPI_GPIO_LIMIT = 9;

    private final Daemon daemon;
    private final LinkedBlockingQueue<Packet> queue;

    public StatesProcessor(final Daemon daemon)
    {
        this.daemon = daemon;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void add(final Packet packet)
    {
        queue.add(packet);
    }

    @Override
    public void run()
    {
        do
        {
			try
			{
				Packet packet = queue.remove();
				sweep(packet);

				daemon.getHardware().clear();
				daemon.getHardware().send();
			}
			catch(NoSuchElementException e) {}
        } while(daemon.isRunning());
    }

    private void sweep(final Packet packet)
    {
        long start = new Date().getTime();

        do
        {
            for (int i = 0; i < RPI_GPIO_LIMIT && i < packet.getStages().length; i++)
            {
                handle(i, packet.getStages().length, packet.getStages()[i]);
            }
            
            daemon.getHardware().resetState(false);
            
        } while ((packet.getDuration() > 0 && (new Date().getTime() - start < packet.getDuration()))
                || (packet.getDuration() <= 0 && queue.isEmpty()));
    }

    private void handle(final int level, final int maxStage, final Stage stage)
    {
        boolean[][] states = stage.getStates();

        for(int i = 0; i < states.length; i++)
        {
            for(int j = 0; j < states[i].length; j++)
            {
                GpioPinDigitalOutput gpdo = null;

                if(states[i][j])
                {
                    gpdo = daemon.getHardware().bit();
                }
				
                daemon.getHardware().shift(gpdo);
            }
        }

        daemon.getHardware().send();
        daemon.getHardware().sendStage(level, maxStage);
    }

    public void shutdown()
    {
        queue.clear();
    }
}
