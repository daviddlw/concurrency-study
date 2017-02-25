package com.david.concurrentcollections;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

import com.david.common.CommonUtils;
import com.david.customizingconcurrency.Decrementer;
import com.david.customizingconcurrency.Incrementer;

public class MainRun
{
	public static void main(String[] args)
	{
		// runConcurrentLinkedDeque();
		// runLinkedBlockingDeque();
		 runPriorityQueue();
		// runSkipListMap();
		// runThreadLocalRandomTask();
		// runAtomVariable();
		// runAtomIntegerArray();
	}

	private static void runLinkedBlockingDeque()
	{
		LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>(3);
		Client client = new Client(list);
		Thread t = new Thread(client);
		t.start();

		System.out.println("Consumer side is starting...");
		for (int i = 0; i < 5; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				try
				{
					String item = list.take();
					if (i % 2 == 0)
					{
						System.out.println(String.format("Main - Size: %s, %s", item, CommonUtils.sdf.format(new Date())));
					} else
					{
						System.err.println(String.format("Main - Size: %s, %s", item, CommonUtils.sdf.format(new Date())));
					}
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try
			{
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("End of the program...");
	}

	private static void runConcurrentLinkedDeque()
	{
		ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
		Thread[] threads = new Thread[100];
		for (int i = 0; i < 100; i++)
		{
			threads[i] = new Thread(new AddTask("AddTask-" + i, list));
			threads[i].start();
		}

		System.out.println(String.format("%d Thread has been established...", threads.length));
		for (int i = 0; i < threads.length; i++)
		{
			try
			{
				threads[i].join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.err.println("list size: " + list.size());

		Thread[] consumerthreads = new Thread[100];
		for (int i = 0; i < 100; i++)
		{
			consumerthreads[i] = new Thread(new PollTask(list));
			consumerthreads[i].start();
		}

		System.out.println(String.format("%d Consumer Thread has been established...", threads.length));
		for (int i = 0; i < consumerthreads.length; i++)
		{
			try
			{
				consumerthreads[i].join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.err.println("total size: " + list.size());
	}

	private static void runPriorityQueue()
	{
		PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();
		Thread[] taskThreads = new Thread[5];
		for (int i = 0; i < taskThreads.length; i++)
		{
			PriorityQueueTask task = new PriorityQueueTask(i, queue);
			taskThreads[i] = new Thread(task);
		}

		for (int i = 0; i < taskThreads.length; i++)
		{
			taskThreads[i].start();
		}

		for (int i = 0; i < taskThreads.length; i++)
		{
			try
			{
				taskThreads[i].join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Total size of queue： " + queue.size());

		for (int i = 0; i < taskThreads.length * 1000; i++)
		{
			Event e = queue.poll();
			System.out.println(String.format("Thread: %s, Priority: %d\n", e.getThread(), e.getPriority()));
		}

		System.out.println("Queue size: " + queue.size());
		System.out.println("End of program...");
	}

	private static void runSkipListMap()
	{
		ConcurrentSkipListMap<String, Contact> rsMap = new ConcurrentSkipListMap<>();
		Thread[] threads = new Thread[25];
		int counter = 0;

		for (char i = 'A'; i < 'Z'; i++)
		{
			Task task = new Task(rsMap, String.valueOf(i));
			threads[counter] = new Thread(task);
			threads[counter].start();
			counter++;
		}

		for (int i = 0; i < threads.length; i++)
		{
			try
			{
				threads[i].join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(String.format("Main, Size of the map: %d\n", rsMap.size()));
		Map.Entry<String, Contact> firstEntry = rsMap.firstEntry();
		Contact item = firstEntry.getValue();
		System.out.println(String.format("Main: First entry： %s=>%s", item.getName(), item.getPhone()));

		Map.Entry<String, Contact> lastEntry = rsMap.lastEntry();
		Contact lastItem = lastEntry.getValue();
		System.out.println(String.format("Main: Last entry： %s=>%s", lastItem.getName(), lastItem.getPhone()));

		System.out.println("Main: submap from A1996 to B1002");
		ConcurrentNavigableMap<String, Contact> resultMap = rsMap.subMap("A1996", "B1002");

		Entry<String, Contact> tempEntry = null;
		do
		{
			tempEntry = resultMap.pollFirstEntry();
			if (tempEntry != null)
			{
				Contact contact = tempEntry.getValue();
				System.out.println(contact);
			}
		} while (tempEntry != null);

	}

	private static void runThreadLocalRandomTask()
	{
		Thread[] threads = new Thread[3];
		for (int i = 0; i < threads.length; i++)
		{
			threads[i] = new Thread(new ThreadLocalRandomTask());
			threads[i].start();
		}
	}

	private static void runAtomVariable()
	{
		Amount amount = new Amount();
		amount.setBalance(1000);

		Thread companyThread = new Thread(new CompanyTask(amount));
		Thread bankThread = new Thread(new BankTask(amount));

		System.out.println("Initial Amount: " + amount.getBalance());

		companyThread.start();
		bankThread.start();

		try
		{
			companyThread.join();
			bankThread.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Final Amount: " + amount.getBalance());
	}

	private static void runAtomIntegerArray()
	{
		final int count = 100;
		AtomicIntegerArray array = new AtomicIntegerArray(1000);
		Incrementer incrementer = new Incrementer(array);
		Decrementer decrementer = new Decrementer(array);

		Thread[] incrementers = new Thread[count];
		Thread[] decrementers = new Thread[count];

		for (int i = 0; i < count; i++)
		{
			incrementers[i] = new Thread(incrementer);
			decrementers[i] = new Thread(decrementer);

			incrementers[i].start();
			decrementers[i].start();
		}

		for (int i = 0; i < count; i++)
		{
			try
			{
				incrementers[i].join();
				decrementers[i].join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		for (int i = 0; i < array.length(); i++)
		{
			if (array.get(i) != 0)
			{
				System.out.println(String.format("%d=>%d", i, array.get(i)));
			}
		}

		System.out.println("Test is over...");

	}
		
}
