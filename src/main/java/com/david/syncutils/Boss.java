package com.david.syncutils;

import java.util.concurrent.CountDownLatch;

public class Boss implements Runnable
{
	private final CountDownLatch latch;
	private boolean isUseCountDownLatch;

	public Boss(CountDownLatch latch, boolean isUseCountDownLatch)
	{
		super();
		this.latch = latch;
		this.isUseCountDownLatch = isUseCountDownLatch;
	}

	@Override
	public void run()
	{
		System.out.println("等待所有工人把活干完...");

		try
		{
			if (isUseCountDownLatch)
			{
				latch.await();
			}
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("所有工作已经被完成，开始检查工作成果...");

	}

}
