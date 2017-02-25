package com.david.nio.ndemo;

import java.io.Serializable;
import java.util.Arrays;

public class MyResponseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String value;
	private byte[] data;

	public MyResponseObject() {
		super();
		name = "";
		value = "";
		data = new byte[1024];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "MyResponseObject [name=" + name + ", value=" + value + ", data=" + Arrays.toString(data) + "]";
	}

}
