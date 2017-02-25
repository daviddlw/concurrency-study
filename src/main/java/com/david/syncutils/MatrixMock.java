package com.david.syncutils;

import java.util.Arrays;
import java.util.Random;

/**
 * 生成一个随机矩阵
 * 
 * @author dailiwei
 * 
 */
public class MatrixMock
{
	private int row;
	private int col;
	private int num;
	private int counter;
	private Random rand;
	private int data[][];

	public MatrixMock(int row, int col, int num)
	{
		super();
		data = new int[row][col];
		rand = new Random();
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < col; j++)
			{
				data[i][j] = rand.nextInt(100);
				if (data[i][j] == num)
				{
					counter++;
				}
			}
		}

		System.out.println(String.format("Generated a matrix with %d rows, %d cols, total count of num(%d) is %d", row, col, num, counter));
	}

	public int[] getRow(int row)
	{
		if (row >= 0 && row < data.length)
			return data[row];
		
		return null;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < data.length; row++)
		{
			sb.append(Arrays.toString(data[row]) + "\n");
		}

		return sb.toString();
	}

}
