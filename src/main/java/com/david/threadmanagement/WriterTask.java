package com.david.threadmanagement;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class WriterTask implements Runnable
{

	private Deque<Event> deque;

	public WriterTask(Deque<Event> deque)
	{
		super();
		this.deque = deque;
	}

	public Deque<Event> getDeque()
	{
		return deque;
	}

	public void setDeque(Deque<Event> deque)
	{
		this.deque = deque;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 100; i++)
		{
			Event event = new Event();
			event.setDate(new Date());
			event.setEvent(String.format("The thread %d generated the event", Thread.currentThread().getId()));
			System.err.println(String.format("The thread %d was added to the queue", Thread.currentThread().getId()));
			deque.addFirst(event);
			
			try
			{
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
