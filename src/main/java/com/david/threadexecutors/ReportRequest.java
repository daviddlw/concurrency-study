package com.david.threadexecutors;

import java.util.concurrent.CompletionService;

public class ReportRequest implements Runnable
{
	private String name;
	private CompletionService<String> service;

	public ReportRequest(String name, CompletionService<String> service)
	{
		super();
		this.name = name;
		this.service = service;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public CompletionService<String> getService()
	{
		return service;
	}

	public void setService(CompletionService<String> service)
	{
		this.service = service;
	}

	@Override
	public void run()
	{
		ReportGenerator generator = new ReportGenerator(name, "Report");
		service.submit(generator);
	}

}
