package com.david.nio.custom;

public class AnothorClient {
	public static void main(String[] args) {
		try {
			Client client = new Client();
			new Thread(client).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
