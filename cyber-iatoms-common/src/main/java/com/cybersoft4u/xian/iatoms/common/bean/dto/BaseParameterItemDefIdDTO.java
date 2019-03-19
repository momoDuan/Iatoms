package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.CompositeIdentifier;

/**
 * Purpose:  基本參數項目表主鍵DTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年6月28日
 * @MaintenancePersonnel CrissZhang
 */
public class BaseParameterItemDefIdDTO extends CompositeIdentifier {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -236091924715649206L;

	/**
	 * 參數類型代碼
	 */
	private String bptdCode;
	
	/**
	 * 參數項目編號
	 */
	private String bpidId;
	
	/**
	 * 生效日期
	 */
	private Date effectiveDate;
	
	/**
	 * Constructor:無參構造函數
	 */
	public BaseParameterItemDefIdDTO() {
	}
	
	/**
	 * Constructor: 含參構造函數
	 */
	public BaseParameterItemDefIdDTO(String bptdCode, String bpidId, Date effectiveDate) {
		this.bptdCode = bptdCode;
		this.bpidId = bpidId;
		this.effectiveDate = effectiveDate;
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
