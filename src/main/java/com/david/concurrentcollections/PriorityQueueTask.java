package com.david.concurrentcollections;

import java.util.concurrent.PriorityBlockingQueue;

public class PriorityQueueTask implements Runnable
{
	private int id;

	private PriorityBlockingQueue<Event> queue;

	public PriorityQueueTask(int id, PriorityBlockingQueue<Event> queue)
	{
		super();
		this.id = id;
		this.queue = queue;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 1000; i++)
		{
			queue.add(new Event(id, i));
		}
	}

}
