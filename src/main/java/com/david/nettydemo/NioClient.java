package com.david.nettydemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.david.common.CommonConstants;

public class NioClient {
	public static void main(String[] args) {
		startNioClient();
	}

	private static void startNioClient() {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new HeartBeatClient());
		// new Thread(new HeartBeatClient()).start();
	}
}

/**
 * 心跳客户端
 * 
 * @author dailiwei
 * 
 */
class HeartBeatClient implements Runnable {
	private volatile boolean stop;
	private SocketChannel sc;
	private Selector selector;

	public HeartBeatClient() {
		super();
		try {
			selector = Selector.open();
			sc = SocketChannel.open();
			sc.configureBlocking(false);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void stop() {
		stop = true;
	}

	@Override
	public void run() {
		try {
			doConnect();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		while (!stop) {
			try {
				int num = selector.select(1000);
				if (num > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> it = keys.iterator();
					SelectionKey key = null;
					while (it.hasNext()) {
						key = it.next();
						it.remove();
						SocketChannel sc = (SocketChannel) key.channel();
						if (key.isValid()) {
							if (key.isConnectable() && sc.isConnectionPending()) {
								if (sc.finishConnect()) {
									sc.register(selector, SelectionKey.OP_READ);
									doWrite(sc);
								}
							} else {
								System.out.println("connect to server failed");
							}

							if (key.isReadable()) {
								ByteBuffer readBuffer = ByteBuffer.allocate(CommonConstants.B1024);
								int readLength = sc.read(readBuffer);
								if (readLength > 0) {
									readBuffer.flip();
									byte[] dataBytes = new byte[readBuffer.remaining()];
									readBuffer.get(dataBytes);
									String result = new String(dataBytes, Charset.forName(CommonConstants.UTF_8));
									System.out.println("received message from server [" + sc.getRemoteAddress()
											+ "], contents: [" + result + "]");

									this.stop = true;
								} else if (readLength < 0) {
									key.cancel();
									sc.close();
								}
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}

		}
	}

	private void doConnect() throws IOException {
		if (sc != null) {
			if (sc.connect(new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT))) {
				// 已连接，注册读时间，准备异步读取数据
				sc.register(selector, SelectionKey.OP_READ);
				doWrite(sc);
			} else {
				// 客户端已经发送sync，还没收到服务端的sync+ack确认，则管组连接
				sc.register(selector, SelectionKey.OP_CONNECT);
			}
		} else {
			System.out.println("SocketChannel is null");
		}
	}

	private void doWrite(SocketChannel sc) throws IOException {
		if (sc != null) {
			String data = "REQUEST ORDER TIME";
			byte[] dataBytes = data.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(dataBytes.length);
			writeBuffer.put(dataBytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
			if (!writeBuffer.hasRemaining()) {
				System.out.println("send order to server succeed");
			}
		}
	}

}
