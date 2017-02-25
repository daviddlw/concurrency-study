package com.david.nettydemo.httpxml;

import java.util.List;

/**
 * 客户信息实体
 * 
 * @author David.dai
 * 
 */
public class Customer {
	private int id;
	private String firstName;
	private String lastName;
	private List<String> fullName;

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(int id, String firstName, String lastName, List<String> fullName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<String> getFullName() {
		return fullName;
	}

	public void setFullName(List<String> fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "Custom [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", fullName=" + fullName
				+ "]";
	}

}
