package com.david.syncutils;

import java.util.Arrays;

/**
 * 保存单个查询结果
 * @author dailiwei
 *
 */
public class Result
{
	private int[] data;

	public Result(int size)
	{
		super();
		data = new int[size];
	}

	public int[] getData()
	{
		return data;
	}

	public void setData(int position, int value)
	{
		data[position] = value;
	}

	@Override
	public String toString()
	{
		return "Result [data=" + Arrays.toString(data) + "]";
	}

}
