package com.david.syncutils;

import java.util.Date;

public class Job implements Runnable
{
	private PrintQueue printQueue;

	public Job(PrintQueue printQueue)
	{
		super();
		this.printQueue = printQueue;
	}

	@Override
	public void run()
	{
		System.out.println(String.format("Thread %s, start to print job...", Thread.currentThread().getName()));
		printQueue.printJob(new Date());
		System.out.println(String.format("Thread %s, end to print job...", Thread.currentThread().getName()));
	}

	public PrintQueue getPrintQueue()
	{
		return printQueue;
	}

	public void setPrintQueue(PrintQueue printQueue)
	{
		this.printQueue = printQueue;
	}

}
