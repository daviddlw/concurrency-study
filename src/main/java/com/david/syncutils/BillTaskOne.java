package com.david.syncutils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class BillTaskOne implements Runnable
{
	private final CyclicBarrier barrier;

	public BillTaskOne(CyclicBarrier barrier)
	{
		super();
		this.barrier = barrier;
	}

	@Override
	public void run()
	{
		System.out.println("BillTaskOne will sleep for 5 seconds, it starts...");
		try
		{
			TimeUnit.SECONDS.sleep(5);
			System.out.println("Get BillTaskOne result, getNumberWaiting=> " + barrier.getNumberWaiting());
			barrier.await();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("BillTaskOne has finished...");
	}

}
