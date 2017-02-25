package com.david.syncutils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Worker implements Runnable
{
	private String name;
	private final CountDownLatch latch;
	private final CyclicBarrier barrier;
	private boolean isUseCountDownLatch;

	public Worker(String name, CountDownLatch latch, CyclicBarrier barrier, boolean isUseCountDownLatch)
	{
		super();
		this.name = name;
		this.latch = latch;
		this.barrier = barrier;
		this.isUseCountDownLatch = isUseCountDownLatch;
	}

	@Override
	public void run()
	{
		System.out.println(String.format("%s： %s 已经完成了工作...", Thread.currentThread().getName(), name));
		if (isUseCountDownLatch)
		{
			latch.countDown();
		}else {
			try
			{
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
