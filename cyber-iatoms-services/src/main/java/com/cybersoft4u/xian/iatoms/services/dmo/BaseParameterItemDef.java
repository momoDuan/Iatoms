package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;

public class BaseParameterItemDef extends
		DomainModelObject<BaseParameterItemDefId, BaseParameterItemDefDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 7010813251437528374L;

	private String bpidId;
	private String bptdCode;
	private Date effectiveDate;
	private String itemName;
	private String itemValue;
	private String itemDesc;
	private Integer itemOrder;
	private String textField1;
	private String parentBpidId;
	private String updatedById;
	private String updatedByName;
	private Timestamp updatedDate;
	private String deleted = "N";
	private Timestamp deletedDate;
	
	/**
	 * Constructor: 無參構造函數
	 */
	public BaseParameterItemDef() {
		super();
	}
	
	/**
	 * Constructor: 含參構造函數
	 */
	public BaseParameterItemDef(BaseParameterItemDefId id) {
		super(id);
		this.id = id;
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
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
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
