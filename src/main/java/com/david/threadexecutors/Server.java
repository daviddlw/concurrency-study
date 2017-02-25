package com.david.threadexecutors;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server
{
	private ThreadPoolExecutor executor;
	private boolean isFixed = false;

	public Server(boolean isFixed)
	{
		super();
		if (isFixed)
		{
			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
		} else
		{
			executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		}

		// executor.setMaximumPoolSize(50);
	}

	public void executeTask(Task task)
	{
		System.out.println(String.format("Server: A new task has arrived..."));
		executor.execute(task);
		System.out.println(String.format("Server: Pool size=> %d", executor.getPoolSize()));
		System.out.println(String.format("Server: Maximum Pool Size %d", executor.getMaximumPoolSize()));
		System.out.println(String.format("Server: Active Count=> %d", executor.getActiveCount()));
		System.out.println(String.format("Server: Completed tasks... %d", executor.getCompletedTaskCount()));
	}

	public void endServer()
	{
		executor.shutdown();
		// executor.shutdownNow();
	}
}
