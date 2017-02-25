package com.david.nettydemo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Date;

import com.david.common.CommonConstants;
import com.david.common.CommonUtils;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler>
{

	@Override
	public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment)
	{
		attachment.getAsyncSsc().accept(attachment, this);
		ByteBuffer byteBuffer = ByteBuffer.allocate(CommonConstants.B1024);
		result.read(byteBuffer, byteBuffer, new ReadCompletionHandler(result));
	}

	@Override
	public void failed(Throwable exc, AsyncTimeServerHandler attachment)
	{
		exc.printStackTrace();
		attachment.getLatch().countDown();
	}

}

/**
 * 异步读完毕的回调
 * 
 * @author dailiwei
 *
 */
class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer>
{
	private AsynchronousSocketChannel asyncSc;

	public ReadCompletionHandler(AsynchronousSocketChannel asyncSc)
	{
		super();
		if (this.asyncSc == null)
		{
			this.asyncSc = asyncSc;
		}

	}

	@Override
	public void completed(Integer result, ByteBuffer attachment)
	{
		attachment.flip();
		byte[] body = new byte[attachment.remaining()];
		attachment.get(body);
		try
		{
			String resultStr = new String(body, Charset.forName(CommonConstants.UTF_8));
			
			System.out.println(String.format("server [%s] accept a finish a read-opt from client [%s], contents: [%s]",
					asyncSc.getLocalAddress(), asyncSc.getRemoteAddress(), resultStr));
			if (resultStr.equalsIgnoreCase("REQUEST ORDER TIME"))
			{
				doWrite();
			} else
			{
				System.out.println("BAD REQUEST");
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment)
	{
		try
		{
			this.asyncSc.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doWrite() throws IOException
	{
		String data = CommonUtils.sdf.format(new Date());
		byte[] dataBytes = data.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(dataBytes.length);
		writeBuffer.put(dataBytes);
		writeBuffer.flip();
		asyncSc.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

			@Override
			public void completed(Integer result, ByteBuffer buffer)
			{
				//如果没有发完，继续发送
				if (buffer.hasRemaining())
				{
					asyncSc.write(buffer, buffer, this);
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment)
			{
				try
				{
					asyncSc.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// put以后，当前操作位和大小是一样的，就是说当前操作的是数组最末尾。flip方法做的是，把最大限制给置到当前操作位，然后把当前操作位给置0。表示写完了
	}

}
