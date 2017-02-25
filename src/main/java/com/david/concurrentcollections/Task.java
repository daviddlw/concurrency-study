package com.david.concurrentcollections;

import java.util.concurrent.ConcurrentSkipListMap;

public class Task implements Runnable
{
	private ConcurrentSkipListMap<String, Contact> rsMap = new ConcurrentSkipListMap<>();
	private String id;

	public Task(ConcurrentSkipListMap<String, Contact> rsMap, String id)
	{
		super();
		this.rsMap = rsMap;
		this.id = id;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 1000; i++)
		{
			Contact contact = new Contact(id, String.valueOf(i + 1000));
			rsMap.put(id + contact.getPhone(), contact);
		}
	}

}
