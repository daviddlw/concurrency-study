package com.david.concurrentcollections;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomTask implements Runnable
{

	public ThreadLocalRandomTask()
	{
		super();
		ThreadLocalRandom.current();
	}

	@Override
	public void run()
	{
		String name = Thread.currentThread().getName();
		String message = String.format("%s - generate the random number: %d", name, ThreadLocalRandom.current().nextInt(10));
		for (int i = 0; i < 10; i++)
		{
			System.out.println(message);
		}
	}

}
