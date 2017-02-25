package com.david.concurrentcollections;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.david.common.CommonUtils;

public class Client implements Runnable
{
	private LinkedBlockingDeque<String> list;

	public Client(LinkedBlockingDeque<String> list)
	{
		super();
		this.list = list;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 5; i++)
		{

			for (int j = 0; j < 3; j++)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%d : %d", i, j));
				list.add(sb.toString());

				System.out.println(String.format("A request is sent => %s", CommonUtils.sdf.format(new Date())));
			}

			try
			{
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Client executing is over...");
	}

}
