package com.david.nettydemo.httpxml;

/**
 * 邮递方式枚举
 * 
 * @author David.dai
 * 
 */
public enum Shipping {

	OrdinaryExpress(1, "普通快递"),

	ZhaijiSong(2, "宅急送"),

	InternationalMail(3, "国际邮递"),

	DomesticExpress(4, "国内快递"),

	InternationalExpress(5, "国际快递");

	private Shipping(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private int code;

	private String desc;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
