package com.david.customizingconcurrency;

public class Consumer implements Runnable
{
	private MyPriorityTranferQueue<Event> buffer;

	public Consumer(MyPriorityTranferQueue<Event> buffer)
	{
		super();
		this.buffer = buffer;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 1002; i++)
		{
			try
			{
				Event event = buffer.take();
				System.out.println(String.format("Consumer: %s - %d", event.getThread(), event.getPriority()));
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
