package com.david.customizingconcurrency;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyThread extends Thread
{
	private Date createDate;
	private Date startDate;
	private Date endDate;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public MyThread(Runnable r, String name)
	{
		super(r, name);
		setCreateDate();
	}

	public void setCreateDate()
	{
		createDate = new Date();
	}

	public void setStartDate()
	{
		startDate = new Date();
	}

	public void setEndDate()
	{
		endDate = new Date();
	}

	@Override
	public void run()
	{
		setStartDate();
		super.run();
		setEndDate();
	}

	private long getExecutingTime()
	{
		return endDate.getTime() - startDate.getTime();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(":");
		sb.append("StartDate: " + sdf.format(createDate));
		sb.append(", ");
		sb.append("EndDate: " + sdf.format(endDate));
		sb.append(", ExecutionTime: " + getExecutingTime() + " milliseconds.");

		return sb.toString();

	}

}
