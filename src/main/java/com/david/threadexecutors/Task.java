package com.david.threadexecutors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.david.common.CommonUtils;

public class Task implements Runnable
{
	private String name;

	public Task(String name)
	{
		super();
		this.name = name;
	}

	@Override
	public void run()
	{
		System.out.println(String.format("%s, A thread has created at %s", Thread.currentThread().getName(),
				CommonUtils.sdf.format(new Date())));
		System.out.println(String.format("%s, A thread has started at %s", Thread.currentThread().getName(),
				CommonUtils.sdf.format(new Date())));
		Random r = new Random();
		int duration = r.nextInt(5);
		try
		{
			System.out.println(String.format("%s, A thread will sleep for %d seconds",
					Thread.currentThread().getName(), duration));
			TimeUnit.SECONDS.sleep(duration);

		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(String.format("%s, A thread has finished at %s", Thread.currentThread().getName(),
				CommonUtils.sdf.format(new Date())));
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
