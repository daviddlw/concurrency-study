package com.david.nettydemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

import com.david.common.CommonConstants;

public class AsyncTimeServerHandler implements Runnable
{
	private CountDownLatch latch;
	private AsynchronousServerSocketChannel asyncSsc;

	public AsyncTimeServerHandler()
	{
		super();
		try
		{
			asyncSsc = AsynchronousServerSocketChannel.open();
			asyncSsc.bind(new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT));
			System.out.println("async server started now：[" + asyncSsc.getLocalAddress() + "]");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public AsynchronousServerSocketChannel getAsyncSsc()
	{
		return asyncSsc;
	}

	public void setAsyncSsc(AsynchronousServerSocketChannel asyncSsc)
	{
		this.asyncSsc = asyncSsc;
	}

	public CountDownLatch getLatch()
	{
		return latch;
	}

	public void setLatch(CountDownLatch latch)
	{
		this.latch = latch;
	}

	public void doAccept()
	{
		asyncSsc.accept(this, new AcceptCompletionHandler());
	}

	@Override
	public void run()
	{
		try
		{
			latch = new CountDownLatch(1);
			doAccept();
			latch.await();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
