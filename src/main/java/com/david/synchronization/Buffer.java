package com.david.synchronization;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer
{
	private LinkedList<String> buffer;
	private ReentrantLock lock;
	private Condition lines;
	private Condition space;
	private int maxSize;
	private boolean pendingLines;

	public Buffer(int maxSize)
	{
		super();
		this.maxSize = maxSize;
		buffer = new LinkedList<String>();
		lock = new ReentrantLock();
		lines = lock.newCondition();
		space = lock.newCondition();
		pendingLines = true;
	}

	public void insert(String line)
	{
		lock.lock();
		try
		{
			while (buffer.size() == maxSize)
			{
				space.await();
			}
			buffer.offer(line);
			System.out.println(String.format("%s, Insert line: %d", Thread.currentThread().getName(), buffer.size()));
			lines.signalAll();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			lock.unlock();
		}
	}

	public String get()
	{
		String line = "";
		lock.lock();
		try
		{
			while ((buffer.size() == 0) && hasPendingLines())
			{
				lines.await();
			}

			if (hasPendingLines())
			{
				line = buffer.poll();
				System.out.println(String.format("%s, Line Readed: %d", Thread.currentThread().getName(), buffer.size()));
				space.signalAll();
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			lock.unlock();
		}

		return line;
	}

	public void setPendingLines(boolean pendingLines)
	{
		this.pendingLines = pendingLines;
	}

	public boolean hasPendingLines()
	{
		return pendingLines || buffer.size() > 0;
	}

}

class MockProducer implements Runnable
{

	private FileMock mock;
	private Buffer buffer;

	public MockProducer(FileMock mock, Buffer buffer)
	{
		super();
		this.mock = mock;
		this.buffer = buffer;
	}

	@Override
	public void run()
	{
		buffer.setPendingLines(true);

		while (mock.hasMoreLines())
		{
			String line = mock.getLine();
			buffer.insert(line);
		}

		buffer.setPendingLines(false);
	}

}

class MockConsumer implements Runnable
{

	private Buffer buffer;

	public MockConsumer(Buffer buffer)
	{
		super();
		this.buffer = buffer;
	}

	@Override
	public void run()
	{
		while (buffer.hasPendingLines())
		{
			String line = buffer.get();
			processLines(line);
		}
	}
	
	private void processLines(String line)
	{
		Random rand = new Random();
		try
		{
			Thread.sleep(rand.nextInt(100));
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
