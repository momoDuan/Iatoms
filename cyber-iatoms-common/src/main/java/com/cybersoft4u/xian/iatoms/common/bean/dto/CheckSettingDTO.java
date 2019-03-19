package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

public class CheckSettingDTO extends DataTransferObject<String>{

	public static  final String TO_NAME ="toName";
	public static  final String TO_MAIL ="toMail";
	public static  final String FROM_MAIL ="fromMail";
	public static  final String FROM_NAME ="fromName";
	public static  final String MAIL_SUBJECT ="mailSubject";
	public static  final String MAIL_CONTEXT ="mailContext";
	
	/**
	 * 最小數量
	 */
	private Integer minNumber;
	/**
	 * 最大數量
	 */
	private Integer maxNumber;
	/**
	 * 核檢方式
	 */
	private String checkType;
	/**
	 * 核檢數量
	 */
	private Double checkValue;
	
	private Integer intervalValue;
	
	private Integer addValue;
	/**
	 * @return the minNumber
	 */
	public Integer getMinNumber() {
		return minNumber;
	}
	/**
	 * @param minNumber the minNumber to set
	 */
	public void setMinNumber(Integer minNumber) {
		this.minNumber = minNumber;
	}
	/**
	 * @return the maxNumber
	 */
	public Integer getMaxNumber() {
		return maxNumber;
	}
	/**
	 * @param maxNumber the maxNumber to set
	 */
	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}
	/**
	 * @return the checkType
	 */
	public String getCheckType() {
		return checkType;
	}
	/**
	 * @param checkType the checkType to set
	 */
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	/**
	 * @return the checkValue
	 */
	public Double getCheckValue() {
		return checkValue;
	}
	/**
	 * @param checkValue the checkValue to set
	 */
	public void setCheckValue(Double checkValue) {
		this.checkValue = checkValue;
	}
	/**
	 * @return the intervalValue
	 */
	public Integer getIntervalValue() {
		return intervalValue;
	}
	/**
	 * @param intervalValue the intervalValue to set
	 */
	public void setIntervalValue(Integer intervalValue) {
		this.intervalValue = intervalValue;
	}
	/**
	 * @return the addValue
	 */
	public Integer getAddValue() {
		return addValue;
	}
	/**
	 * @param addValue the addValue to set
	 */
	public void setAddValue(Integer addValue) {
		this.addValue = addValue;
	}
	
	
	
}
