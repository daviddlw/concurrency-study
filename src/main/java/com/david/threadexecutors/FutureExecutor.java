package com.david.threadexecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FutureExecutor implements Callable<Integer>
{
	public int number;

	public FutureExecutor(int number)
	{
		super();
		this.number = number;
	}

	@Override
	public Integer call() throws Exception
	{
		return executeFactorialCalculator();
	}

	private int executeFactorialCalculator() throws InterruptedException
	{
		int result = 1;

		if (number == 0 || number == 1)
		{
			result = 1;
		} else
		{
			for (int i = 2; i < number; i++)
			{
				result *= i;
				TimeUnit.MILLISECONDS.sleep(20);
			}
		}

		System.out.println(String.format("%s: %d", Thread.currentThread().getName(), result));

		return result;
	}

}
