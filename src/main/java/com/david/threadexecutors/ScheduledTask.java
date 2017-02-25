package com.david.threadexecutors;

import java.util.Date;

import com.david.common.CommonUtils;

public class ScheduledTask implements Runnable
{

	@Override
	public void run()
	{
		System.out.println("A new task is running at " + CommonUtils.sdf.format(new Date()));
	}

}
