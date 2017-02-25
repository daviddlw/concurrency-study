package com.david.test.syncutils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.david.syncutils.MatrixMock;

public class SycnUtilsTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testMatrixMock()
	{
		MatrixMock mock = new MatrixMock(5, 5, 8);
		System.out.println(mock);
		System.out.println(Arrays.toString(mock.getRow(2)));
		FutureTask<String> f = new FutureTask<>(new Callable<String>() {

			@Override
			public String call() throws Exception
			{
				// TODO Auto-generated method stub
				return null;
			}
		});
		
//		ThreadPoolExecutor tpex = new ThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		ExecutorService ex = Executors.newSingleThreadExecutor();
		ex.submit(new RunnableFuture() {

			@Override
			public boolean cancel(boolean mayInterruptIfRunning)
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCancelled()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isDone()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Object get() throws InterruptedException, ExecutionException
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
					TimeoutException
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				
			}
		});
	}

}
