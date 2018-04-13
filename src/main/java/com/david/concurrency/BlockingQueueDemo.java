package com.david.concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

public class BlockingQueueDemo
{
	private static final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();;

	public BlockingQueueDemo()
	{
		super();

	}

	public static void main(String[] args) throws InterruptedException
	{
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		System.err.println("producer-consumer start...");
		producer.start();
		consumer.start();

		producer.join();

		System.err.println("producer-consumer end...");
	}

}

class Producer extends Thread
{
	private BlockingQueue<Integer> queue;

	public Producer(BlockingQueue<Integer> queue)
	{
		super();
		this.queue = queue;
	}

	@Override
	public void run()
	{
		List<Integer> list = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6 });
		boolean flag = false;
		String message = StringUtils.EMPTY;
		for (Integer num : list)
		{
//			flag = queue.offer(num);
			try {
				queue.put(num);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			message = String.format("Producer offer a number: %d, flag = %s",
					num, flag);
			System.out.println(message);
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Consumer extends Thread
{
	private BlockingQueue<Integer> queue;

	public Consumer(BlockingQueue<Integer> queue)
	{
		super();
		this.queue = queue;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Integer result = queue.take();
				TimeUnit.SECONDS.sleep(2);
				System.err.println(result);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
