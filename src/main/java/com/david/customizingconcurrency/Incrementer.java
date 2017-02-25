package com.david.customizingconcurrency;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class Incrementer implements Runnable
{
	private AtomicIntegerArray array;

	public Incrementer(AtomicIntegerArray array)
	{
		super();
		this.array = array;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < array.length(); i++)
		{
			array.incrementAndGet(i);
		}
	}

}
