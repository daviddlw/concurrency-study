package com.david.synchronization;

public class Company extends Thread
{
	private Account account;

	public Company(Account account)
	{
		super();
		this.account = account;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 100; i++)
		{
			account.add(1000);
		}
	}

}
