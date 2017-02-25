package com.david.customizingconcurrency;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyExecutor extends ThreadPoolExecutor
{
	private ConcurrentHashMap<String, Date> startTimes;

	public MyExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		startTimes = new ConcurrentHashMap<>();
	}

	@Override
	public void shutdown()
	{
		System.out.println("MyExecutor: Going to shutdown.");
		System.out.println("MyExecutor: Executed task： " + getCompletedTaskCount());
		System.out.println("MyExecutor: Running task: " + getActiveCount());
		System.out.println("MyExecutor: Pending task: " + getQueue().size());
		super.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow()
	{
		/**
		 * 对于运行结束的任务使用 getCompletedTaskCount()
		 * 对于正在运行的任务使用 getActiveCount();
		 */
		System.out.println("MyExecutor: Going to immediately shutdown.");
		System.out.println("MyExecutor: Executed task： " + getCompletedTaskCount());
		System.out.println("MyExecutor: Running task: " + getActiveCount());
		System.out.println("MyExecutor: Pending task: " + getQueue().size());
		return super.shutdownNow();
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r)
	{
		System.out.println(String.format("MyExecutor is beginning: %s-%s", t.getName(), r.hashCode()));
		startTimes.put(String.valueOf(r.hashCode()), new Date());
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t)
	{
		Future<?> result = (Future<?>) r;
		System.out.println("MyExecutor is over.");
		try
		{
			System.out.println("MyExecutor result: " + result.get());
			Date startDate = startTimes.remove(String.valueOf(r.hashCode()));
			Date endDate = new Date();
			long diff = endDate.getTime() - startDate.getTime();
			System.out.println("MyExecutor duration: " + diff);

		} catch (InterruptedException | ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
