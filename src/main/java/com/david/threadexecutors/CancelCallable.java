package com.david.threadexecutors;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.david.common.CommonUtils;

/**
 * 取消任务
 * @author dailiwei
 *
 */
public class CancelCallable implements Callable<String>
{

	@Override
	public String call() throws Exception
	{
		while (true)
		{
			System.out.println(String.format("Task is running - %s", CommonUtils.sdf.format(new Date())));
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}

}
