package com.david.threadexecutors;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ReportProcessor implements Runnable
{
	private CompletionService<String> service;
	private boolean end;

	public ReportProcessor(CompletionService<String> service)
	{
		super();
		this.service = service;
		end = false;
	}

	@Override
	public void run()
	{
		while (!end)
		{
			try
			{
				Future<String> result = service.poll(20, TimeUnit.SECONDS);
				if (result != null)
				{
					String report = result.get();
					System.err.println("ReportReceiver: " + report);
				}

			} catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("ReportSender End...");
		}
	}

	public boolean isEnd()
	{
		return end;
	}

	public void setEnd(boolean end)
	{
		this.end = end;
	}

}
