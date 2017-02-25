package com.david.threadexecutors;

import java.util.concurrent.Callable;

public class TaskValidator implements Callable<String>
{
	private UserValidator validator;
	private String username;
	private String password;

	public TaskValidator(UserValidator validator, String username, String password)
	{
		super();
		this.validator = validator;
		this.username = username;
		this.password = password;
	}

	@Override
	public String call() throws Exception
	{
		if (!validator.validate(username, password))
		{
			System.out.println(String.format("%s: The user has not been found.", validator.getName()));
			throw new Exception("Error validating user...");
		}

		System.out.println(String.format("%s: The user has been found.", validator.getName()));

		return validator.getName();
	}

}
