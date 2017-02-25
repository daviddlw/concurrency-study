package com.david.customizingconcurrency;

import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchNumberTask extends RecursiveAction
{

	private static final long serialVersionUID = 1L;
	private int start;
	private int end;
	private int number;
	private int index;
	private AtomicBoolean isFind;
	private List<Integer> list;

	public SearchNumberTask()
	{
		super();
		isFind = new AtomicBoolean(false);
		// TODO Auto-generated constructor stub
	}

	public SearchNumberTask(int start, int end, List<Integer> list, int number)
	{
		super();
		this.start = start;
		this.end = end;
		this.number = number;
		this.list = list;
	}

	@Override
	protected void compute()
	{
		if (end - start < 1000)
		{
			// if (isFind = false)
			// {
			search(start, end, number);
			// }
		} else
		{
			int middle = (end - start) / 2;
			SearchNumberTask startTask = new SearchNumberTask(start, middle + 1, list, number);
			SearchNumberTask endTask = new SearchNumberTask(middle + 1, end, list, number);
			invokeAll(startTask, endTask);
		}
	}

	private void search(int start, int end, int number)
	{
		for (int i = start; i < end; i++)
		{
			if (list.get(i) == number)
			{
				index = i;
				break;
			}
		}

		if (index > 0)
		{
			System.out.println("所查找的数据在索引： " + index);
		}
	}

}
