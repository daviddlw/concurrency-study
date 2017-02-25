package com.david.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client2App
{
	public static void main(String[] args)
	{
		readData();
	}

	public static void readData()
	{
		SocketChannel sc;
		try
		{
			sc = SocketChannel.open();
			boolean isConnect = sc.connect(new InetSocketAddress("www.baidu.com", 80));
			sc.configureBlocking(false);
			if (isConnect)
			{
				while (sc.finishConnect())
				{
					ByteBuffer buffer = ByteBuffer.allocate(128);
					int count = sc.read(buffer);
					if (count > 0)
					{
						StringBuilder sb = new StringBuilder();
						buffer.flip();
						while (buffer.hasRemaining())
						{
							String msg = Charset.forName("UTF-8").decode(buffer).toString();
							sb.append(msg);
						}
						System.out.println(sb.toString());
						buffer.clear();
					}		
				}
						
			} else
			{
				System.out.println("连接成功...");
			}

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
