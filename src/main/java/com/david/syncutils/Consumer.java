package com.david.syncutils;

import java.util.List;
import java.util.concurrent.Exchanger;

public class Consumer implements Runnable
{
	private List<String> buffer;
	private final Exchanger<List<String>> exchanger;

	public Consumer(List<String> buffer, Exchanger<List<String>> exchanger)
	{
		super();
		this.buffer = buffer;
		this.exchanger = exchanger;
	}

	@Override
	public void run()
	{
		int cycle = 1;
		for (int i = 0; i < 10; i++)
		{
			String info = String.format("Consumer: Cycle %d", cycle);
			System.out.println(info);
			try
			{
				buffer = exchanger.exchange(buffer);
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			System.out.println("Consumer :" + buffer.size());
			for (int j = 0; j < 10; j++)
			{
				String message = buffer.get(0);
				System.out.println("Consumer: " + message);
				buffer.remove(0);
			}
			
			cycle++;

		}
	}

}
