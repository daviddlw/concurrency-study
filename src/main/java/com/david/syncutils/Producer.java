package com.david.syncutils;

import java.util.List;
import java.util.concurrent.Exchanger;

public class Producer implements Runnable
{
	private List<String> buffer;
	private final Exchanger<List<String>> exchanger;

	public Producer(List<String> buffer, Exchanger<List<String>> exchanger)
	{
		super();
		this.buffer = buffer;
		this.exchanger = exchanger;
	}

	@Override
	public void run()
	{
		System.out.println("execute producer start...");
		int cycle = 1;
		for (int i = 0; i < 10; i++)
		{
			System.out.println(String.format("Producer: Cycle %d", cycle));
			for (int j = 0; j < 10; j++)
			{
				String message = String.format("Event %d", (i * 10) + j);
				System.out.println("Producer: " + message);
				buffer.add(message);
			}

			try
			{
				buffer = exchanger.exchange(buffer);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			cycle++;

			System.out.println("Producer: " + buffer.size());
		}

		System.out.println("execute producer end...");
	}

}
