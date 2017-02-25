package com.david.concurrentcollections;

public class BankTask implements Runnable
{
	private Amount amount;

	public BankTask(Amount amount)
	{
		super();
		this.amount = amount;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 10; i++)
		{
			amount.substractAmount(1000);
		}
	}

}
