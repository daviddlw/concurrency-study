package com.david.synchronization;

public class FileMock
{
	private String[] contents;
	private int index;

	public FileMock(int size, int length)
	{
		contents = new String[size];
		for (int i = 0; i < size; i++)
		{
			StringBuilder sb = new StringBuilder(length);
			for (int j = 0; j < length; j++)
			{
				int item = (int) Math.random() * 255;
				sb.append((char) item);
			}
			contents[i] = sb.toString();
		}
		index = 0;
	}

	public boolean hasMoreLines()
	{
		return index < contents.length;
	}

	public String getLine()
	{
		if (hasMoreLines())
		{
			System.out.println("Mock: " + (contents.length - index));
			return contents[index++];
		}

		return null;
	}

}
