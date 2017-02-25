package com.david.threadexecutors;

import java.util.Date;
import java.util.concurrent.Callable;

import com.david.common.CommonUtils;

public class DelayCallable implements Callable<String>
{
	private String name;

	public DelayCallable(String name)
	{
		super();
		this.name = name;
	}

	@Override
	public String call() throws Exception
	{
		System.out.println("Starting at " + CommonUtils.sdf.format(new Date()));
		return "Hello World, " + name;
	}

}
