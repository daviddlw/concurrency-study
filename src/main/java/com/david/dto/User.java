package com.david.dto;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long uid;
	private String name;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(long uid, String name) {
		super();
		this.uid = uid;
		this.name = name;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", name=" + name + "]";
	}

}
