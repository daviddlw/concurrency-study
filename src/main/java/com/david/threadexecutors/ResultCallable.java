package com.david.threadexecutors;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ResultCallable implements Callable<Result>
{
	private String name;

	public ResultCallable(String name)
	{
		super();
		this.name = name;
	}

	@Override
	public Result call() throws Exception
	{
		System.out.println(String.format("%s, Starting", name));
		Random r = new Random();
		int duration = r.nextInt(6);
		try
		{
			System.out.println(String.format("%s, Waiting %d seconds for results.", name, duration));
			TimeUnit.SECONDS.sleep(duration);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		int value = 0;
		for (int i = 0; i < 3; i++)
		{
			value += (int) (Math.random() * 100);
		}

		Result result = new Result(value, name);
		System.out.println(String.format("%s, End", name));
		
		return result;
	}

}
