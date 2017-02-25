package com.david.concurrentcollections;

import java.util.concurrent.ConcurrentLinkedDeque;

public class AddTask implements Runnable
{
	private ConcurrentLinkedDeque<String> list;
	private String name;

	public AddTask(String name, ConcurrentLinkedDeque<String> list)
	{
		super();
		this.name = name;
		this.list = list;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 10000; i++)
		{
			list.add(String.format("%s-%d", name, i));
		}
	}
}
