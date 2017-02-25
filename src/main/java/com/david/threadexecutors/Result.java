package com.david.threadexecutors;

public class Result
{
	private int value;
	private String name;

	public Result()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Result(int value, String name)
	{
		super();
		this.value = value;
		this.name = name;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "Result [value=" + value + ", name=" + name + "]";
	}

}
