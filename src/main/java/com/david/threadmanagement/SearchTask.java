package com.david.threadmanagement;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SearchTask implements Runnable
{
	private Result result;

	public SearchTask(Result result)
	{
		super();
		this.result = result;
	}

	@Override
	public void run()
	{
		String name = Thread.currentThread().getName();
		System.out.println(String.format("Thread: %s has started...", name));
		try
		{
			doTask();
			result.setName(name);
		} catch (InterruptedException e)
		{
			System.err.println(String.format("Thread %s has interrupted...", name));
			return;
		}
		System.out.println(String.format("Thread: %s has ended...", name));
	}

	private void doTask() throws InterruptedException
	{
		Random random = new Random(new Date().getTime());
		int value = (int) (random.nextDouble() * 100);
		System.out.println(String.format("Thread %s: %d", Thread.currentThread().getName(), value));
		TimeUnit.SECONDS.sleep(value);
	}

}
