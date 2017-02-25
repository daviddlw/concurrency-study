package com.david.threadmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Sleeping and Resuming a thread
 * @author dailiwei
 *
 */
public class FileClock implements Runnable
{

	@Override
	public void run()
	{
		for (int i = 0; i < 10; i++)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(String.format("%s", sdf.format(new Date())));
			try
			{
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e)
			{
				System.err.println("The FileLock has been interrupted...");
			}
		}
	}

}
