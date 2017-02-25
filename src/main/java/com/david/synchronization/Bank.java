package com.david.synchronization;

public class Bank extends Thread
{
	private Account account;

	public Bank(Account account)
	{
		super();
		this.account = account;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 100; i++)
		{
			account.substract(1000);
		}
	}

}
