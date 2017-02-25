package com.david.syncutils;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue
{
	private final Semaphore semaphore;
	private boolean[] freePrinters;
	private Lock lockPrinters;

	public PrintQueue()
	{
		super();
		semaphore = new Semaphore(3, true);
		freePrinters = new boolean[3];

		for (int i = 0; i < 3; i++)
		{
			freePrinters[i] = true;
		}

		lockPrinters = new ReentrantLock();
	}

	public void printJob(Object doc)
	{
		try
		{
			semaphore.acquire();
			Random r = new Random();
			int duration = r.nextInt(5);
			int pIndex = getPrinter();
			String info = String.format("Thread: %s, print document with printer- %d, - %s, sleep for %d",
					Thread.currentThread().getName(), pIndex, doc, duration);
			System.out.println(info);
			TimeUnit.SECONDS.sleep(duration);

			freePrinters[pIndex] = true;

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			semaphore.release();
		}
	}

	private int getPrinter()
	{
		int result = 0;
		lockPrinters.lock();
		try
		{
			for (int i = 0; i < freePrinters.length; i++)
			{
				if (freePrinters[i])
				{
					result = i;
					freePrinters[i] = false;
					break;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			lockPrinters.unlock();
		}

		return result;
	}

}
