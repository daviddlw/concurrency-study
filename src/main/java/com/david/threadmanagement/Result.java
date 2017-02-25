package com.david.threadmanagement;

public class Result
{
	private String name;

	public Result()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Result(String name)
	{
		super();
		this.name = name;
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
		return "Result [name=" + name + "]";
	}

}
