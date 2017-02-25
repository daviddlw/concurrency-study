package com.david.nettydemo.httpxml;

/**
 * 订单实体
 * @author David.dai
 *
 */
public class Order {

	private int purchaseCount;

	private Customer customer;

	private Address billAddress;

	private Shipping deliveryType;

	private Address deliveryAddress;

	private float totalPrice;

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(int purchaseCount, Customer customer, Address billAddress, Shipping deliveryType,
			Address deliveryAddress, float totalPrice) {
		super();
		this.purchaseCount = purchaseCount;
		this.customer = customer;
		this.billAddress = billAddress;
		this.deliveryType = deliveryType;
		this.deliveryAddress = deliveryAddress;
		this.totalPrice = totalPrice;
	}

	public int getPurchaseCount() {
		return purchaseCount;
	}

	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(Address billAddress) {
		this.billAddress = billAddress;
	}

	public Shipping getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Shipping deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return "Order [purchaseCount=" + purchaseCount + ", customer=" + customer + ", billAddress=" + billAddress
				+ ", deliveryType=" + deliveryType + ", deliveryAddress=" + deliveryAddress + ", totalPrice="
				+ totalPrice + "]";
	}

}
