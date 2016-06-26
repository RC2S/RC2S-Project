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

    public StatesProcessor(Daemon daemon)
    {
        this.daemon = daemon;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void add(Packet packet)
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
				try
				{
					Packet packet = queue.remove();
					sweep(packet);

					daemon.getHardware().clear();
					daemon.getHardware().send();
				}
				catch(NoSuchElementException e) {}
				
				Thread.sleep(50l);
			}
			catch(InterruptedException e) {}
        } while(daemon.isRunning());
    }

    private void sweep(Packet packet)
    {
        long start = new Date().getTime();

        do
        {
            for (int i = 0; i < RPI_GPIO_LIMIT && i < packet.getStages().length; i++)
            {
                handle(i, packet.getStages().length, packet.getStages()[i]);
            }
        } while ((packet.getDuration() > 0 && (new Date().getTime() - start < packet.getDuration()))
                || (packet.getDuration() <= 0 && queue.isEmpty()));
    }

    private void handle(int level, int maxStage, Stage stage)
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
                    System.out.print("1 ");
                }
				else
					System.out.print("0 ");
				
                daemon.getHardware().shift(gpdo);
            }
			System.out.println("");
        }

        daemon.getHardware().send();
        daemon.getHardware().sendStage(level, maxStage);
    }

    public void shutdown()
    {
        queue.clear();
    }
}
