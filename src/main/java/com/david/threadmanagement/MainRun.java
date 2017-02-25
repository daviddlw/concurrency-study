package com.david.threadmanagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class MainRun
{
	private static int count = 10;
	private static Thread[] threads = new Thread[count];
	private static State[] status = new State[count];
	private static Logger logger = Logger.getLogger(MainRun.class);

	public static void main(String[] args)
	{
		// runCalculator(3);
		// runCalculator2();
		// runInterrupted();
		// runFileSearch();
		// runFileClock();
		// runFinializationOfAThread();
		// runDaemonThread();
		// runExceptionHandler();
		// runUnsafeTask();
		// runSafeTask();
		// runThreadGroup();
		// runMyThreadGroup();
		runMyThreadFactory();
	}

	private static void runMyThreadFactory()
	{
		MyThreadFactory factory = new MyThreadFactory("MyThreadFactory");
		Thread t;
		System.out.println("Starting the thread...");
		for (int i = 0; i < 10; i++)
		{
			t = factory.newThread(new Runnable() {

				@Override
				public void run()
				{
					try
					{
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
		}

		System.err.println("Factory stats: " + factory.getStats());
	}

	private static void runMyThreadGroup()
	{
		MyThreadGroup myThreadGroup = new MyThreadGroup("MyThreadGroup");
		MyThreadGroupTask task = new MyThreadGroupTask();
		for (int i = 0; i < 2; i++)
		{
			Thread t = new Thread(myThreadGroup, task);
			t.start();
		}
	}

	private static void runThreadGroup()
	{
		ThreadGroup threadGroup = new ThreadGroup("Searcher");
		Result result = new Result();
		SearchTask searchTask = new SearchTask(result);

		for (int i = 0; i < 5; i++)
		{
			Thread t = new Thread(threadGroup, searchTask);
			t.start();

			try
			{
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(String.format("Number of thread group: %d", threadGroup.activeCount()));
		System.out.println("Information about the thread group");
		threadGroup.list();

		Thread[] threads = new Thread[threadGroup.activeCount()];
		threadGroup.enumerate(threads);

		for (Thread thread : threads)
		{
			System.out.println(String.format("Thread name: %s, Thread state: %s", thread.getName(), thread.getState()));
		}

		waitFinish(threadGroup);

		threadGroup.interrupt();
	}

	private static void waitFinish(ThreadGroup threadGroup)
	{
		while (threadGroup.activeCount() > 9)
		{
			try
			{
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void runUnsafeTask()
	{
		UnsafeTask unsafeTask = new UnsafeTask();
		for (int i = 0; i < 3; i++)
		{
			Thread t = new Thread(unsafeTask);
			t.start();

			try
			{
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void runSafeTask()
	{
		SafeTask safeTask = new SafeTask();
		for (int i = 0; i < 3; i++)
		{
			Thread t = new Thread(safeTask);
			t.start();

			try
			{
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void runExceptionHandler()
	{
		Thread t = new Thread(new TaskRun());

		t.setUncaughtExceptionHandler(new ExceptionHandler());
		t.start();
	}

	public static void runDaemonThread()
	{
		int count = 3;
		Deque<Event> deque = new ArrayDeque<>();
		WriterTask writerTask = new WriterTask(deque);
		for (int i = 0; i < count; i++)
		{
			Thread t = new Thread(writerTask);
			t.start();
		}

		CleanerTask cleanerTask = new CleanerTask(deque);
		cleanerTask.start();
	}

	public static void runFinializationOfAThread()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(CommonUtils.YYYY_MM_DD_HH_MM_SS);
		System.out.println("runFinializationOfAThread start..." + sdf.format(new Date()));

		Thread t1 = new Thread(new DataSourcesLoader());
		Thread t2 = new Thread(new NetworkConnectionsLoader());

		t1.start();
		t2.start();

		try
		{
			t1.join();
			t2.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		// this part will be executed after the t1 and t2 finished...
		System.out.println("runFinializationOfAThread end..." + sdf.format(new Date()));
	}

	public static void runFileClock()
	{
		FileClock fileClock = new FileClock();
		Thread t = new Thread(fileClock);
		t.start();

		try
		{
			TimeUnit.SECONDS.sleep(5);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		t.interrupt();
	}

	public static void runCalculator2()
	{
		for (int i = 0; i < count; i++)
		{
			threads[i] = new Thread(new Calculator(i));
			if (i % 2 == 0)
			{
				threads[i].setPriority(Thread.MAX_PRIORITY);
			} else
			{
				// 如果priority不在1到10的范围里面则会抛出java.lang.IllegalArgumentException异常
				threads[i].setPriority(Thread.MIN_PRIORITY);
				// threads[i].setPriority(14);
			}

			threads[i].setName(String.format("My Thread - %d", i));
		}

		String path = "F:" + File.separator + "threadlog.txt";
		try
		{
			FileWriter fw = new FileWriter(path);
			PrintWriter pw = new PrintWriter(fw);
			for (int i = 0; i < count; i++)
			{
				pw.println(String.format("Main : Status of Thread %s : %s", threads[i].getName(), threads[i].getState()));
				status[i] = threads[i].getState();
			}

			for (int i = 0; i < count; i++)
			{
				threads[i].start();
			}

			boolean finish = false;
			while (!finish)
			{
				for (int i = 0; i < count; i++)
				{
					if (threads[i].getState() != status[i])
					{
						writeThreadInfo(pw, threads[i], status[i]);
						status[i] = threads[i].getState();
					}
				}

				finish = true;

				for (int i = 0; i < count; i++)
				{
					finish = finish && (threads[i].getState() == State.TERMINATED);
				}
			}
		} catch (IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	private static void runCalculator(int count)
	{
		for (int i = 0; i < count; i++)
		{
			Thread t = new Thread(new Calculator(i));
			t.start();
		}
	}

	public static void runFileSearch()
	{
		FileSearch fileSearch = new FileSearch("F:\\dubbox-master\\", "Validation.java");
		Thread thread = new Thread(fileSearch);
		thread.start();

		Thread thread2 = new Thread(fileSearch);
		thread2.start();

		try
		{
			TimeUnit.SECONDS.sleep(5);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		thread.interrupt();
	}

	private static void runInterrupted()
	{
		PrimeGenerator generator = new PrimeGenerator();
		generator.start();

		try
		{
			Thread.sleep(5000);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		generator.interrupt();
	}

	private static void writeThreadInfo(PrintWriter pw, Thread thread, State state)
	{
		pw.println(String.format("Main : Id %d - %s", thread.getId(), thread.getName()));
		pw.println(String.format("Main : Priority: %d", thread.getPriority()));
		pw.println(String.format("Main : Old State: %s", state));
		pw.println(String.format("Main : New State: %s", thread.getState()));
		pw.println(String.format("Main : ************************************"));

		pw.flush();
		pw.close();
	}
}
