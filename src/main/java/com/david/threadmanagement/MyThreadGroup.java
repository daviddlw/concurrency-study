package com.david.threadmanagement;

import java.util.Random;

import org.apache.log4j.Logger;

public class MyThreadGroup extends ThreadGroup
{
	private Logger logger = Logger.getLogger(MyThreadGroup.class);

	public MyThreadGroup(String name)
	{
		super(name);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		logger.error(String.format("The thread - %d has thrown an Exception...", t.getId()));
		logger.error(e.getMessage(), e);
		logger.error("Terminating the rest of the thread.");
	}
}

class MyThreadGroupTask implements Runnable
{

	@Override
	public void run()
	{
		int result = 0;
		Random rand = new Random(Thread.currentThread().getId());
		while (true)
		{
			result = 1000 / ((int) rand.nextDouble() * 1000);
			System.out.println(String.format("Thread - %d : %d", Thread.currentThread().getId(), result));
			if (Thread.currentThread().isInterrupted())
			{
				System.err.println(String.format("Thread - %d: Interrupted...", Thread.currentThread().getId()));
				return;
			}
		}
	}

}
