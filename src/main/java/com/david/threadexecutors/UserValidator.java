package com.david.threadexecutors;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UserValidator
{
	private String name;

	public UserValidator(String name)
	{
		super();
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean validate(String username, String password)
	{
		Random r = new Random();

		int duration = r.nextInt(6);

		try
		{
			System.out.println(String.format("Validator: %s: Validating a user duration %d seconds", name, duration));
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e)
		{
			return false;
		}

		return r.nextBoolean();
	}

}
