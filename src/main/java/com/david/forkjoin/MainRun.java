package com.david.forkjoin;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class MainRun
{
	public static void main(String[] args)
	{
		// runForkJoinFramework();
		runJoinResult();
	}

	private static void runJoinResult()
	{
		Document doc = new Document();
		String searhWord = "the";
		String[][] matrix = doc.generateDocument(100, 1000, searhWord);
		DocumentTask documentTask = new DocumentTask(matrix, 0, 100, searhWord);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(documentTask);

		do
		{
			System.out.println("==================================");
			System.out.println("Main-Parallelism: " + pool.getParallelism());
			System.out.println("Main-Active Thread: " + pool.getActiveThreadCount());
			System.out.println("Main-Task Count: " + pool.getQueuedTaskCount());
			System.out.println("Main-Steal Count: " + pool.getStealCount());

			try
			{
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (!documentTask.isDone());

		pool.shutdown();
		try
		{
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Integer count;
		try
		{
			count = documentTask.get();
		} catch (InterruptedException | ExecutionException e)
		{
			// TODO Auto-generated catch block
			count = 0;
			e.printStackTrace();
		}

		System.out.println("The word " + searhWord + " appears " + count + " times...");

	}

	/**
	 * 跑forjoin框架
	 */
	private static void runForkJoinFramework()
	{
		ProductListGenerator generator = new ProductListGenerator();
		List<Product> products = generator.generate(50);

		Task task = new Task(products, 0, products.size(), 5);

		ForkJoinPool forkJoinPool = new ForkJoinPool();
		forkJoinPool.execute(task);

		do
		{
			System.out.println("Main: Thread Count=> " + forkJoinPool.getActiveThreadCount());
			System.out.println("Main: Thread Steal=> " + forkJoinPool.getStealCount());
			System.out.println("Main: Parallelism=> " + forkJoinPool.getParallelism());

		} while (!task.isDone());

		forkJoinPool.shutdown();

		if (task.isCompletedNormally())
		{
			System.out.println("Main: The task has completed normally.");
		}

		for (int i = 0; i < products.size(); i++)
		{
			Product product = products.get(i);
			if (product.getPrice() != 15)
			{
				System.out.println("Product: " + product);
			}
		}

		System.out.println("runForkJoinFramework end...");
	}
}
