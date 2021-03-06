package com.david.nettydemo.httpxml;

/**
 * 地址实体
 * 
 * @author David.dai
 * 
 */
public class Address {
	private String street1;

	private String street2;

	private String city;

	private String province;

	private String zipcode;

	private String country;

	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Address(String street1, String street2, String city, String province, String zipcode, String country) {
		super();
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.province = province;
		this.zipcode = zipcode;
		this.country = country;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [street1=" + street1 + ", street2=" + street2 + ", city=" + city + ", province=" + province
				+ ", zipcode=" + zipcode + ", country=" + country + "]";
	}

}
