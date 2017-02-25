package com.david.threadmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UnsafeTask implements Runnable
{
	private Date startDate;

	@Override
	public void run()
	{
		startDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(CommonUtils.YYYY_MM_DD_HH_MM_SS);
		System.out.println(String.format("Starting thread - %d : %s", Thread.currentThread().getId(), sdf.format(startDate)));
		
		try
		{
			int time = (int)Math.rint(Math.random()*10);
			TimeUnit.SECONDS.sleep(time);			
			
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.err.println(String.format("Thread finished - %d : %s", Thread.currentThread().getId(), sdf.format(startDate)));
	}

}
