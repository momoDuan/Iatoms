package com.cybersoft4u.xian.iatoms.services.dmo;

import java.util.Date;

import cafe.core.bean.CompositeIdentifier;

/**
 * Purpose: BASE_PARAMETER_ITEM_DEF表DMO主鍵
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年6月28日
 * @MaintenancePersonnel ericdu
 */
public class BaseParameterItemDefId extends CompositeIdentifier {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -5457855325810395801L;
	
	private String bpidId;
	private String bptdCode;
	private Date effectiveDate;
	
	/**
	 * Constructor: 無參構造函數
	 */
	public BaseParameterItemDefId() {
		super();
	}
	
	/**
	 * Constructor: 含參構造函數
	 */
	public BaseParameterItemDefId(String bpidId, String bptdCode,
			Date effectiveDate) {
		super();
		this.bpidId = bpidId;
		this.bptdCode = bptdCode;
		this.effectiveDate = effectiveDate;
	}


	/**
	 * @return the bpidId
	 */
	public String getBpidId() {
		return bpidId;
	}
	/**
	 * @param bpidId the bpidId to set
	 */
	public void setBpidId(String bpidId) {
		this.bpidId = bpidId;
	}
	/**
	 * @return the bptdCode
	 */
	public String getBptdCode() {
		return bptdCode;
	}
	/**
	 * @param bptdCode the bptdCode to set
	 */
	public void setBptdCode(String bptdCode) {
		this.bptdCode = bptdCode;
	}
	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
