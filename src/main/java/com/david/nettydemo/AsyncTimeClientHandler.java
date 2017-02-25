package com.david.nettydemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

import com.david.common.CommonConstants;

public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable
{
	private AsynchronousSocketChannel asyncSc;
	private CountDownLatch latch;

	public AsyncTimeClientHandler()
	{
		super();
		try
		{
			asyncSc = AsynchronousSocketChannel.open();
			System.out.println("client open");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AsynchronousSocketChannel getAsyncSc()
	{
		return asyncSc;
	}

	public void setAsyncSc(AsynchronousSocketChannel asyncSc)
	{
		this.asyncSc = asyncSc;
	}

	public CountDownLatch getLatch()
	{
		return latch;
	}

	public void setLatch(CountDownLatch latch)
	{
		this.latch = latch;
	}

	@Override
	public void run()
	{
		latch = new CountDownLatch(1);
		asyncSc.connect(new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT), this, this);
		try
		{
			latch.await();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			try
			{
				asyncSc.close();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void completed(Void result, AsyncTimeClientHandler attachment)
	{
		String data = "REQUEST ORDER TIME";
		byte[] dataBytes = data.getBytes(Charset.forName(CommonConstants.UTF_8));
		ByteBuffer writeBuffer = ByteBuffer.allocate(dataBytes.length);
		writeBuffer.put(dataBytes);
		writeBuffer.flip();
		asyncSc.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

			@Override
			public void completed(Integer result, ByteBuffer buffer)
			{
				if (buffer.hasRemaining())
				{
					asyncSc.write(buffer, buffer, this);
				} else
				{
					ByteBuffer readBuffer = ByteBuffer.allocate(CommonConstants.B1024);
					asyncSc.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

						@Override
						public void completed(Integer result, ByteBuffer buffer)
						{
							buffer.flip();
							byte[] bytes = new byte[buffer.remaining()];
							buffer.get(bytes);
							String body = new String(bytes, Charset.forName(CommonConstants.UTF_8));
							try
							{
								System.out.println("receive data from sever[" + asyncSc.getRemoteAddress()
										+ "], contents:[" + body + "]");
							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							latch.countDown();
						}

						@Override
						public void failed(Throwable exc, ByteBuffer attachment)
						{
							try
							{
								asyncSc.close();
								latch.countDown();
							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer buffer)
			{
				try
				{
					asyncSc.close();
					latch.countDown();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void failed(Throwable exc, AsyncTimeClientHandler attachment)
	{
		exc.printStackTrace();
		try
		{
			asyncSc.close();
			latch.countDown();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
