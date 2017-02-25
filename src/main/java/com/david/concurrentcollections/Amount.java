package com.david.concurrentcollections;

import java.util.concurrent.atomic.AtomicLong;

public class Amount
{
	private AtomicLong balance;

	public Amount()
	{
		super();
		balance = new AtomicLong();
	}

	public long getBalance()
	{
		return balance.get();
	}

	public void setBalance(long balance)
	{
		this.balance.set(balance);
	}

	public void addAmount(long delta)
	{
		this.balance.addAndGet(delta);
	}

	public void substractAmount(long delta)
	{
		this.balance.addAndGet(-delta);
	}
}
