package com.david.threadmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Waiting for the finalization of thread(use with join)
 * 
 * @author dailiwei
 * 
 */
public class DataSourcesLoader implements Runnable
{

	@Override
	public void run()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(CommonUtils.YYYY_MM_DD_HH_MM_SS);
		System.out.println(String.format("DataSourcesLoader has started... => %s", sdf.format(new Date())));

		try
		{
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e)
		{
			System.err.println("DataSourcesLoader is interrupted");
		}

		System.out.println(String.format("DataSourcesLoader has ended... => %s", sdf.format(new Date())));
	}

}
