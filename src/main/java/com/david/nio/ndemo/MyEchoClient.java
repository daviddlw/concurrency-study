package com.david.nio.ndemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.david.common.CommonConstants;

public class MyEchoClient extends MyBaseClient {

	private static Logger logger = Logger.getLogger(MyEchoServer.class);

	public static void main(String[] args) {
		startClient();
	}

	public static void startClient() {
		try {
			SocketChannel sc = SocketChannel.open();
			Selector selector = Selector.open();
			boolean isConnected = sc.connect(new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT));
			logger.info(String.format("connect to server: %s", isConnected));
			sc.configureBlocking(false);
			SelectionKey selectionKey = sc.register(selector, SelectionKey.OP_READ);

			if (isConnected) {
				new Thread(new SendRunnable(sc, selector)).start();
				new Thread(new ReadRunnable(selector)).start();

			} else {
				selectionKey.interestOps(SelectionKey.OP_CONNECT);
				if (selector.select(1000) > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> it = keys.iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						if (key.isConnectable()) {
							SocketChannel socketChannel = (SocketChannel) key.channel();
							if (socketChannel.isConnectionPending()) {
								socketChannel.finishConnect();
							}

							sendMessage(selector, sc, System.in);
						}
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class SendRunnable extends MyEchoClient implements Runnable {
	private SocketChannel sc;
	private Selector selector;

	public SendRunnable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SendRunnable(SocketChannel sc, Selector selector) {
		super();
		this.sc = sc;
		this.selector = selector;
	}

	@Override
	public void run() {
		sendMessage(selector, sc, System.in);
	}

}

class ReadRunnable extends MyEchoClient implements Runnable {

	private Selector selector;

	public ReadRunnable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReadRunnable(Selector selector) {
		super();
		this.selector = selector;
	}

	@Override
	public void run() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			if (selector.select() > 0) {
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					if (key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
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
						System.out.println(s);
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
