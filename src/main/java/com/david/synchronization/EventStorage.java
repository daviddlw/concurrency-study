package com.david.synchronization;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EventStorage
{
	private int maxSize;
	private LinkedList<Date> storageList;
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

	public EventStorage()
	{
		super();
		maxSize = 10;
		storageList = new LinkedList<Date>();
	}

	public int getMaxSize()
	{
		return maxSize;
	}

	public void setMaxSize(int maxSize)
	{
		this.maxSize = maxSize;
	}

	public List<Date> getStorageList()
	{
		return storageList;
	}

	public void setStorageList(LinkedList<Date> storageList)
	{
		this.storageList = storageList;
	}

	public synchronized void set()
	{
		while (storageList.size() == maxSize)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		storageList.offer(new Date());
		System.out.println(String.format("producer run, storage size: %s", storageList.size()));
		notifyAll();
	}

	public synchronized void get()
	{
		while (storageList.size() == 0)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Date date = storageList.poll();
		System.out.println("consumer run: " + sdf.format(date));
		notifyAll();
	}

}

class Producer implements Runnable
{

	private EventStorage storage;

	public Producer(EventStorage storage)
	{
		super();
		this.storage = storage;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 20; i++)
		{
			storage.set();
		}
	}

}

class Consumer implements Runnable
{

	private EventStorage storage;

	public Consumer(EventStorage storage)
	{
		super();
		this.storage = storage;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 20; i++)
		{
			storage.get();
		}
	}

}
