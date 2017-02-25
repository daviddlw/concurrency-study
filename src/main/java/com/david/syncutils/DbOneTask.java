package com.david.syncutils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DbOneTask implements Callable<List<User>>
{
	private final CountDownLatch latch;

	public DbOneTask(CountDownLatch latch)
	{
		super();
		this.latch = latch;
	}

	@Override
	public List<User> call() throws Exception
	{
		System.out.println("execute dbone task for 3 seconds...");
		TimeUnit.SECONDS.sleep(3);
		List<User> users = Arrays.asList(new User[] { new User(1, "daviddai"), new User(2, "redis"),
				new User(3, "mongodb") });
		
		System.out.println("dbone task finish...");
		latch.countDown();

		return users;
	}

}
