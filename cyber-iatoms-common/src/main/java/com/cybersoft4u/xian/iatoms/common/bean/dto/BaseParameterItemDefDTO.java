package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 基本參數項目表DTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016年6月28日
 * @MaintenancePersonnel CrissZhang
 */
public class BaseParameterItemDefDTO extends DataTransferObject<BaseParameterItemDefIdDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 7489007542887766295L;
	
	/**
	 * 
	 * Purpose: 枚舉類
	 * @author CrissZhang
	 * @since  JDK 1.7
	 * @date   2016年6月28日
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		BPID_ID("bpidId"),
		BPTD_CODE("bptdCode"),
		PT_NAME("ptName"),
		EFFECTIVE_DATE("effectiveDate"),
		ITEM_NAME("itemName"),
		ITEM_VALUE("itemValue"),
		ITEM_DESC("itemDesc"),
		ITEM_ORDER("itemOrder"),
		TEXT_FIELD1("textField1"),
		PARENT_BPID_ID("parentBpidId"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		DELETED_DATE("deletedDate"),
		DELETED("deleted");

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
	 * 參數項目編號
	 */
	private String bpidId;
	
	/**
	 * 參數類型代碼
	 */
	private String bptdCode;
	
	/**
	 * 參數類型名稱
	 */
	private String ptName;
	
	/**
	 * 版本日期
	 */
	private Date effectiveDate;
	
	/**
	 * 參數項目名稱
	 */
	private String itemName;
	
	/**
	 * 參數項目值
	 */
	private String itemValue;
	
	/**
	 * 參數項目備註說明
	 */
	private String itemDesc;
	
	/**
	 * 參數項目順序
	 */
	private Integer itemOrder;
	
	/**
	 * TEXT保留欄位1
	 */
	private String textField1;
	
	/**
	 * 父參數項目編號
	 */
	private String parentBpidId;
	
	/**
	 * 異動人員ID
	 */
	private String updatedById;
	
	/**
	 * 異動人員姓名
	 */
	private String updatedByName;
	
	/**
	 * 異動日期
	 */
	private Timestamp updatedDate;
	
	/**
	 * 刪除標記
	 */
	private String deleted = "N";
	
	/**
	 * 刪除日期
	 */
	private Timestamp deletedDate;
	
	/**
	 * Constructor: 無參構造函數
	 */
	public BaseParameterItemDefDTO() {
	}
	
	/**
	 * Constructor: 含參構造函數
	 */
	public BaseParameterItemDefDTO(String bpidId, String bptdCode, String ptName, Date effectiveDate, String itemName,
			String itemValue, String itemDesc, Integer itemOrder, String textField1, String parentBpidId,
			String updatedById, String updatedByName, Timestamp updatedDate, String deleted, Timestamp deletedDate) {
		this.bpidId = bpidId;
		this.bptdCode = bptdCode;
		this.ptName = ptName;
		this.effectiveDate = effectiveDate;
		this.itemName = itemName;
		this.itemValue = itemValue;
		this.itemDesc = itemDesc;
		this.itemOrder = itemOrder;
		this.textField1 = textField1;
		this.parentBpidId = parentBpidId;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
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
	 * @return the ptName
	 */
	public String getPtName() {
		return ptName;
	}

	/**
	 * @param ptName the ptName to set
	 */
	public void setPtName(String ptName) {
		this.ptName = ptName;
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
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	 * @return the itemDesc
	 */
	public String getItemDesc() {
		return itemDesc;
	}

	/**
	 * @param itemDesc the itemDesc to set
	 */
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	/**
	 * @return the itemOrder
	 */
	public Integer getItemOrder() {
		return itemOrder;
	}

	/**
	 * @param itemOrder the itemOrder to set
	 */
	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}

	/**
	 * @return the textField1
	 */
	public String getTextField1() {
		return textField1;
	}

	/**
	 * @param textField1 the textField1 to set
	 */
	public void setTextField1(String textField1) {
		this.textField1 = textField1;
	}

	/**
	 * @return the parentBpidId
	 */
	public String getParentBpidId() {
		return parentBpidId;
	}

	/**
	 * @param parentBpidId the parentBpidId to set
	 */
	public void setParentBpidId(String parentBpidId) {
		this.parentBpidId = parentBpidId;
	}

	/**
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}

	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}

	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	/**
	 * @return the updatedDate
	 */
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the deleted
	 */
	public String getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the deletedDate
	 */
	public Timestamp getDeletedDate() {
		return deletedDate;
	}

	/**
	 * @param deletedDate the deletedDate to set
	 */
	public void setDeletedDate(Timestamp deletedDate) {
		this.deletedDate = deletedDate;
	}

}
