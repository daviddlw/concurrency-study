package com.david.customizingconcurrency;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MyPriorityTranferQueue<T> extends PriorityBlockingQueue<T> implements TransferQueue<T>
{
	private static final long serialVersionUID = 1L;
	private AtomicInteger counter;
	private LinkedBlockingQueue<T> transfered;
	private ReentrantLock lock;

	public MyPriorityTranferQueue()
	{
		super();
		counter = new AtomicInteger(0);
		lock = new ReentrantLock();
		transfered = new LinkedBlockingQueue<>();
	}

	public MyPriorityTranferQueue(Collection<? extends T> c)
	{
		super(c);
		// TODO Auto-generated constructor stub
	}

	public MyPriorityTranferQueue(int initialCapacity, Comparator<? super T> comparator)
	{
		super(initialCapacity, comparator);
		// TODO Auto-generated constructor stub
	}

	public MyPriorityTranferQueue(int initialCapacity)
	{
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean tryTransfer(T e)
	{
		lock.lock();
		boolean value = false;
		if (counter.get() == 0)
		{
			value = false;
		} else
		{
			put(e);
			value = true;
		}
		lock.unlock();
		return value;
	}

	@Override
	public void transfer(T e) throws InterruptedException
	{
		lock.lock();
		boolean value = false;
		if (counter.get() != 0)
		{
			put(e);
			lock.unlock();
		} else
		{
			transfered.add(e);
			lock.unlock();
			synchronized (e)
			{
				e.wait();
			}
		}
	}

	@Override
	public boolean tryTransfer(T e, long timeout, TimeUnit unit) throws InterruptedException
	{
		lock.lock();
		if (counter.get() != 0)
		{
			put(e);
			lock.unlock();
			return true;
		} else
		{
			transfered.add(e);
			long newTimeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
			lock.unlock();
			e.wait(newTimeout);

			lock.lock();
			if (transfered.contains(e))
			{
				transfered.remove(e);
				lock.unlock();
				return false;
			} else
			{
				lock.unlock();
				return true;
			}
		}
	}

	@Override
	public boolean hasWaitingConsumer()
	{
		// TODO Auto-generated method stub
		return (counter.get() != 0);
	}

	@Override
	public int getWaitingConsumerCount()
	{
		// TODO Auto-generated method stub
		return counter.get();
	}

	@Override
	public T take() throws InterruptedException
	{
		lock.lock();
		counter.incrementAndGet();
		T value = transfered.poll();
		if (value == null)
		{
			lock.unlock();
			value = super.take();
			lock.lock();
		} else
		{
			synchronized (value)
			{
				value.notify();
			}
		}
		counter.decrementAndGet();
		lock.unlock();
		
		return value;
	}

}
