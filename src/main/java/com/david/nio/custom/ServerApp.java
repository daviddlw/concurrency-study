package com.david.nio.custom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.david.common.CommonConstants;

public class ServerApp extends BaseApp {
	private SelectLoop connectionBell;
	private SelectLoop readBell;
	private boolean isReadBellRunning = false;

	public static void main(String[] args) {
		try {
			// 启动server
			new ServerApp().startServer();
			System.out.println("server start...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startServer() throws IOException {
		// 初始化一个connection闹钟，处理连接事件
		connectionBell = new SelectLoop();
		// 初始化一个read闹钟，处理阅读时间
		readBell = new SelectLoop();

		// 打开一个serversocket
		ServerSocketChannel ssc = ServerSocketChannel.open();
		// 配置成非阻塞模式
		ssc.configureBlocking(false);

		// 绑定serversocket的地址与端口
		ServerSocket serverSocket = ssc.socket();
		serverSocket.bind(new InetSocketAddress(HOST, PORT));

		// 服务端socket ssc注册连接accect事件1<<4=16,SelectionKey.OP_ACCEPT
		ssc.register(connectionBell.getSelector(), SelectionKey.OP_ACCEPT);
		new Thread(connectionBell).start();

	}

	public class SelectLoop implements Runnable {

		private static final String UTF_8 = CommonConstants.UTF_8;
		private Selector selector;
		private ByteBuffer temp = ByteBuffer.allocate(1024);

		public SelectLoop() throws IOException {
			super();
			this.selector = Selector.open(); //打开选择器
		}

		public Selector getSelector() {
			return this.selector;
		}

		@Override
		public void run() {
			while (true) {
				try {
					// 阻塞到当有一个注册事件发生了
					this.selector.select();
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = selectionKeys.iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						this.dispatch(key);
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

		private void dispatch(SelectionKey key) throws IOException, InterruptedException {
			if (key.isAcceptable()) {
				// 这是一个connection accept事件，并且是注册在
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel socketChannel = ssc.accept();
				socketChannel.configureBlocking(false);
				socketChannel.register(readBell.getSelector(), SelectionKey.OP_READ);

				synchronized (ServerApp.class) {
					if (!ServerApp.this.isReadBellRunning) {
						ServerApp.this.isReadBellRunning = true;
						new Thread(readBell).start();
					}
				}
			} else if (key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				int count = sc.read(temp);
				if (count < 0) {
					key.cancel();
					sc.close();
					return;					
				}

				temp.flip();
				String msg = Charset.forName(UTF_8).decode(temp).toString();
				System.out.println(String.format("Server received [%s] from client address: %s", msg, sc.getRemoteAddress()));
				TimeUnit.SECONDS.sleep(1);

				// SimpleDateFormat sdf = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// msg = String.format("replay message: %s - %s",
				// "david server", sdf.format(new Date()));
				sc.write(ByteBuffer.wrap(msg.getBytes(Charset.forName(UTF_8))));

				temp.clear();
			}
		}
	}
}
