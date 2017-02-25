package com.david.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static String[] splitStr2Array(String source, String spliter) {
		String regex = spliter;
		if ((regex.equals("?")) || (regex.equals("*")) || (regex.equals(")")) || (regex.equals("(")) || (regex.equals("{")) || (regex.equals("$"))
				|| (regex.equals("+")) || (regex.equals(".")) || (regex.equals("|"))) {
			regex = "[" + regex + "]";
		}
		return source.split(regex);
	}

	public static String byte2Hex(byte[] srcBytes) {
		StringBuilder hexRetSB = new StringBuilder();
		for (byte b : srcBytes) {
			String hexString = Integer.toHexString(0xFF & b);
			hexRetSB.append(hexString.length() == 1 ? Integer.valueOf(0) : "").append(hexString);
		}
		return hexRetSB.toString();
	}

	public static byte[] hex2Bytes(String source) {
		byte[] sourceBytes = new byte[source.length() / 2];
		for (int i = 0; i < sourceBytes.length; i++) {
			sourceBytes[i] = ((byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16));
		}
		return sourceBytes;
	}

}
