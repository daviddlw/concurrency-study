package com.david.syncutils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class BillTaskThree implements Runnable
{
	private final CyclicBarrier barrier;

	public BillTaskThree(CyclicBarrier barrier)
	{
		super();
		this.barrier = barrier;
	}

	@Override
	public void run()
	{
		System.out.println("BillTaskThree will sleep for 6 seconds, it starts...");

		try
		{
			TimeUnit.SECONDS.sleep(6);
			System.out.println("Get BillTaskThree result, getNumberWaiting=> " + barrier.getNumberWaiting());
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
		System.out.println("BillTaskThree has finished...");
	}

}
