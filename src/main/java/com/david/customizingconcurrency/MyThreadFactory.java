package com.david.customizingconcurrency;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class MyThreadFactory implements ThreadFactory
{
	private String prefix;
	private int counter;

	public MyThreadFactory(String prefix)
	{
		super();
		this.prefix = prefix;
		counter = 1;
	}

	@Override
	public Thread newThread(Runnable r)
	{
		MyThread myThread = new MyThread(r, prefix + "-" + counter);
		counter++;
		return myThread;
	}

}

class MyTask implements Runnable
{

	@Override
	public void run()
	{
		try
		{
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
