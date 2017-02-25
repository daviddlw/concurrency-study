package com.david.synchronization;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PricesInfo
{
	private double price1;
	private double price2;
	private ReadWriteLock lock;

	public PricesInfo()
	{
		super();
		price1 = 1.0;
		price2 = 2.0;
		lock = new ReentrantReadWriteLock();
	}

	public double getPrice1()
	{
		lock.readLock().lock();
		double value = price1;
		lock.readLock().unlock();

		return value;
	}

	public double getPrice2()
	{
		lock.readLock().lock();
		double value = price2;
		lock.readLock().lock();
		return value;
	}

	public void setPrice(double price1, double price2)
	{
		lock.writeLock().lock();
		this.price1 = price1;
		this.price2 = price2;
		lock.writeLock().unlock();
	}

}

class Reader implements Runnable
{
	private PricesInfo pricesInfo;

	public Reader(PricesInfo pricesInfo)
	{
		super();
		this.pricesInfo = pricesInfo;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 10; i++)
		{
			System.out.println(String.format("Thread: %s, Price1: %f", Thread.currentThread().getName(), pricesInfo.getPrice1()));
			System.out.println(String.format("Thread: %s, Price2: %f", Thread.currentThread().getName(), pricesInfo.getPrice2()));
		}
	}

}

class Writer implements Runnable
{
	private PricesInfo pricesInfo;

	public Writer(PricesInfo pricesInfo)
	{
		super();
		this.pricesInfo = pricesInfo;
	}

	public PricesInfo getPricesInfo()
	{
		return pricesInfo;
	}

	public void setPricesInfo(PricesInfo pricesInfo)
	{
		this.pricesInfo = pricesInfo;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 3; i++)
		{
			System.out.println("Writer: Attempt to modify the price.");
			pricesInfo.setPrice(Math.random() * 10, Math.random() * 10);
			System.out.println("Price has been modifed...");
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
