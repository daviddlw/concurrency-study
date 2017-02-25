package com.david.nio.ndemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.david.common.CommonConstants;

/**
 * 回写服务器
 * 
 * @author dailiwei
 *
 */
public class MyEchoServer {
	private static Random rand = new Random();
	private static Map<String, Integer> resultMap = new HashMap<String, Integer>();
	private static Logger logger = Logger.getLogger(MyEchoServer.class);

	public static void main(String[] args) {
		startServer();
	}

	public static void startServer() {
		ServerSocketChannel ssc = null;
		Selector selector = null;
		InetSocketAddress address = new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT);
		ByteBuffer buffer = ByteBuffer.allocate(CommonConstants.B1024);

		try {
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ServerSocket sc = ssc.socket();
			sc.bind(address);

			selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			logger.info("sever start...");

			while (true) {
				int num = rand.nextInt(10);
				if (selector.select() > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> it = keys.iterator();
					while (it.hasNext()) {
						SelectionKey selectionKey = it.next();
						if (selectionKey.isAcceptable()) {
							it.remove();
							// 如果是新连接上来的事件则接受，并且给这个channel注册读取事件
							ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
							SocketChannel socketChannel = serverSocketChannel.accept();
							logger.info("server connect success...");

							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ);

							logger.info(String.format("a new connection is connected: %s", socketChannel.getRemoteAddress()));
							resultMap.put(socketChannel.getRemoteAddress().toString(), num);

						} else if (selectionKey.isReadable()) {
							logger.info("server readable...");
							SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							int size = 0;
							byte[] bytes;

							while ((size = socketChannel.read(buffer)) > 0) {
								buffer.flip();
								bytes = new byte[size];
								buffer.get(bytes);
								baos.write(bytes);
								buffer.clear();
							}
							
							String s = new String(baos.toByteArray(), Charset.forName(CommonConstants.UTF_8));
							String message = "receive data: " + s.toString() + ", random num: " + num;
							logger.info(message);
							socketChannel.write(ByteBuffer.wrap(message.getBytes(Charset.forName(CommonConstants.UTF_8))));

						}
					}
				}
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
