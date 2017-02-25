package com.david.syncutils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 分行搜索者
 * @author dailiwei
 *
 */
public class Searcher implements Runnable
{
	private int startRow;
	private int endRow;
	private MatrixMock mock;
	private Result result;
	private int number;
	private final CyclicBarrier cyclicBarrier;

	public Searcher(int startRow, int endRow, MatrixMock mock, Result result, int number, CyclicBarrier cyclicBarrier)
	{
		super();
		this.startRow = startRow;
		this.endRow = endRow;
		this.mock = mock;
		this.result = result;
		this.number = number;
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run()
	{
		int counter;
		System.out.println(String.format("%s: Start the lines from %d to %d", Thread.currentThread().getName(), startRow, endRow));
		for (int row = startRow; row < endRow; row++)
		{
			counter = 0;
			int[] current = mock.getRow(row);
			for (int col = 0; col < current.length; col++)
			{
				if (current[col] == number)
				{
					counter++;
				}
			}

			result.setData(row, counter);
		}

		System.out.println(String.format("%s: End processed...", Thread.currentThread().getName()));
		
		try
		{
			cyclicBarrier.await();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
