package com.david.nettydemo;

import java.io.Serializable;

public class User implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	private int userId;

	public User()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String userName, int userId)
	{
		super();
		this.userName = userName;
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "User [userName=" + userName + ", userId=" + userId + "]";
	}

}
