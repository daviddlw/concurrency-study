package com.david.threadmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NetworkConnectionsLoader implements Runnable
{

	@Override
	public void run()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(CommonUtils.YYYY_MM_DD_HH_MM_SS);
		System.out.println(String.format("NetworkConnectionsLoader has started... => %s", sdf.format(new Date())));

		try
		{
			TimeUnit.SECONDS.sleep(6);
		} catch (InterruptedException e)
		{
			System.err.println("NetworkConnectionsLoader is interrupted");
		}
		
		System.out.println(String.format("NetworkConnectionsLoader has ended... => %s", sdf.format(new Date())));
	}

}
