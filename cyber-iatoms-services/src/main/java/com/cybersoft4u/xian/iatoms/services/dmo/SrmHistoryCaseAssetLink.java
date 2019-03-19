package com.cybersoft4u.xian.iatoms.services.dmo;

import java.math.BigDecimal;

// Generated 2017/2/24 �U�� 01:13:23 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;

/**
 * SrmHistoryCaseAssetLink generated by hbm2java
 */
public class SrmHistoryCaseAssetLink extends DomainModelObject<String, SrmCaseAssetLinkDTO>{

	private String assetLinkId;
	private String caseId;
	private String itemType;
	private String itemId;
	private String itemCategory;
	private String serialNumber;
	private Integer number;
	private String action;
	private String content;
	private String isLink;
	private String createdById;
	private String createdByName;
	private Date createdDate;
	private Date enableDate;
	private String warehouseId;
	private String contractId;
	private BigDecimal price;
	private String historyAssetId;
	private String propertyId;

	public SrmHistoryCaseAssetLink() {
	}

	public SrmHistoryCaseAssetLink(String assetLinkId) {
		this.assetLinkId = assetLinkId;
	}

	public SrmHistoryCaseAssetLink(String assetLinkId,
			String caseId, String itemType,
			String itemId, String itemCategory, String serialNumber,
			Integer number, String action, String content, String isLink,
			String createdById, String createdByName, Date createdDate,
			Date enableDate, String warehouseId, String contractId, BigDecimal price,
			String historyAssetId, String propertyId) {
		this.assetLinkId = assetLinkId;
		this.caseId = caseId;
		this.itemType = itemType;
		this.itemId = itemId;
		this.itemCategory = itemCategory;
		this.serialNumber = serialNumber;
		this.number = number;
		this.action = action;
		this.content = content;
		this.isLink = isLink;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.enableDate = enableDate;
		this.warehouseId = warehouseId;
		this.contractId = contractId;
		this.price = price;
		this.historyAssetId = historyAssetId;
		this.propertyId = propertyId;
	}

	public String getAssetLinkId() {
		return this.assetLinkId;
	}

	public void setAssetLinkId(String assetLinkId) {
		this.assetLinkId = assetLinkId;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemCategory() {
		return this.itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsLink() {
		return this.isLink;
	}

	public void setIsLink(String isLink) {
		this.isLink = isLink;
	}

	public String getCreatedById() {
		return this.createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return this.createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getEnableDate() {
		return this.enableDate;
	}

	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the historyAssetId
	 */
	public String getHistoryAssetId() {
		return historyAssetId;
	}

	/**
	 * @param historyAssetId the historyAssetId to set
	 */
	public void setHistoryAssetId(String historyAssetId) {
		this.historyAssetId = historyAssetId;
	}

	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}
}
