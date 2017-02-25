package com.david.threadexecutors;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ExecutableCallable implements Callable<String>
{
	private String name;

	public ExecutableCallable(String name)
	{
		super();
		this.name = name;
	}

	@Override
	public String call() throws Exception
	{
		Random r = new Random();
		int duration = r.nextInt(6);

		System.out.println(String.format("%s: Waiting for %d seconds...", name, duration));
		TimeUnit.SECONDS.sleep(duration);

		return "Hello, " + name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
