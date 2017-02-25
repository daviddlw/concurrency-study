package com.david.nio.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.david.common.CommonConstants;
import com.david.dto.User;

public class MySocketServer {

	public static void main(String[] args) {
		// startServer();
		// startServerByThreadPool();
		// startServerByThreads();
		// startServerObjectByThreadPool();
		startServerObjectByGzipThreadPool();
	}

	public static void startServerObjectByGzipThreadPool() {
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService exec = Executors.newFixedThreadPool(processors);
		try (ServerSocket serverSocket = new ServerSocket(CommonConstants.PORT)) {
			System.out.println("server gzip start...");
			while (true) {
				Socket socket = serverSocket.accept();
				socket.setSoTimeout(10 * 1000);
				exec.execute(new SocketObjectGzipRunnable(socket));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 可接受序列化对象
	 */
	public static void startServerObjectByThreadPool() {
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService exec = Executors.newFixedThreadPool(processors);
		try (ServerSocket serverSocket = new ServerSocket(CommonConstants.PORT)) {
			System.out.println("server object start...");
			while (true) {
				Socket socket = serverSocket.accept();
				exec.execute(new SocketObjectRunnable(socket));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 线程池启动socketserver
	 */
	public static void startServerByThreadPool() {
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService exec = Executors.newFixedThreadPool(processors);
		try (ServerSocket serverSocket = new ServerSocket(CommonConstants.PORT)) {
			System.out.println("server start...");
			while (true) {
				Socket socket = serverSocket.accept();
				String msg = String.format("server: %s to client: %s => ", serverSocket.getLocalSocketAddress(),
						socket.getRemoteSocketAddress());
				byte[] buffer = msg.getBytes(Charset.forName(CommonConstants.UTF_8));
				socket.getOutputStream().write(buffer);
				exec.execute(new SocketRunnable(socket));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 使用多线程方式启动server
	 */
	public static void startServerByThreads() {
		try (ServerSocket serverSocket = new ServerSocket(CommonConstants.PORT)) {
			System.out.println("server start...");
			while (true) {
				new Thread(new SocketRunnable(serverSocket.accept())).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 线程启动server但是只能忙碌接受一个client
	 */
	public static void startServer() {
		// 启动服务端口，监听来自客户端的socket链接
		try (ServerSocket serverSocket = new ServerSocket(CommonConstants.PORT)) {
			System.out.println("server start...");
			Socket socket = serverSocket.accept();
			// 将socket里面的输入流转换成bufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 将socket的输出流包装秤printwriter
			PrintWriter pw = new PrintWriter(socket.getOutputStream());

			while (true) {
				// 读出输入字符串
				String msg = br.readLine();
				System.out.println(msg);
				System.out.println("remote address: " + socket.getRemoteSocketAddress());
				pw.println("Server received: [" + msg + "]");
				pw.flush();
				if (msg.equals("quit")) {
					System.out.println("quit server...");
					break;
				}
			}

			socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class SocketObjectGzipRunnable implements Runnable {

	private Socket socket;

	public SocketObjectGzipRunnable(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		GZIPInputStream gzipIn = null;
		GZIPOutputStream gzipOut = null;
		ObjectInputStream objectIn = null;
		ObjectOutputStream objectOut = null;
		try {
			gzipIn = new GZIPInputStream(socket.getInputStream());
			objectIn = new ObjectInputStream(gzipIn);
			User user = (User) objectIn.readObject();
			if (user != null) {
				System.out.println(user);
				System.out.println(String.format("server received => data: %s", user));
			}

			gzipOut = new GZIPOutputStream(socket.getOutputStream());
			objectOut = new ObjectOutputStream(gzipOut);
			long timestamp = System.currentTimeMillis();
			User newUser = new User(timestamp, "测试" + timestamp);
			objectOut.writeObject(newUser);
			objectOut.flush();
			gzipOut.finish();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectIn != null) {
				try {
					objectIn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (objectOut != null) {
				try {
					objectOut.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

			if (gzipIn != null) {
				try {
					gzipIn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (gzipOut != null) {
				try {
					gzipOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

class SocketObjectRunnable implements Runnable {

	private Socket socket;

	public SocketObjectRunnable(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		ObjectInputStream objectIn = null;
		ObjectOutputStream objectOut = null;
		try {
			objectIn = new ObjectInputStream(socket.getInputStream());
			objectOut = new ObjectOutputStream(socket.getOutputStream());

			User user = (User) objectIn.readObject();
			if (user != null) {
				System.out.println("data from " + socket.getRemoteSocketAddress() + " + : " + user);
				user.setUid(System.currentTimeMillis());
				user.setName("测试名称：" + user.getName());

				objectOut.writeObject(user);
				objectOut.flush();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (objectIn != null) {
				try {
					objectIn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (objectOut != null) {
				try {
					objectOut.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}

/**
 * socket runnable
 * 
 * @author David.dai
 * 
 */
class SocketRunnable implements Runnable {

	private Socket socket;

	public SocketRunnable(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			// 将socket里面的输入流转换成bufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 将socket的输出流包装秤printwriter
			PrintWriter pw = new PrintWriter(socket.getOutputStream());

			while (true) {
				// 读出输入字符串
				String msg = br.readLine();
				System.out.println(msg);
				System.out.println("remote address: " + socket.getRemoteSocketAddress());
				pw.println("Server received: [" + msg + "]");
				pw.flush();
				if (msg.equals("quit")) {
					System.out.println("quit server...");
					break;
				}
			}

		} catch (Exception e) {

		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
