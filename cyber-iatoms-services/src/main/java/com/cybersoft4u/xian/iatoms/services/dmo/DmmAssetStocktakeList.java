package com.cybersoft4u.xian.iatoms.services.dmo;


import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;

/**
 * 
 * Purpose: 設備盤點明細表ＤＭＯ
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-18
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStocktakeList extends  DomainModelObject<String,DmmAssetStacktakeListDTO> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3672137572126039769L;
	private String id;
	private String stocktackId;
	private String serialNumber;
	private String assetTypeId;
	private String assetStatus;
	private int stocktakeStatus;
	private String remark;
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;

	public DmmAssetStocktakeList() {
		super();
	}

	public DmmAssetStocktakeList(String id, String assetTypeId,
			String assetStatus, int stocktakeStatus) {
		this.id = id;
		this.assetTypeId = assetTypeId;
		this.assetStatus = assetStatus;
		this.stocktakeStatus = stocktakeStatus;
	}

	public DmmAssetStocktakeList(String id, String stocktackId,
			String serialNumber, String assetTypeId, String assetStatus,
			int stocktakeStatus, String remark, String updatedById,
			String updatedByName, Date updatedDate) {
		this.id = id;
		this.stocktackId = stocktackId;
		this.serialNumber = serialNumber;
		this.assetTypeId = assetTypeId;
		this.assetStatus = assetStatus;
		this.stocktakeStatus = stocktakeStatus;
		this.remark = remark;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStocktackId() {
		return this.stocktackId;
	}

	public void setStocktackId(String stocktackId) {
		this.stocktackId = stocktackId;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getAssetTypeId() {
		return this.assetTypeId;
	}

	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetStatus() {
		return this.assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public int getStocktakeStatus() {
		return this.stocktakeStatus;
	}

	public void setStocktakeStatus(int stocktakeStatus) {
		this.stocktakeStatus = stocktakeStatus;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdatedById() {
		return this.updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public String getUpdatedByName() {
		return this.updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
