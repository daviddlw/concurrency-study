package com.david.nettydemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.david.common.CommonConstants;

public class NioServer {
	public static void main(String[] args) {
		startNioServer();
	}

	private static void startNioServer() {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new MultiplexerServer());
		// new Thread(new MultiplexerServer()).start();
	}
}

/**
 * 多路复用服务器
 * 
 * @author dailiwei
 * 
 */
class MultiplexerServer implements Runnable {
	private ServerSocketChannel ssc;
	private Selector selector;
	private volatile boolean stop;

	public MultiplexerServer() {
		super();
		try {
			selector = Selector.open();
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
//			ssc.socket().setReuseAddress(true);
			ssc.socket().bind(new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT),
					CommonConstants.B1024);
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			this.stop = false;
			System.out.println("nio server started at address[" + ssc.getLocalAddress() + "]");

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void stop() {
		stop = true;
	}

	@Override
	public void run() {
		while (!stop) {
			try {

				int num = selector.select(1000);
				if (num > 0) {
					Set<SelectionKey> selectorKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = selectorKeys.iterator();
					SelectionKey key = null;
					while (it.hasNext()) {
						key = it.next();
						it.remove();
						try {
							handleInput(key);
						} catch (Exception e) {
							if (key != null) {
								key.cancel();
								if (key.channel() != null) {
									key.channel().close();
								}
							}
						}

					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// 多路复用器关闭以后，所有注册在上面的channel与pipe都会被自动注册并且关闭
			// if (selector != null) {
			// try {
			// selector.close();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
		}

	}

	/**
	 * 处理输入连接
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			if (key.isAcceptable()) {
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept(); //设置新客户端连接的参数
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
				System.out.println("a connection [" + sc.getRemoteAddress()
						+ "] is established and it starts to register op-read option");
			}

			if (key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer byteBuffer = ByteBuffer.allocate(CommonConstants.B1024);
				int byteRead = sc.read(byteBuffer);
				if (byteRead > 0) {
					byteBuffer.flip();
					byte[] dataBytes = new byte[byteBuffer.remaining()];
					byteBuffer.get(dataBytes);
					String result = new String(dataBytes, Charset.forName(CommonConstants.UTF_8));
					if (result.equalsIgnoreCase("REQUEST ORDER TIME")) {
						System.out.println("received request order time succeed: [" + result + "]");
						doWrite(sc);
					} else {
						System.out.println("bad request order");
					}

				} else if (byteRead < 0) {
					key.cancel();
					sc.close();
				}

			}
		}
	}

	private void doWrite(SocketChannel sc) throws IOException {
		if (sc != null) {
			String data = "send from server(" + sc.getLocalAddress() + ") message";
			byte[] dataBytes = data.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(dataBytes.length);
			writeBuffer.put(dataBytes);
			writeBuffer.flip();//
			// put以后，当前操作位和大小是一样的，就是说当前操作的是数组最末尾。flip方法做的是，把最大限制给置到当前操作位，然后把当前操作位给置0。表示写完了
			sc.write(writeBuffer);
		} else {
			System.out.println("SockectChannel is null");
		}
	}
}