package com.david.syncutils;

import java.util.concurrent.CountDownLatch;

public class VideoConference implements Runnable
{
	private final CountDownLatch latch;

	public VideoConference(int number)
	{
		super();
		latch = new CountDownLatch(number);
	}

	public CountDownLatch getLatch()
	{
		return latch;
	}

	public void arrive(String name)
	{
		String info = String.format("%s has arrived...", name);
		System.out.println(info);
		latch.countDown();
	}

	@Override
	public void run()
	{
		System.out.println("Current count: " + latch.getCount());
		try
		{
			latch.await();
			System.out.println("All the participate has arrived...");
			System.out.println("Let's begin...");
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
