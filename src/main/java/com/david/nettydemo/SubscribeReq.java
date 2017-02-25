package com.david.nettydemo;

import java.io.Serializable;

public class SubscribeReq implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int subReqID;
	private String userName;
	private String projectName;
	private String phoneNumber;
	private String address;

	public SubscribeReq()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public SubscribeReq(int subReqID, String userName, String projectName, String phoneNumber, String address)
	{
		super();
		this.subReqID = subReqID;
		this.userName = userName;
		this.projectName = projectName;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public int getSubReqID()
	{
		return subReqID;
	}

	public void setSubReqID(int subReqID)
	{
		this.subReqID = subReqID;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	@Override
	public String toString()
	{
		return "SubscribeReq [subReqID=" + subReqID + ", userName=" + userName + ", projectName=" + projectName
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
	}

}
