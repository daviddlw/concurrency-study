package com.david.threadexecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ReportGenerator implements Callable<String>
{
	private String sender;
	private String title;

	public ReportGenerator(String sender, String title)
	{
		super();
		this.sender = sender;
		this.title = title;
	}

	public String getSender()
	{
		return sender;
	}

	public void setSender(String sender)
	{
		this.sender = sender;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	@Override
	public String call() throws Exception
	{
		int duration = (int) (Math.random() * 10);
		System.out.println(String.format("%s_%s, generating report duration %d seconds...", sender, title, duration));
		TimeUnit.SECONDS.sleep(duration);

		String info = sender + ": " + title;
		return info;
	}
}
