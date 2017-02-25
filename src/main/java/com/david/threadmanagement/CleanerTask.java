package com.david.threadmanagement;

import java.util.Date;
import java.util.Deque;

public class CleanerTask extends Thread
{
	private Deque<Event> deque;

	public CleanerTask(Deque<Event> deque)
	{
		super();
		this.deque = deque;
		setDaemon(true);
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
		while (true)
		{
			Date now = new Date();
			clean(now);
		}
	}
	
	private void runException(){
		String test = null;
		System.out.println(test.trim());
	}

	private void clean(Date date)
	{
		long difference = 0;
		boolean delete = false;
		
		if (deque.size() > 0)
		{
			do
			{
				Event event = deque.getLast();
				difference = date.getTime() - event.getDate().getTime();
				if (difference > 10000)
				{
					System.out.println(String.format("Cleaner: %s", event.getEvent()));
					deque.removeLast();
					delete = true;
				}
				
				runException();

			} while (difference > 10000);
		}

		if (delete)
		{
			System.out.println(String.format("Cleaner: size of queue: %d", deque.size()));
		}
	}

}
