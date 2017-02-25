package com.david.threadmanagement;

import java.util.Date;

public class Event
{
	private Date date;
	private String event;

	public Event()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Event(Date date, String event)
	{
		super();
		this.date = date;
		this.event = event;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getEvent()
	{
		return event;
	}

	public void setEvent(String event)
	{
		this.event = event;
	}

	@Override
	public String toString()
	{
		return "Event [date=" + date + ", event=" + event + "]";
	}

}
