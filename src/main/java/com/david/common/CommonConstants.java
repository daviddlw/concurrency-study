package com.david.common;

import java.text.SimpleDateFormat;

public class CommonConstants
{

	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
	public static final String UTF_8 = "UTF-8";
	public static final String NIO_RW = "rw";
	public static final String LOCALHOST = "localhost";
	public static final String LOCALHOST_IP = "127.0.0.1";
	public static final int PORT = 10000;
	public static final int B1024 = 1024;
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
}
