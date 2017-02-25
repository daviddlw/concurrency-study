package com.david.customizingconcurrency;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class SleepTwoSecondsTask implements Callable<String>
{

	@Override
	public String call() throws Exception
	{
		System.out.println("running SleepTwoSecondsTask");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = sdf.format(new Date());
		return result;
	}

}
