package com.david.nettydemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.david.common.CommonConstants;

public class BioClient
{
	public static void main(String[] args)
	{
		startBioClient();
	}

	public static void startBioClient()
	{
		Socket socket = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try
		{
			socket = new Socket(CommonConstants.LOCALHOST, CommonConstants.PORT);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println("QUERY TIME ORDER");
			System.out.println("send order to server succeed");

			String result = br.readLine();
			if (result != null)
			{
				System.out.println("Now is: " + result);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (pw != null)
			{
				pw.close();
				pw = null;
			}
			if (socket != null)
			{
				try
				{
					socket.close();
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				socket = null;
			}
		}
	}
}
