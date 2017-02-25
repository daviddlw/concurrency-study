package com.david.customizingconcurrency;

public class Event implements Comparable<Event>
{
	private String thread;
	private int priority;

	public Event()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Event(String thread, int priority)
	{
		super();
		this.thread = thread;
		this.priority = priority;
	}

	public String getThread()
	{
		return thread;
	}

	public void setThread(String thread)
	{
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

	@Override
	public int compareTo(Event e)
	{
		if (this.getPriority() < e.getPriority())
		{
			return 1;
		} else if (this.getPriority() > e.getPriority())
		{
			return -1;
		} else
		{
			return 0;
		}
	}

}
