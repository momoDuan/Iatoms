package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 案件最新交易參數資料檔DTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016/12/15
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseNewTransactionParameterDTO extends DataTransferObject<String>{
	
	
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Purpose: 枚举类型参数
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/2/23
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		PARAMTER_VALUE_ID("paramterValueId"),
		CASE_ID("caseId"),
		DTID("dtid"),
		TRANSACTION_TYPE("transactionType"),
		TRANSACTION_TYPE_NAME("transactionTypeName"),
		MERCHANT_CODE("merchantCode"),
		MERCHANT_CODE_OTHER("merchantCodeOther"),
		PARAMTER_ITEM_CODE("paramterItemCode"),
		IS_EDIT("isEdit"),
		TID("tid"),
		ITEM_VALUE("itemValue");

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
	 * ID
	 */
	private String paramterValueId;
	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 交易類別
	 */
	private String transactionType;
	/**
	 * 交易參數名稱
	 */
	private String transactionTypeName;
	/**
	 * 特店代號
	 */
	private String merchantCode;
	/**
	 * 分期特點代號
	 */
	private String merchantCodeOther;
	/**
	 * TID
	 */
	private String tid;
	/**
	 * 參數值
	 */
	private String itemValue;
	/**
	 * 參數代碼
	 */
	private String paramterItemCode;
	
	/**
	 * 是否可編輯
	 */
	private String isEdit;
	/**
	 * Constructor:無參構造函數
	 */
	public SrmCaseNewTransactionParameterDTO() {
		super();
	}
	
	/**
	 * Constructor:有參構造函數
	 */
	public SrmCaseNewTransactionParameterDTO(String paramterValueId,
			String caseId, String dtid, String transactionType,
			String merchantCode, String merchantCodeOther, String tid,
			String itemValue) {
		super();
		this.paramterValueId = paramterValueId;
		this.caseId = caseId;
		this.dtid = dtid;
		this.transactionType = transactionType;
		this.merchantCode = merchantCode;
		this.merchantCodeOther = merchantCodeOther;
		this.tid = tid;
		this.itemValue = itemValue;
	}
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
	 * @return the dtid
	 */
	public String getDtid() {
		return dtid;
	}
	/**
	 * @param dtid the dtid to set
	 */
	public void setDtid(String dtid) {
		this.dtid = dtid;
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
	 * @return the paramterItemCode
	 */
	public String getParamterItemCode() {
		return paramterItemCode;
	}

	/**
	 * @param paramterItemCode the paramterItemCode to set
	 */
	public void setParamterItemCode(String paramterItemCode) {
		this.paramterItemCode = paramterItemCode;
	}

	/**
	 * @return the isEdit
	 */
	public String getIsEdit() {
		return isEdit;
	}

	/**
	 * @param isEdit the isEdit to set
	 */
	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}
	
}
