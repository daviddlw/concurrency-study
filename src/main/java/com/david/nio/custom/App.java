package com.david.nio.custom;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import com.david.common.CommonConstants;

public class App {
	public static void main(String[] args) {
		// nioRead();
		nioWrite();
	}

	public static String codeString(String dir, String fileName) {
		String code = null;
		try {
			File file = new File(dir + fileName);
			if (file == null || !file.exists()) {
				System.out.println("文件不存在..." + file.getAbsolutePath());
				return null;
			}

			BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
			int p = (bin.read() << 8) + bin.read();

			// 其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
			switch (p) {
			case 0xefbb:
				code = "UTF-8";
				break;
			case 0xfffe:
				code = "Unicode";
				break;
			case 0xfeff:
				code = "UTF-16BE";
				break;
			case 0x5c75:
				code = "ANSI|ASCII";
				break;
			default:
				code = "GBK";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return code;
	}

	/*
	 * nio write
	 */
	private static void nioWrite() {
		String data = "鹅鹅鹅，曲项向天歌，白毛浮绿水，红掌拨清波123";

		File file = new File("D:\\", "new.txt");
		if (!file.exists()) {
			System.out.println(file.length());
			try {
				file.delete();
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try (RandomAccessFile raf = new RandomAccessFile(file, CommonConstants.NIO_RW)) {
			writeWithByteBuffer(raf, data);
			System.err.println("撰写完毕!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeWithByteBuffer(RandomAccessFile raf, String data) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		FileChannel fc = raf.getChannel();
		buffer.clear();
		buffer.put(ByteBuffer.wrap(data.getBytes(Charset.forName(CommonConstants.UTF_8))));
		buffer.flip();

		while (buffer.hasRemaining()) {
			fc.write(buffer);
		}
		
		System.out.println(fc.size());
	}
	
	/**
	 * nio read
	 */
	private static void nioRead() {
		String mode = CommonConstants.NIO_RW;
		File file = new File("D:\\", "使用更新说明.txt");

		if (file.exists()) {

			RandomAccessFile raf = null;
			StringBuilder sb = new StringBuilder();
			Charset charset = Charset.forName(codeString("D:\\", "使用更新说明.txt"));
			CharsetDecoder decoder = charset.newDecoder();

			try {
				raf = new RandomAccessFile(file, mode);
				FileChannel fileChannel = raf.getChannel();
				ByteBuffer buffer = ByteBuffer.allocate(48);
				CharBuffer charBuffer = CharBuffer.allocate(48);
				int readLength = 0;
				do {
					// 从channel写到buffer
					readLength = fileChannel.read(buffer);
					System.err.println("Read fileLength: " + readLength);
					// 从写模式转回读模式
					buffer.flip();
					decoder.decode(buffer, charBuffer, false);
					charBuffer.flip();

					while (charBuffer.hasRemaining()) {
						sb.append(charBuffer.get());
					}

					charBuffer.clear();
					buffer.clear();

				} while (readLength != -1);
				System.out.println(sb.toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (raf != null)
						raf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("文件不存在...");
		}
	}
}
