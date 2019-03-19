package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.List;
import java.util.Map;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose:案件交易參數 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月16日
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseTransactionParameterDTO extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -1840468113387391210L;
	
	/**
	 * 
	 * Purpose: 案件交易參數 枚舉類
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/1/3
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		PARAMTER_VALUE_ID("paramterValueId"),
		CASE_ID("caseId"),
		TRANSACTION_TYPE("transactionType"),
		MERCHANT_CODE("merchantCode"),
		MERCHANT_CODE_OTHER("merchantCodeOther"),
		TID("tid"),
		DTID("dtid"),
		ADVANCE_AUTHORIZED("advanceAuthorized"),
		ITEM_VALUE("itemValue"),
		SALETRANSACTION("saleTransaction"),
		CANCELTRANSACTION("cancelTransaction"),
		MANUALCANCELTRANSACTION("manualCancelTransaction"),
		TRANSACTION_TYPE_NAME("transactionTypeName");
		
		/**
		 * value值
		 */
		private String value;
		
		/**
		 * Constructor:構造函數
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 案件序號---案件匯入
	 */
	private String caseNo;
	/**
	 * 交易參數主鍵
	 */
	private String paramterValueId;
	/**
	 * dtid
	 */
	private String DTID;
	/**
	 * 預先授權
	 */
	private String advanceAuthorized;
	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * 交易參數類別
	 */
	private String transactionType;
	/**
	 * 特店代碼
	 */
	private String merchantCode;
	/**
	 * 特店代碼1
	 */
	private String merchantCodeOther;
	/**
	 * 終端機編號
	 */
	private String tid;
	/**
	 * 參數值，其他不定列的值。json格式
	 */
	private String itemValue;
	/**
	 * 案件匯入交易參數核檢
	 */
	private List<String> itemValues;
	/**
	 * 交易類別名稱
	 */
	private String transactionTypeName;
	/**
	 * 是否刪除-匯入時使用
	 */
	private String isDeleted;
	/**
	 * 可變的交易參數的map key 是 交易參數的 code  value是交易參數對應的值
	 */
	private Map<String, String> srmCaseTransactionParametermap;
	/**
	 * @return the paramterValueId
	 */
	public String getParamterValueId() {
		return paramterValueId;
	}
	/**
	 * @param paramterValueId the paramterValueId to set
	 */
	public void setParamterValueId(String paramterValueId) {
		this.paramterValueId = paramterValueId;
	}
	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}
	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	/**
	 * @return the merchantCode
	 */
	public String getMerchantCode() {
		return merchantCode;
	}
	/**
	 * @param merchantCode the merchantCode to set
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	/**
	 * @return the merchantCodeOther
	 */
	public String getMerchantCodeOther() {
		return merchantCodeOther;
	}
	/**
	 * @param merchantCodeOther the merchantCodeOther to set
	 */
	public void setMerchantCodeOther(String merchantCodeOther) {
		this.merchantCodeOther = merchantCodeOther;
	}
	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * @return the itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}
	/**
	 * @param itemValue the itemValue to set
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	/**
	 * @return the transactionTypeName
	 */
	public String getTransactionTypeName() {
		return transactionTypeName;
	}
	/**
	 * @param transactionTypeName the transactionTypeName to set
	 */
	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}
	
	/**
	 * @return the dTID
	 */
	public String getDTID() {
		return DTID;
	}
	/**
	 * @param dTID the dTID to set
	 */
	public void setDTID(String dTID) {
		DTID = dTID;
	}
	/**
	 * @return the advanceAuthorized
	 */
	public String getAdvanceAuthorized() {
		return advanceAuthorized;
	}
	/**
	 * @param advanceAuthorized the advanceAuthorized to set
	 */
	public void setAdvanceAuthorized(String advanceAuthorized) {
		this.advanceAuthorized = advanceAuthorized;
	}
	/**
	 * @return the itemValues
	 */
	public List<String> getItemValues() {
		return itemValues;
	}
	/**
	 * @param itemValues the itemValues to set
	 */
	public void setItemValues(List<String> itemValues) {
		this.itemValues = itemValues;
	}
	/**
	 * @return the caseNo
	 */
	public String getCaseNo() {
		return caseNo;
	}
	/**
	 * @param caseNo the caseNo to set
	 */
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	/**
	 * @return the srmCaseTransactionParametermap
	 */
	public Map<String, String> getSrmCaseTransactionParametermap() {
		return srmCaseTransactionParametermap;
	}
	/**
	 * @param srmCaseTransactionParametermap the srmCaseTransactionParametermap to set
	 */
	public void setSrmCaseTransactionParametermap(
			Map<String, String> srmCaseTransactionParametermap) {
		this.srmCaseTransactionParametermap = srmCaseTransactionParametermap;
	}
	/**
	 * @return the isDeleted
	 */
	public String getIsDeleted() {
		return isDeleted;
	}
	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
}
