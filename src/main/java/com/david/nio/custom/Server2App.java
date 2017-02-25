package com.david.nio.custom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import com.david.common.CommonConstants;

public class Server2App extends BaseApp {

	public static void main(String[] args) {
		startServer();
	}
	
	public static void startServer() {
		try {
			System.out.println("server start...");
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(HOST, PORT));
			ssc.configureBlocking(false);
			ByteBuffer buffer = ByteBuffer.allocate(48);

			while (true) {
				SocketChannel socketChannel = ssc.accept();
				if (socketChannel != null) {
					int count = socketChannel.read(buffer);
					if (count < 0) {
						socketChannel.close();
						return;
					}

					buffer.flip();
					String result = Charset.forName(CommonConstants.UTF_8).decode(buffer).toString();
					System.out.println(String.format("Server received [%s] from client - %s", result,
							socketChannel.getRemoteAddress()));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
