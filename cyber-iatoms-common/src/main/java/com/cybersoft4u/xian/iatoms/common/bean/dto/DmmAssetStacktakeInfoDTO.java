package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;

/**
 * Purpose: 設備盤點主檔功能DTO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-15
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStacktakeInfoDTO extends DataTransferObject<String>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8776853720232626525L;
	
	public static enum ATTRIBUTE {
		STOCKTACK_ID("stocktackId"),
		WAR_WAREHOUSE_ID("warWarehouseId"),
		COMPLETE_STATUS("completeStatus"),
		REMARK("remark"),
		ASSET_TYPE_ID("assetTypeId"),
		ASSET_STATUS("assetStatus"),
		ASSET_STATUS_NAME("assetStatusName"),
		ASSET_TYPE_NAME("assetTypeName"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * 盤點批號
	 */
	private String stocktackId;
	/**
	 * 倉庫代碼
	 */
	private String warWarehouseId;
	/**
	 * 完成狀態
	 */
	private String completeStatus;
	/**
	 * 說明
	 */
	private String remark;
	/**
	 * 設備ID
	 */
	private String assetTypeId;
	/**
	 * 設備狀態ID
	 */
	private String assetStatus;
	/**
	 * 設備狀態名稱
	 */
	private String assetStatusName;
	/**
	 * 設備名稱
	 */
	private String assetTypeName;
	/**
	 * 異動人員編號
	 */
	private String updatedById;
	/**
	 * 異動人員名稱
	 */
	private String updatedByName;
	/**
	 * 異動日期
	 */
	private Timestamp updatedDate;
	/*//設備批號
	private String stocktackId;
	private String addAssetInventoryId;
	//倉庫Id
	private String warWarehouseId;
	//設備
	private String assetTypeId;
	//設備狀態
	private String assetStatus;
	private String remark;
	private String typeIdList;
	private String statusList;
	private String assetStatusNames;
	private String assetTypeName;
	
	*//**
	 * @return the assetStatusNames
	 *//*
	public String getAssetStatusNames() {
		return assetStatusNames;
	}
	*//**
	 * @param assetStatusNames the assetStatusNames to set
	 *//*
	public void setAssetStatusNames(String assetStatusNames) {
		this.assetStatusNames = assetStatusNames;
	}
	*//**
	 * @return the assetTypeName
	 *//*
	public String getAssetTypeName() {
		return assetTypeName;
	}
	*//**
	 * @param assetTypeName the assetTypeName to set
	 *//*
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}
	*//**
	 * @return the typeIdList
	 *//*
	public String getTypeIdList() {
		return typeIdList;
	}
	*//**
	 * @param typeIdList the typeIdList to set
	 *//*
	public void setTypeIdList(String typeIdList) {
		this.typeIdList = typeIdList;
	}
	*//**
	 * @return the statusList
	 *//*
	public String getStatusList() {
		return statusList;
	}
	*//**
	 * @param statusList the statusList to set
	 *//*
	public void setStatusList(String statusList) {
		this.statusList = statusList;
	}
	public DeviceInventoryDTO() {
		super();
	}
	*//**
	 * Constructor:有參構造函數
	 *//*
	public DeviceInventoryDTO( String stocktackId,String addAssetInventoryId,
			String remark,String warWarehouseId,
			String assetCategory,String assetStatus) {
		super();
		this.stocktackId = stocktackId;
		this.remark = remark;
		this.assetStatus = assetStatus;
		this.addAssetInventoryId = addAssetInventoryId;
		this.warWarehouseId = warWarehouseId;
		this.assetTypeId = assetCategory;
	}
	*//**
	 * @return the warWarehouseId
	 *//*
	public String getWarWarehouseId() {
		return warWarehouseId;
	}
	*//**
	 * @param warWarehouseId the warWarehouseId to set
	 *//*
	public void setWarWarehouseId(String warWarehouseId) {
		this.warWarehouseId = warWarehouseId;
	}
	*//**
	 * @return the stocktackId
	 *//*
	public String getStocktackId() {
		return stocktackId;
	}
	*//**
	 * @param stocktackId the stocktackId to set
	 *//*
	public void setStocktackId(String stocktackId) {
		this.stocktackId = stocktackId;
	}
	*//**
	 * @return the assetStatus
	 *//*
	public String getAssetStatus() {
		return assetStatus;
	}
	public String getAssetTypeId() {
		return assetTypeId;
	}
	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	*//**
	 * @param assetStatus the assetStatus to set
	 *//*
	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}
	*//**
	 * @return the addAssetInventoryId
	 *//*
	public String getAddAssetInventoryId() {
		return addAssetInventoryId;
	}
	*//**
	 * @return the remark
	 *//*
	public String getRemark() {
		return remark;
	}
	*//**
	 * @param addAssetInventoryId the addAssetInventoryId to set
	 *//*
	public void setAddAssetInventoryId(String addAssetInventoryId) {
		this.addAssetInventoryId = addAssetInventoryId;
	}
	*//**
	 * @param remark the remark to set
	 *//*
	public void setRemark(String remark) {
		this.remark = remark;
	}*/
	/**
	 * @return the stocktackId
	 */
	public String getStocktackId() {
		return stocktackId;
	}
	/**
	 * @param stocktackId the stocktackId to set
	 */
	public void setStocktackId(String stocktackId) {
		this.stocktackId = stocktackId;
	}
	/**
	 * @return the warWarehouseId
	 */
	public String getWarWarehouseId() {
		return warWarehouseId;
	}
	/**
	 * @param warWarehouseId the warWarehouseId to set
	 */
	public void setWarWarehouseId(String warWarehouseId) {
		this.warWarehouseId = warWarehouseId;
	}
	/**
	 * @return the completestatus
	 */
	public String getCompleteStatus() {
		return completeStatus;
	}
	/**
	 * @param completestatus the completestatus to set
	 */
	public void setCompleteStatus(String completeStatus) {
		this.completeStatus = completeStatus;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the assetTypeId
	 */
	public String getAssetTypeId() {
		return assetTypeId;
	}
	/**
	 * @param assetTypeId the assetTypeId to set
	 */
	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	/**
	 * @return the assetStatus
	 */
	public String getAssetStatus() {
		return assetStatus;
	}
	/**
	 * @param assetStatus the assetStatus to set
	 */
	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
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
	 * @return the assetStatusName
	 */
	public String getAssetStatusName() {
		return assetStatusName;
	}
	/**
	 * @param assetStatusName the assetStatusName to set
	 */
	public void setAssetStatusName(String assetStatusName) {
		this.assetStatusName = assetStatusName;
	}
	/**
	 * @return the assetTypeName
	 */
	public String getAssetTypeName() {
		return assetTypeName;
	}
	/**
	 * @param assetTypeName the assetTypeName to set
	 */
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}
	
	
}
