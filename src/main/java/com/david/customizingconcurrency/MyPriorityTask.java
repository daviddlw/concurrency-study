package com.david.customizingconcurrency;

import java.util.concurrent.TimeUnit;

public class MyPriorityTask implements Runnable, Comparable<MyPriorityTask>
{
	private String name;
	private int priority;

	public MyPriorityTask(String name, int priority)
	{
		super();
		this.name = name;
		this.priority = priority;
	}

	@Override
	public void run()
	{
		System.out.println(String.format("MyPriorityTask: %s, Priority： %d", name, priority));
		try
		{
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(MyPriorityTask o)
	{
		if (this.priority < o.priority)
		{
			return 1;
		} else if (this.priority > o.priority)
		{
			return -1;
		} else
		{
			return 0;
		}
	}

}
