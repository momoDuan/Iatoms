package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose:交易參數列表DTO 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月15日
 * @MaintenancePersonnel CrissZhang
 */
public class SrmTransactionParameterDetailDTO extends DataTransferObject<String> {	
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -3340048071188770838L;
	/**
	 * Purpose:參數枚舉 
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2016年12月15日
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		TRANSACTION_TYPE("transactionType"),
		PARAMTER_ITEM_ID("paramterItemId"),
		PARAMTER_ITEM_CODE("paramterItemCode"),
		IS_EDIT("isEdit"),
		DEFAULT_VALUE("defaultValue");

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
	 * 交易類別--基本參數表的itemValue
	 */
	private String transactionType;
	/**
	 * 參數項目ID PARAMTER_ITEM_ID
	 */
	private String paramterItemId;
	/**
	 * 參數項目代碼
	 */
	private String paramterItemCode;
	/**
	 * 是否可編輯
	 */
	private String isEdit;
	/**
	 * 默認值
	 */
	private String defaultValue;	
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
	 * @return the paramterItemId
	 */
	public String getParamterItemId() {
		return paramterItemId;
	}
	/**
	 * @param paramterItemId the paramterItemId to set
	 */
	public void setParamterItemId(String paramterItemId) {
		this.paramterItemId = paramterItemId;
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
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}	
}
