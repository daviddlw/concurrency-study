package com.david.nio.ndemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

import com.david.common.CommonConstants;
import com.david.dto.User;
import com.david.utils.HessianSerializeUtils;

public class RequestClient {

	public static void main(String[] args) {
		startClient();
	}

	public static void startClient() {
		for (int i = 1; i <= 1; i++) {
			new Thread(new RequestClientRunnable(i)).start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class MyRunnable implements Runnable {

	private final int idx;

	public MyRunnable(int idx) {
		this.idx = idx;
	}

	public void run() {
		SocketChannel socketChannel = null;
		try {
			socketChannel = SocketChannel.open();
			SocketAddress socketAddress = new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT);
			socketChannel.connect(socketAddress);

			User user = new User(idx, "request_" + idx);
			sendData(socketChannel, user);

			User newUser = receiveData(socketChannel);
			System.out.println(newUser);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				socketChannel.close();
			} catch (Exception ex) {
			}
		}
	}

	private void sendData(SocketChannel socketChannel, User user) throws IOException {
		byte[] bytes = HessianSerializeUtils.serialize(user);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		socketChannel.write(buffer);
		socketChannel.socket().shutdownOutput();
	}

	private User receiveData(SocketChannel socketChannel) throws IOException {
		User user = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			byte[] bytes;
			int count = 0;
			while ((count = socketChannel.read(buffer)) >= 0) {
				buffer.flip();
				bytes = new byte[count];
				buffer.get(bytes);
				baos.write(bytes);
				buffer.clear();
			}
			bytes = baos.toByteArray();
			Object obj = HessianSerializeUtils.deserialize(bytes);
			user = (User) obj;
			socketChannel.socket().shutdownInput();
		} finally {
			try {
				baos.close();
			} catch (Exception ex) {
			}
		}
		return user;
	}
}

class RequestClientRunnable implements Runnable {

	private int index;

	public RequestClientRunnable(int index) {
		super();
		this.index = index;
	}

	@Override
	public void run() {
		Selector selector = null;
		SocketChannel socketChannel = null;
		InetSocketAddress address = new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT);
		try {
			System.out.println("client-" + index + " start...");

			// selector = Selector.open();
			socketChannel = SocketChannel.open();
			boolean isConnect = socketChannel.connect(address);
			// socketChannel.configureBlocking(false);
			// SelectionKey selectionKey = socketChannel.register(selector,
			// SelectionKey.OP_CONNECT);

			System.out.println("isConnect: " + isConnect);

			// 发送数据
			User user = new User(index, "姓名-" + index);
			sendData(socketChannel, user);
			// 接收数据
			User resultUser = receivedData(socketChannel);
			System.out.println("client received data: " + resultUser);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendData(SocketChannel socketChannel, User user) {
		byte[] data;
		try {
			data = HessianSerializeUtils.serialize(user);
			if (data != null) {
				ByteBuffer buffer = ByteBuffer.wrap(data);
				socketChannel.write(buffer);
				socketChannel.socket().shutdownOutput();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static User receivedData(SocketChannel socketChannel) {
		User user = new User();
		byte[] data;
		byte[] result;
		int size = 0;
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			if (socketChannel.isConnectionPending()) {
				socketChannel.finishConnect();
			}
			
			while ((size = socketChannel.read(buffer)) >= 0) {
				buffer.flip();
				data = new byte[size];
				buffer.get(data);
				baos.write(data);
				buffer.clear();
			}

			result = baos.toByteArray();
			user = (User) HessianSerializeUtils.deserialize(result);

			socketChannel.socket().shutdownInput();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
