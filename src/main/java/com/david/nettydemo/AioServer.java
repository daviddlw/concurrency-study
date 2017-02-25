package com.david.nettydemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AioServer
{
	public static void main(String[] args)
	{
		startAioServer();
	}

	private static void startAioServer()
	{
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new AsyncTimeServerHandler());
	}
}




