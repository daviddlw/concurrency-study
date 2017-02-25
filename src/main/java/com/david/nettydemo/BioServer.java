package com.david.nettydemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.david.common.CommonConstants;

public class BioServer
{
	public static void main(String[] args)
	{
		startBioServer();
	}

	public static void startBioServer()
	{
		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		try (ServerSocket serverSocket = new ServerSocket(CommonConstants.PORT))
		{
			System.out.println("The time server is start at port " + CommonConstants.PORT);
			Socket socket = null;
			while (true)
			{
				socket = serverSocket.accept();
				if (socket != null)
				{
					exec.execute(new TimeServer(socket));
					System.out.println("a task is running");
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

class TimeServer implements Runnable
{

	private Socket socket;

	public TimeServer()
	{
		super();
	}


	public TimeServer(Socket socket)
	{
		super();
		this.socket = socket;
	}

	@Override
	public void run()
	{
		BufferedReader br = null;
		PrintWriter pw = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			pw = new PrintWriter(this.socket.getOutputStream(), true);
			String currentTime = null;
			String body = null;
			while (true)
			{
				body = br.readLine();
				if (body == null)
				{
					break;
				}
				currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis())
						.toString() : "BAD ORDER";
				pw.println(currentTime);

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
					this.socket.close();
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.socket = null;
			}
		}
	}
}
