package com.david.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class DocumentTask extends RecursiveTask<Integer>
{
	private String document[][];
	private int start, end;
	private String word;

	public DocumentTask(String[][] document, int start, int end, String word)
	{
		super();
		this.document = document;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Integer compute()
	{
		int result = 0;
		if (end - start < 10)
		{
			result = processLines(document, start, end, word);
		} else
		{
			int middle = (start + end) / 2;
			DocumentTask task1 = new DocumentTask(document, start, middle, word);
			DocumentTask task2 = new DocumentTask(document, middle, end, word);

			invokeAll(task1, task2);

			try
			{
				result = groupResult(task1.get(), task2.get());
			} catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	private Integer processLines(String[][] document, int start, int end, String word)
	{
		List<LineTask> tasks = new ArrayList<>();
		for (int i = start; i < end; i++)
		{
			LineTask lineTask = new LineTask(document[i], 0, document[i].length, word);
			tasks.add(lineTask);
		}

		invokeAll(tasks);

		int result = 0;
		for (LineTask lineTask : tasks)
		{
			try
			{
				result += lineTask.get();
			} catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	private int groupResult(int result1, int result2)
	{
		return result1 + result2;
	}

}
