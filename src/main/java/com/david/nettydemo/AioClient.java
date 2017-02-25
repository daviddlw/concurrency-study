package com.david.nettydemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AioClient
{
	public static void main(String[] args)
	{
		startAioClient();
	}
	
	public static void startAioClient()
	{
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new AsyncTimeClientHandler());
	}
}
