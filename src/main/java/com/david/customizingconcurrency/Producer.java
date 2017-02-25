package com.david.customizingconcurrency;

public class Producer implements Runnable
{
	private MyPriorityTranferQueue<Event> buffer;

	public Producer(MyPriorityTranferQueue<Event> buffer)
	{
		super();
		this.buffer = buffer;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 100; i++)
		{
			Event event = new Event(Thread.currentThread().getName(), i);
			buffer.put(event);			
		}
	}
}
