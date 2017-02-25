package com.david.synchronization;

public class Account
{
	private int balance;

	public Account()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Account(int balance)
	{
		super();
		this.balance = balance;
	}

	public synchronized void add(int amount)
	{
		int tmp = balance;
		try
		{
			Thread.sleep(10);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		tmp += amount;
		balance = tmp;
	}

	public synchronized void substract(int amount)
	{
		int tmp = balance;
		try
		{
			Thread.sleep(10);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		tmp -= amount;
		balance = tmp;
	}

	public int getBalance()
	{
		return balance;
	}

	public void setBalance(int balance)
	{
		this.balance = balance;
	}

}
