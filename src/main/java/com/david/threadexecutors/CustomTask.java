package com.david.threadexecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CustomTask extends FutureTask<String>
{
	private String name;

	public CustomTask(Callable<String> callable)
	{
		super(callable);
		name = ((ExecutableCallable) callable).getName();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	protected void done()
	{
		if (isCancelled())
		{
			System.out.println("task has been canceled...");
		} else
		{
			System.out.println("task has finished...");
		}
	}

}
