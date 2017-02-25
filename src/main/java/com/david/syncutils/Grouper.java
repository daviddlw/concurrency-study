package com.david.syncutils;

/**
 * 聚合方法，聚合多个线程的结果
 * 
 * @author dailiwei
 * 
 */
public class Grouper implements Runnable
{
	private Result result;

	public Grouper(Result result)
	{
		super();
		this.result = result;
	}

	@Override
	public void run()
	{
		int total = 0;
		System.out.println("Grouper: Processing the result...");
		int[] data = result.getData();

		for (int item : data)
		{
			total += item;
		}

		System.out.println("Grouper: Total result: " + total);
	}

}
