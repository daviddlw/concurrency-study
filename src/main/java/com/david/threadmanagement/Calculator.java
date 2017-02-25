package com.david.threadmanagement;

public class Calculator implements Runnable
{
	private int number;

	public Calculator(int number)
	{
		super();
		this.number = number;
	}

	@Override
	public void run()
	{
		for (int i = 1; i <= 10; i++)
		{
			System.out.println(String.format("%s, %d * %d = %d", Thread.currentThread().getName(), number, i, number
					* i));
		}
	}

}
