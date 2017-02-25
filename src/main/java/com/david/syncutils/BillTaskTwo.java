package com.david.syncutils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class BillTaskTwo implements Runnable
{
	private final CyclicBarrier barrier;

	public BillTaskTwo(CyclicBarrier barrier)
	{
		super();
		this.barrier = barrier;
	}

	@Override
	public void run()
	{
		System.out.println("BillTaskTwo will sleep for 3 seconds, it starts...");

		try
		{
			TimeUnit.SECONDS.sleep(3);
			System.out.println("Get BillTaskTwo result, getNumberWaiting=> " + barrier.getNumberWaiting());
			barrier.await(); //参与者都需要到达的同步点
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("BillTaskTwo has finished...");

	}

}
