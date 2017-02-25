package com.david.syncutils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DbTwoTask implements Callable<List<User>>
{
	private final CountDownLatch latch;

	public DbTwoTask(CountDownLatch latch)
	{
		super();
		this.latch = latch;
	}

	@Override
	public List<User> call() throws Exception
	{
		System.out.println("execute dbtwo task for 5 seconds...");
		TimeUnit.SECONDS.sleep(3);
		List<User> users = Arrays.asList(new User[] { new User(4, "helloword"), new User(5, "sharpingwang"),
				new User(6, "fangwa"), new User(7, "dadiao") });
		
		System.out.println("dbtwo task finish...");
		latch.countDown();

		return users;
	}

}
