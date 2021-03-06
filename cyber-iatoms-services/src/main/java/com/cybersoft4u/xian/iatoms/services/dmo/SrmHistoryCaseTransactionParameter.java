package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;

// Generated 2016/12/7 �U�� 02:32:52 by Hibernate Tools 3.4.0.CR1

/**
 * SrmHistoryCaseTransactionParameter generated by hbm2java
 */
public class SrmHistoryCaseTransactionParameter extends DomainModelObject<String, SrmCaseTransactionParameterDTO>{

	private String paramterValueId;
	private String caseId;
	private String transactionType;
	private String merchantCode;
	private String merchantCodeOther;
	private String tid;
	private String itemValue;

	public SrmHistoryCaseTransactionParameter() {
	}

	public SrmHistoryCaseTransactionParameter(String paramterValueId,
			String caseId, String transactionType) {
		this.paramterValueId = paramterValueId;
		this.caseId = caseId;
		this.transactionType = transactionType;
	}

	public SrmHistoryCaseTransactionParameter(String paramterValueId,
			String caseId, String transactionType, String merchantCode,
			String merchantCodeOther, String tid, String itemValue) {
		this.paramterValueId = paramterValueId;
		this.caseId = caseId;
		this.transactionType = transactionType;
		this.merchantCode = merchantCode;
		this.merchantCodeOther = merchantCodeOther;
		this.tid = tid;
		this.itemValue = itemValue;
	}

	public String getParamterValueId() {
		return this.paramterValueId;
	}

	public void setParamterValueId(String paramterValueId) {
		this.paramterValueId = paramterValueId;
	}

	public String getCaseId() {
		return this.caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getMerchantCode() {
		return this.merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantCodeOther() {
		return this.merchantCodeOther;
	}

	public void setMerchantCodeOther(String merchantCodeOther) {
		this.merchantCodeOther = merchantCodeOther;
	}

	public String getTid() {
		return this.tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getItemValue() {
		return this.itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

}
