package com.cybersoft4u.xian.iatoms.services.dmo;


import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO;

/**
 * 
 * Purpose: 設備盤點表ＤＭＯ
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-18
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStocktakeInfo extends  DomainModelObject<String,DmmAssetStacktakeListDTO> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -637487170473765090L;
	private String stocktackId;
	private String warWarehouseId;
	private String remark;
	private String completeStatus = "N";
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;

	public DmmAssetStocktakeInfo() {
	}

	public DmmAssetStocktakeInfo(String stocktackId) {
		this.stocktackId = stocktackId;
	}

	public DmmAssetStocktakeInfo( String stocktackId, String warWarehouseId,String completeStatus,
			String remark, String updatedById, String updatedByName,Date updatedDate) {
		this.stocktackId = stocktackId;
		this.warWarehouseId = warWarehouseId;
		this.remark = remark;
		this.completeStatus = completeStatus;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the completeStatus
	 */
	public String getCompleteStatus() {
		return completeStatus;
	}

	/**
	 * @param completeStatus the completeStatus to set
	 */
	public void setCompleteStatus(String completeStatus) {
		this.completeStatus = completeStatus;
	}

	/**
	 * @return the stocktackId
	 */
	public String getStocktackId() {
		return stocktackId;
	}

	/**
	 * @return the warWarehouseId
	 */
	public String getWarWarehouseId() {
		return warWarehouseId;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}

	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param stocktackId the stocktackId to set
	 */
	public void setStocktackId(String stocktackId) {
		this.stocktackId = stocktackId;
	}

	/**
	 * @param warWarehouseId the warWarehouseId to set
	 */
	public void setWarWarehouseId(String warWarehouseId) {
		this.warWarehouseId = warWarehouseId;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	

}
