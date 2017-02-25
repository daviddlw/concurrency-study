package com.david.concurrentcollections;

public class Event implements Comparable<Event>
{
	private int priority;
	private int thread;

	public Event(int thread, int priority)
	{
		super();
		this.priority = priority;
		this.thread = thread;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public int getThread()
	{
		return thread;
	}

	public void setThread(int thread)
	{
		this.thread = thread;
	}

	@Override
	public int compareTo(Event e)
	{
		if (this.priority > e.priority)
		{
			return -1;
		} else if (this.priority < e.priority)
		{
			return 1;
		} else
		{
			return 0;
		}
	}

}
