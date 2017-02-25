package com.david.nio.custom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.david.common.CommonConstants;

public class Client2App extends BaseApp {

	public static void main(String[] args) {
		sendMessage();
	}
	
	public static void sendMessage() {
		try {
			SocketChannel sc = SocketChannel.open();
			sc.connect(new InetSocketAddress(HOST, PORT));
			sc.configureBlocking(false);
			String message = "";

			while (true) {
				message = CommonConstants.sdf.format(new Date());
				sc.write(ByteBuffer.wrap(message.getBytes(Charset.forName(CommonConstants.UTF_8))));
				TimeUnit.SECONDS.sleep(1);
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
