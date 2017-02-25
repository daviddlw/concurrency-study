package com.david.nio.custom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ClientApp extends BaseApp implements Runnable {

	// 空闲计数器,如果空闲超过10次,将检测server是否中断连接.
	private static int idleCounter = 0;
	private Selector selector;
	private SocketChannel socketChannel;
	private ByteBuffer temp = ByteBuffer.allocate(1024);

	public static void main(String[] args) {

		try {
			ClientApp clientApp = new ClientApp();
			new Thread(clientApp).start();
			System.out.println("client start...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ClientApp() throws IOException {
		super();
		selector = Selector.open();
		socketChannel = SocketChannel.open();
		Boolean isConnected = socketChannel.connect(new InetSocketAddress(HOST, PORT));
		socketChannel.configureBlocking(false);

		SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);
		if (isConnected) {
			this.sendFirstMsg();
		} else {
			key.interestOps(SelectionKey.OP_CONNECT);
		}
	}

	public void sendFirstMsg() throws IOException {
		String msg = "Hello, NIO.";
		socketChannel.write(ByteBuffer.wrap(msg.getBytes(Charset.forName(UTF_8))));
	}

	@Override
	public void run() {
		while (true) {
			try {
				int num = this.selector.select(1000);
				if (num == 0) {
					idleCounter++;
					if (idleCounter > 10) {
						try {
							this.sendFirstMsg();
						} catch (ClosedChannelException e) {
							e.printStackTrace();
							this.socketChannel.close();
							return;
						}
					}
					continue;
				} else {
					idleCounter = 0;
				}
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectionKeys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					if (key.isConnectable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						if (sc.isConnectionPending()) {
							sc.finishConnect();
						}
						this.sendFirstMsg();
					}

					if (key.isReadable()) {
						SocketChannel sc = (SocketChannel) key.channel();
//						this.temp = ByteBuffer.allocate(1024);
						int count = sc.read(temp);
						if (count < 0) {
							sc.close();
							continue;
						}

						temp.flip();
						String msg = Charset.forName(UTF_8).decode(temp).toString();
						System.out.println(String.format("Client received [%s] from server address: %s", msg, sc.getRemoteAddress()));
						TimeUnit.SECONDS.sleep(1);
						sc.write(ByteBuffer.wrap(msg.getBytes(Charset.forName(UTF_8))));
						temp.clear();
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
