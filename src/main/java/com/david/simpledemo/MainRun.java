package com.david.simpledemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class MainRun {
	private static final String LOCALHOST_STRING = "127.0.0.1";

	public static void main(String[] args) {
		try {
			System.out.println(getMacAddress(LOCALHOST_STRING));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取ip地址
	 * 
	 * @param ipAddress
	 * @return
	 * @throws IOException
	 */
	private static String getMacAddress(String ipAddress) throws IOException {
		String macAddress = "";
		String loopAddress = "127.0.0.1";
		if (loopAddress.equals(ipAddress)) {
			InetAddress inetAddress = InetAddress.getLocalHost();
			byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();

			StringBuilder sb = new StringBuilder();
			String s = "";
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				s = Integer.toHexString(mac[i] & 0xff);
				sb.append(s.length() == 1 ? "0" + s : s);
			}
			macAddress = sb.toString();

		} else {
			System.out.println(ipAddress);
			Process p = Runtime.getRuntime().exec("nbstat -A" + ipAddress);
			System.out.println("======process======");
			InputStreamReader inReader = new InputStreamReader(p.getInputStream());
			BufferedReader br = new BufferedReader(inReader);

			String str = "";
			while ((str = br.readLine()) != null) {
				if (str.indexOf("MAC") != -1) {
					macAddress = str.substring(str.indexOf("MAC") + 9, str.length());
					macAddress = macAddress.trim();
					System.out.println(macAddress);
					break;
				}
			}
			p.destroy();
			br.close();
			inReader.close();
		}

		return macAddress.toUpperCase();
	}
}
