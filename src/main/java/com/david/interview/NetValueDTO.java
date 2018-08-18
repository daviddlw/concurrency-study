package com.david.interview;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class NetValueDTO {
	private Date date;
	private int index;
	private List<BigDecimal> values;
	public NetValueDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NetValueDTO(Date date, int index, List<BigDecimal> values) {
		super();
		this.date = date;
		this.index = index;
		this.values = values;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<BigDecimal> getValues() {
		return values;
	}
	public void setValues(List<BigDecimal> values) {
		this.values = values;
	}
	@Override
	public String toString() {
		return "NetValueDTO [date=" + date + ", index=" + index + ", values=" + values + "]";
	}
	
	
}
