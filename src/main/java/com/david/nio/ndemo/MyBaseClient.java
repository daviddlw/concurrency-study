package com.david.nio.ndemo;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

import com.david.common.CommonConstants;

public class MyBaseClient {

	protected static void sendMessage(Selector selector, SocketChannel sc, InputStream in) {

		try (Scanner scanner = new Scanner(in)) {
			System.out.println("请输入值：");
			while (true) {
				if (scanner.hasNextLine()) {
					String s = scanner.nextLine();
					// 写相关信息
					sc.write(ByteBuffer.wrap(s.getBytes(Charset.forName(CommonConstants.UTF_8))));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
