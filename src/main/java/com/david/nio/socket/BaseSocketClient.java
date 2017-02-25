package com.david.nio.socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.david.common.CommonConstants;
import com.david.dto.User;

public class BaseSocketClient {

	protected static void runDemo() {
		// startClient();
		// startObjectClient();
		startGzipObjectClient();
	}

	protected static void startGzipObjectClient() {
		for (int i = 0; i < 10; i++) {
			Socket socket = null;
			GZIPOutputStream gzipOut = null;
			ObjectOutputStream objectOut = null;
			GZIPInputStream gzipIn = null;
			ObjectInputStream objectIn = null;

			try {
				int timeout = 10 * 1000;
				socket = new Socket();
				socket.connect(new InetSocketAddress(CommonConstants.LOCALHOST, CommonConstants.PORT), timeout);
				socket.setSoTimeout(timeout);

				gzipOut = new GZIPOutputStream(socket.getOutputStream());
				objectOut = new ObjectOutputStream(gzipOut);
				User user = new User(i, "name_" + i);
				objectOut.writeObject(user);
				objectOut.flush();
				gzipOut.finish();

				gzipIn = new GZIPInputStream(socket.getInputStream());
				objectIn = new ObjectInputStream(gzipIn);
				User receivedUser = (User) objectIn.readObject();
				if (receivedUser != null) {
					System.out.println(receivedUser);
				}

				TimeUnit.SECONDS.sleep(1);

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

	protected static void startObjectClient() {
		ObjectOutputStream objectOut = null;
		ObjectInputStream objectIn = null;
		try (Socket socket = new Socket(CommonConstants.LOCALHOST, CommonConstants.PORT)) {
			System.out.println("start object client...");
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectOut.writeObject(new User(System.currentTimeMillis(), "测试名称" + System.currentTimeMillis()));
			objectOut.flush();

			InputStream in = socket.getInputStream();
			// byte[] bufferObj = new byte[in.available()];
			// in.read(bufferObj);
			// objectIn = new ObjectInputStream(new
			// ByteArrayInputStream(bufferObj));
			objectIn = new ObjectInputStream(new BufferedInputStream(in));
			User newUser = (User) objectIn.readObject();
			if (newUser != null) {
				System.out.println(newUser);
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
		}
	}

	protected static void startClient() {
		try (Socket socket = new Socket(CommonConstants.LOCALHOST, CommonConstants.PORT)) {
			System.out.println("start client...");
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader fsReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				String msg = reader.readLine();
				pw.println(msg);
				pw.flush();

				if (msg.equals("quit")) {
					System.out.println("quit client...");
					break;
				}

				String serverMsg = fsReader.readLine();
				System.err.println("serverMsg: " + serverMsg);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
