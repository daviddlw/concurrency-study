package com.david.customizingconcurrency;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ScheduledExecutorTask;

import com.david.threadexecutors.ScheduledTask;

public class MainRun
{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args)
	{
		// runMyExecutor();
		// runPriorityExecutorTasks();
		// runThreadFactory();
		// runPeriodicTask();
		// runDelayedTask();
		// runForkJoinPoolTask();
		runMyPriorityTransferQueue();
	}

	public static void runMyExecutor()
	{
		MyExecutor myExecutor = new MyExecutor(2, 4, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
		List<Future<String>> list = new ArrayList<>();

		for (int i = 0; i < 10; i++)
		{
			SleepTwoSecondsTask task = new SleepTwoSecondsTask();
			Future<String> result = myExecutor.submit(task);
			list.add(result);
		}

		for (int i = 0; i < 5; i++)
		{
			try
			{
				String result = list.get(i).get();
				System.out.println("Task result: " + result);
			} catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		myExecutor.shutdown();

		for (int i = 5; i < 10; i++)
		{
			try
			{
				String result = list.get(i).get();
				System.out.println("Task result: " + result);
			} catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try
		{
			myExecutor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void runPriorityExecutorTasks()
	{
		System.out.println("Priority executor is starting...");
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

		for (int i = 0; i < 4; i++)
		{
			executor.execute(new MyPriorityTask("Thread-" + i, i));
		}

		try
		{
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		for (int i = 4; i < 8; i++)
		{
			executor.execute(new MyPriorityTask("Thread-" + i, i));
		}

		executor.shutdown();

		System.out.println("Executor is shutdown...");

		try
		{
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Priority executor is overing...");

	}

	private static void runThreadFactory()
	{
		MyThreadFactory factory = new MyThreadFactory("MyThreadFactory");
		MyTask task = new MyTask();
		// 同一个factory对象
		Thread thread = factory.newThread(task);
		Thread thread2 = factory.newThread(task);
		Thread thread3 = factory.newThread(task);

		thread.start();
		thread2.start();
		thread3.start();
		try
		{
			thread.join();
			thread2.join();
			thread3.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Main: Thread Information: ");
		System.out.println(thread);
		System.out.println(thread2);
		System.out.println(thread3);
		System.out.println("Main: End of the example.");

	}

	private static void runPeriodicTask()
	{
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		Runnable r = new Runnable() {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			@Override
			public void run()
			{
				System.out.println(sdf.format(new Date()));
			}
		};

		exec.scheduleAtFixedRate(r, 0, 2, TimeUnit.SECONDS);
	}

	private static void runDelayedTask()
	{
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		Runnable r = new Runnable() {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			@Override
			public void run()
			{
				System.out.println(sdf.format(new Date()));
			}
		};

		System.err.println(sdf.format(new Date()));
		exec.scheduleWithFixedDelay(r, 0, 3, TimeUnit.SECONDS);
		System.err.println(sdf.format(new Date()));
	}

	private static void runForkJoinPoolTask()
	{
		List<Product> products = GeneratorFactory.generate(1000);

		ForkJoinTask task = new ForkJoinTask(products, 0, products.size(), 0.2);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);

		do
		{
			System.out.println("Main: active thread count => " + pool.getActiveThreadCount());
			System.out.println("Main: steal thread => " + pool.getStealCount());
			System.out.println("Main: Parallelism => " + pool.getParallelism());
		} while (!task.isDone());

		pool.shutdown();

		if (task.isCompletedNormally())
		{
			System.out.println("Task is completed normally...");
		}

		for (int i = 0; i < products.size(); i++)
		{
			System.out.println(products.get(i));
		}

		System.out.println("ForkJoinPool task is over...");
	}

	private static void runMyPriorityTransferQueue()
	{
		MyPriorityTranferQueue<Event> buffer = new MyPriorityTranferQueue<>();
		Producer producer = new Producer(buffer);

		Thread[] producerThread = new Thread[10];
		for (int i = 0; i < producerThread.length; i++)
		{
			producerThread[i] = new Thread(producer);
			producerThread[i].start();
		}

		Consumer consumer = new Consumer(buffer);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		System.out.println(String.format("Main - Buffer: Consumer count： %d", buffer.getWaitingConsumerCount()));
		Event event = new Event("Core Event", 0);
		try
		{
			buffer.transfer(event);
			System.out.println("My Event has been transfered...");

			for (int i = 0; i < producerThread.length; i++)
			{
				producerThread[i].join();
			}

			TimeUnit.SECONDS.sleep(1);
			System.out.println(String.format("Main - Buffer: Consumer count： %d", buffer.getWaitingConsumerCount()));

			event = new Event("Core Event 2", 0);
			buffer.transfer(event);

			consumerThread.join();
			System.out.println("Main: The end of the program...");

		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
