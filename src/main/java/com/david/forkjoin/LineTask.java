package com.david.forkjoin;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class LineTask extends RecursiveTask<Integer>
{
	private static final long serialVersionUID = 1L;

	private String[] line;
	private int start, end;
	private String word;

	public LineTask(String[] line, int start, int end, String word)
	{
		super();
		this.line = line;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute()
	{
		int result = 0;
		if (end - start < 100)
		{
			result = count(line, start, end, word);
		} else
		{
			int middle = (start + end) / 2;
			LineTask task1 = new LineTask(line, start, middle, word);
			LineTask task2 = new LineTask(line, middle, end, word);
			invokeAll(task1, task2);
			
			try
			{
				result = groupResult(task1.get(), task2.get());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return result;
	}

	private Integer groupResult(int result1, int result2)
	{
		return result1 + result2;
	}

	private Integer count(String[] line, int start, int end, String word)
	{
		int counter = 0;
		for (int i = start; i < end; i++)
		{
			if (line[i].equalsIgnoreCase(word))
			{
				counter++;
			}
		}

		try
		{
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return counter;
	}

}
