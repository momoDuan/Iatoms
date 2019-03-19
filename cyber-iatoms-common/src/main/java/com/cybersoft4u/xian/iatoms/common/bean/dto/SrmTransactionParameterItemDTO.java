package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 交易參數項目DTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016/12/8
 * @MaintenancePersonnel CrissZhang
 */
public class SrmTransactionParameterItemDTO extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -4512370567552244407L;
	
	/**
	 * Purpose: enum
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/2/23
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		PARAMTER_ITEM_ID("paramterItemId"),
		PARAMTER_ITEM_CODE("paramterItemCode"),
		PARAMTER_ITEM_NAME("paramterItemName"),
		PARAMTER_ITEM_TYPE("paramterItemType"),
		PARAMTER_DATA_TYPE("paramterDataType"),
		PARAMTER_ITEM_LENGTH("paramterItemLength"),
		ITEM_ORDER("itemOrder"),
		EFFECTIVE_DATE("effectiveDate"),
		APPROVED_FLAG("approvedFlag");

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
	 * 項目ID
	 */
	private String paramterItemId;
	/**
	 * 參數項目代碼
	 */
	private String paramterItemCode;
	/**
	 * 參數項目名稱
	 */
	private String paramterItemName;
	/**
	 * 參數形態
	 */
	private String paramterItemType;
	/**
	 * 參數數據類型（integer,string,float等）
	 */
	private String paramterDataType;
	/**
	 * 參數長度
	 */
	private Short paramterItemLength;
	/**
	 * 欄位排序
	 */
	private Short itemOrder;
	/**
	 * 版本時間
	 */
	private Date effectiveDate;
	/**
	 * 
	 */
	private String approvedFlag;
	
	/**
	 * Constructor:無參構造函數
	 */
	public SrmTransactionParameterItemDTO() {
		super();
	}
	/**
	 * Constructor:有參構造函數
	 */
	public SrmTransactionParameterItemDTO(String paramterItemId,
			String paramterItemCode, String paramterItemName,
			String paramterItemType,String paramterDataType,
			Short paramterItemLength) {
		super();
		this.paramterItemId = paramterItemId;
		this.paramterItemCode = paramterItemCode;
		this.paramterItemName = paramterItemName;
		this.paramterItemType = paramterItemType;
		this.paramterDataType = paramterDataType;
		this.paramterItemLength = paramterItemLength;
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
	 * @return the paramterItemName
	 */
	public String getParamterItemName() {
		return paramterItemName;
	}
	/**
	 * @param paramterItemName the paramterItemName to set
	 */
	public void setParamterItemName(String paramterItemName) {
		this.paramterItemName = paramterItemName;
	}
	/**
	 * @return the paramterItemType
	 */
	public String getParamterItemType() {
		return paramterItemType;
	}
	/**
	 * @param paramterItemType the paramterItemType to set
	 */
	public void setParamterItemType(String paramterItemType) {
		this.paramterItemType = paramterItemType;
	}
	/**
	 * @return the paramterDataType
	 */
	public String getParamterDataType() {
		return paramterDataType;
	}
	/**
	 * @param paramterDataType the paramterDataType to set
	 */
	public void setParamterDataType(String paramterDataType) {
		this.paramterDataType = paramterDataType;
	}
	/**
	 * @return the paramterItemLength
	 */
	public Short getParamterItemLength() {
		return paramterItemLength;
	}
	/**
	 * @param paramterItemLength the paramterItemLength to set
	 */
	public void setParamterItemLength(Short paramterItemLength) {
		this.paramterItemLength = paramterItemLength;
	}/**
	 * @return the itemOrder
	 */
	public Short getItemOrder() {
		return itemOrder;
	}
	/**
	 * @param itemOrder the itemOrder to set
	 */
	public void setItemOrder(Short itemOrder) {
		this.itemOrder = itemOrder;
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
	/**
	 * @return the approvedFlag
	 */
	public String getApprovedFlag() {
		return approvedFlag;
	}
	/**
	 * @param approvedFlag the approvedFlag to set
	 */
	public void setApprovedFlag(String approvedFlag) {
		this.approvedFlag = approvedFlag;
	}
	
}
