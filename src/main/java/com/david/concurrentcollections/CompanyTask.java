package com.david.concurrentcollections;

public class CompanyTask implements Runnable
{
	private Amount amount;

	public CompanyTask(Amount amount)
	{
		super();
		this.amount = amount;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 10; i++)
		{
			amount.addAmount(1000);
		}
	}

}
