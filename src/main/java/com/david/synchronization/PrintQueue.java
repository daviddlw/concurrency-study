package com.david.synchronization;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue
{
	private final Lock queueLock = new ReentrantLock();
	/**
	 * 公平锁，从0开始到max，顺序轮训获取锁
	 */
	private final Lock queueLock2 = new ReentrantLock(true);

	public void printJob(Object doc)
	{
		queueLock.lock();

		try
		{
			Random r = new Random();
			int duration = r.nextInt(5);
			String info = String.format("Thread: %s, PrintQueue: Printing a Job during %d seconds", Thread.currentThread().getName(),
					duration);

			System.out.println(info);
			System.out.println("finish job...");
			
			TimeUnit.SECONDS.sleep(duration);			

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			queueLock.unlock();
		}
	}
	
	public void printJob2(Object doc)
	{
		queueLock2.lock();

		try
		{
			Random r = new Random();
			int duration = r.nextInt(5);
			String info = String.format("Thread: %s, PrintQueue: Printing a Job during %d seconds", Thread.currentThread().getName(),
					duration);

			System.out.println(info);
			System.out.println("finish job...");

			TimeUnit.SECONDS.sleep(duration);			

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			queueLock2.unlock();
		}
		
		queueLock2.lock();
		try
		{
			Random r = new Random();
			int duration = r.nextInt(5);
			String info = String.format("Thread: %s, PrintQueue: Printing a Job during %d seconds", Thread.currentThread().getName(),
					duration);

			System.out.println(info);

			TimeUnit.SECONDS.sleep(duration);			

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			queueLock2.unlock();
		}
	}
}

class Job implements Runnable
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
		String info = String.format("Thread: %s, start to print doc...", Thread.currentThread().getName());
		System.out.println(info);
		printQueue.printJob(new Object());
		String info2 = String.format("Thread: %s, end to print doc...", Thread.currentThread().getName());
		System.out.println(info2);
	}
}

class Job2 implements Runnable
{

	private PrintQueue printQueue;

	public Job2(PrintQueue printQueue)
	{
		super();
		this.printQueue = printQueue;
	}

	@Override
	public void run()
	{
		String info = String.format("Thread: %s, start to print doc...", Thread.currentThread().getName());
		System.out.println(info);
		printQueue.printJob2(new Object());
		String info2 = String.format("Thread: %s, end to print doc...", Thread.currentThread().getName());
		System.out.println(info2);
	}
}
