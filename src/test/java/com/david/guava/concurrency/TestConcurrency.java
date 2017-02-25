package com.david.guava.concurrency;

import static org.junit.Assert.*;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class TestConcurrency {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger logger = Logger.getLogger(TestConcurrency.class);

	@Test
	public void testListenableExecutorService() {
		ListeningExecutorService service = MoreExecutors
				.listeningDecorator(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
		service.execute(new Runnable() {

			@Override
			public void run() {
				System.out.println("execute action: " + sdf.format(new Date()));
			}
		});
	}

	@Test
	public void testListenableFuture() {
		ListeningExecutorService service = MoreExecutors
				.listeningDecorator(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

		ListenableFuture<String> future = service.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				String result = sdf.format(new Date());
				System.out.println("result: " + result);
				return result;
			}
		});

		Futures.addCallback(future, new FutureCallback<String>() {

			@Override
			public void onFailure(Throwable ex) {
				logger.error(ex.getMessage(), ex);
			}

			@Override
			public void onSuccess(String message) {
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
				System.out.println("deal with callback value: " + message);
			}
		});

	}

}
