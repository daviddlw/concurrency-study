package com.david.threadmanagement;

public class TaskRun implements Runnable
{

	@Override
	public void run()
	{
		String str = "aaa";
		int num = Integer.parseInt(str);
		System.out.println(num);
	}

}
