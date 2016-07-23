package com.rc2s.common.client.utils;

import java.util.ArrayList;

public class ThreadPool
{
    private static ArrayList<Thread> threadPool = new ArrayList();
    
    public static void registerThread(Thread t)
    {
        ThreadPool.threadPool.add(t);
    }

    public static void interruptChildrenProcesses()
    {
        for (Thread t : ThreadPool.threadPool)
        {
            try
            {
                t.interrupt();
                t.join();
            } catch (InterruptedException e)
            {
            }
        }
    }

    public static void releaseThread(Thread t)
    {
        ThreadPool.threadPool.remove(t);
    }
}
