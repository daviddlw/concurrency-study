package com.david.threadexecutors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.david.common.CommonUtils;

public class MainRun
{
	public static void main(String[] args)
	{
		// runSimpleThreadExecutor();
		// runFactorialCalculator();
		// runInvokeAny();
		// runInvokeAll();
		// runDelayTask();
		// runScheduledTask();
		// runCancelTask();
		// runControlThread();
		// runSeperateLaunch();
		runRejectedDemo();
	
	}

	private static void runRejectedDemo()
	{
		RejectTaskController controller = new RejectTaskController();
		ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		exec.setRejectedExecutionHandler(controller);

		System.out.println("Starting...");
		for (int i = 0; i < 3; i++)
		{
			ResultCallable callable = new ResultCallable("Task-" + i);
			exec.submit(callable);
		}
		
		System.out.println("Execute shutdown command...");
		exec.shutdown();
		
		System.out.println("Sending another task...");
		ResultCallable anotherCallable = new ResultCallable("rejected task");
		exec.submit(anotherCallable);
		
		System.out.println("Ending...");
	}

	private static void runSeperateLaunch()
	{
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<String> service = new ExecutorCompletionService<>(executor);

		ReportRequest faceReq = new ReportRequest("face", service);
		ReportRequest onlineReq = new ReportRequest("online", service);

		Thread faceThread = new Thread(faceReq);
		Thread onlineThread = new Thread(onlineReq);

		ReportProcessor processor = new ReportProcessor(service);
		Thread senderThread = new Thread(processor);

		System.out.println("Starting the threads...");
		faceThread.start();
		onlineThread.start();
		senderThread.start();

		System.out.println("Waiting for the report generator...");

		try
		{
			faceThread.join();
			onlineThread.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Ending the threads...");
		executor.shutdown();

		try
		{
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		processor.setEnd(true);
		System.out.println("Test End");
	}

	private static void runControlThread()
	{
		ExecutorService exec = Executors.newCachedThreadPool();
		CustomTask[] customTasks = new CustomTask[5];
		for (int i = 0; i < 5; i++)
		{
			ExecutableCallable execCallable = new ExecutableCallable("Task-" + i);
			customTasks[i] = new CustomTask(execCallable);
			exec.submit(customTasks[i]);
		}

		try
		{
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < customTasks.length; i++)
		{
			customTasks[i].cancel(true);
		}

		for (int i = 0; i < customTasks.length; i++)
		{
			if (!customTasks[i].isCancelled())
			{
				try
				{
					System.out.println("not cancelled:　" + customTasks[i].get());
				} catch (InterruptedException | ExecutionException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		exec.shutdown();
		System.out.println("controlling test is over...");
	}

	private static void runCancelTask()
	{
		CancelCallable task = new CancelCallable();
		ExecutorService exec = Executors.newCachedThreadPool();
		Future<String> result = exec.submit(task);

		try
		{
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 取消任务
		result.cancel(true);

		try
		{
			System.out.println("result value: " + result.get());
		} catch (InterruptedException | ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Task is cancelled? " + result.isCancelled());
		System.out.println("Task is done? " + result.isDone());
		exec.shutdown();
		System.out.println("runCancelTask end...");
	}

	private static void runScheduledTask()
	{
		ScheduledExecutorService scheduledExec = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture<?> result = scheduledExec.scheduleAtFixedRate(new ScheduledTask(), 0, 3, TimeUnit.SECONDS);
		for (int i = 0; i < 10; i++)
		{
			System.err.println("Main: Delay-" + result.getDelay(TimeUnit.MILLISECONDS));
			try
			{
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		scheduledExec.shutdown();

		try
		{
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Task end " + CommonUtils.sdf.format(new Date()));
	}

	private static void runDelayTask()
	{
		ScheduledExecutorService scheduledExec = Executors.newSingleThreadScheduledExecutor();
		for (int i = 0; i < 5; i++)
		{
			scheduledExec.schedule(new DelayCallable("Task-" + 0), i + 1, TimeUnit.SECONDS);
		}

		scheduledExec.shutdown();

		try
		{
			scheduledExec.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Delay test is over...\n" + CommonUtils.sdf.format(new Date()));
	}

	private static void runInvokeAll()
	{
		ExecutorService exec = Executors.newCachedThreadPool();
		List<ResultCallable> tasks = new ArrayList<ResultCallable>();

		for (int i = 0; i < 3; i++)
		{
			tasks.add(new ResultCallable("Task-" + i));
		}

		List<Future<Result>> resultLs = null;
		try
		{
			resultLs = exec.invokeAll(tasks);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Future<Result> future : resultLs)
		{
			Result item;
			try
			{
				item = future.get();
				System.out.println(item);
			} catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		exec.shutdown();
	}

	private static void runSimpleThreadExecutor()
	{
		Server server = new Server(true);
		for (int i = 0; i < 100; i++)
		{
			Task task = new Task("Task-" + i);
			server.executeTask(task);
		}

		server.endServer();
	}

	private static void runFactorialCalculator()
	{
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

		List<Future<Integer>> ls = new ArrayList<>();
		Random r = new Random();
		int num = 0;

		for (int i = 0; i < 10; i++)
		{
			num = r.nextInt(10);

			Future<Integer> item = executor.submit(new FutureExecutor(num));
			System.err.println("num: " + num);
			ls.add(item);
		}

		do
		{
			System.out.println(String.format("Main: Number of Completed Tasks: %d", executor.getCompletedTaskCount()));
			for (int i = 0; i < ls.size(); i++)
			{
				Future<Integer> item = ls.get(i);
				System.out.println(String.format("Main: Task is Done %s", item.isDone()));
			}

			try
			{
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (executor.getCompletedTaskCount() < ls.size());

		System.out.println("Main Results: ");
		for (int i = 0; i < ls.size(); i++)
		{
			Future<Integer> item = ls.get(i);
			try
			{
				int number = item.get();
				System.out.println(String.format("i: %d, number: %d", i, number));
			} catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		executor.shutdown();
	}

	private static void runInvokeAny()
	{
		UserValidator user1 = new UserValidator("user1");
		UserValidator user2 = new UserValidator("user2");

		TaskValidator taskValidator1 = new TaskValidator(user1, "task1", "task1");
		TaskValidator taskValidator2 = new TaskValidator(user2, "task2", "task2");

		List<TaskValidator> taskValidators = Arrays.asList(new TaskValidator[] { taskValidator1, taskValidator2 });
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
		String result = "";

		System.out.println("Start of the validator...");

		try
		{
			result = executor.invokeAny(taskValidators);
			System.out.println("Main Results: " + result);
		} catch (InterruptedException | ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		executor.shutdown();

		System.out.println("End the validator...");
	}
}
