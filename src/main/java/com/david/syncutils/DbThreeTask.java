package com.david.syncutils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DbThreeTask implements Callable<List<User>>
{
	private CountDownLatch latch;

	public DbThreeTask(CountDownLatch latch)
	{
		super();
		this.latch = latch;
	}

	@Override
	public List<User> call() throws Exception
	{
		System.out.println("execute dbone task for 6 seconds...");
		TimeUnit.SECONDS.sleep(6);
		List<User> users = Arrays.asList(new User[]{
			new User(8, "mazidacx5"),
			new User(9, "benz123")
		});
		
		System.out.println("dbthree task finish...");
		
		latch.countDown();
		
		return users;
	}

}
