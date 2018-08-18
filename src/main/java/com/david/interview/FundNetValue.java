package com.david.interview;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FundNetValue {
	/** 净值日期 */
	private static final String YYYY_MM_DD_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_MM_SS);
	
	private Date netValueDate;
	private BigDecimal netValue;
	public FundNetValue(Date netValueDate, BigDecimal netValue) {
		super();
		this.netValueDate = netValueDate;
		this.netValue = netValue;
	}
	public Date getNetValueDate() {
		return netValueDate;
	}
	public void setNetValueDate(Date netValueDate) {
		this.netValueDate = netValueDate;
	}
	public BigDecimal getNetValue() {
		return netValue;
	}
	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}
	@Override
	public String toString() {
		return "FundNetValue [netValueDate=" + sdf.format(netValueDate) + ", netValue=" + netValue + "]";
	}

}
