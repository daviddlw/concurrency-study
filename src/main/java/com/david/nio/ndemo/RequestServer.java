package com.david.nio.ndemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.david.common.CommonConstants;
import com.david.dto.User;
import com.david.utils.HessianSerializeUtils;

public class RequestServer {
	public static void main(String[] args) {
		startServer();
	}

	public static void startServer() {
		Selector selector = null;
		ServerSocketChannel serverSocketChannel = null;

		try {
			// 打开选择器一旦有通道有消息，进行接收
			selector = Selector.open();
			InetSocketAddress address = new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT);
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);

			serverSocketChannel.socket().bind(address);
			serverSocketChannel.socket().setReuseAddress(true);

			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			while (selector.select() > 0) {
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					keys.remove();
					execute(key);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void execute(SelectionKey selectionKey) {
		ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
		try {
			System.out.println("server start...");
			SocketChannel socketChannel = ssc.accept();

			// 接收数据
			User user = readData(socketChannel);
			System.out.println(String.format("server: %s received data: [%s] from client: %s", ssc.getLocalAddress(), user,
					socketChannel.getRemoteAddress()));

			// 发送数据
			User newUser = new User(user.getUid() * 100, user.getName() + "修改后");
			sendData(socketChannel, newUser);
			System.out.println(String.format("server: %s send data: [%s] to client: %s", ssc.getLocalAddress(), newUser,
					socketChannel.getRemoteAddress()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static User readData(SocketChannel channel) {
		User user = new User();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int size = 0;
		byte[] data;
		byte[] result;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			while ((size = channel.read(buffer)) >= 0) {
				buffer.flip();
				data = new byte[size];
				buffer.get(data);
				baos.write(data);
				buffer.clear();
			}

			result = baos.toByteArray();
			if (result != null && result.length > 0) {
				user = (User) HessianSerializeUtils.deserialize(result);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}

	public static void sendData(SocketChannel channel, User user) {
		try {
			byte[] data = HessianSerializeUtils.serialize(user);
			ByteBuffer buffer = ByteBuffer.wrap(data);
			channel.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
