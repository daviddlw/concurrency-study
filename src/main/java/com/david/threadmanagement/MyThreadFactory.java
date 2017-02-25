package com.david.threadmanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class MyThreadFactory implements ThreadFactory
{

	private int counter;
	private String name;
	private List<String> stats;

	public MyThreadFactory(String name)
	{
		super();
		this.name = name;
		counter = 0;
		stats = new ArrayList<String>();
	}

	@Override
	public Thread newThread(Runnable r)
	{
		String tName = String.format("%s_Thread_%d", name, counter);
		Thread t = new Thread(r, tName);
		counter++;
		System.out.println(tName);
		SimpleDateFormat sdf = new SimpleDateFormat(CommonUtils.YYYY_MM_DD_HH_MM_SS);
		String info = String.format("Created thread %d with name %s on %s", t.getId(), t.getName(), sdf.format(new Date()));
		stats.add(info);

		return t;
	}

	public String getStats()
	{
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = stats.iterator();
		while (it.hasNext())
		{
			sb.append(it.next());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}

