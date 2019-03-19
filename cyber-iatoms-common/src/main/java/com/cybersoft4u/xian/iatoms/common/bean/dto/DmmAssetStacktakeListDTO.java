package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;
import java.util.List;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 設備盤點DTO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-6-30
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStacktakeListDTO extends DataTransferObject<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1699737163614961586L;
	
	public static enum ATTRIBUTE {
		ID("id"),
		TACK_ID("tackId"),
		STOCKTACK_ID("stocktackId"),
		SERIAL_NUMBER("serialNumber"),
		REMARK("remark"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		ASSET_TYPE_ID("assetTypeId"),
		ASSET_STATUS("assetStatus"),
		STOCKTAKE_STATUS("stocktakeStatus"),
		STOCKTACK_STATUS_NAME("stocktackStatusName"),
		ASSET_TYPE_NAME("assetTypeName"),
		ASSET_STATUS_NAME("assetStatusName"),
		OVERAGE("overage"),
		ASSETL_LESS("assetlLess"),
		ALREADY_STOCKTACK("alreadyStocktack"),
		NO_STOCKTACK("noStocktack"),
		TOTAL_STOCKTACK("totalStocktack"),
		CONTACT("contact"),
		ADDRESS("address"),
		WAR_HOUSE_NAME("warHouseName"),
		UPDATE_DATE("updateDate"),
		SERIAL_NUMBER_LIST("serialNumberList"),
		OVERAGE_LIST("overageList"),
		ASSETL_LESS_LIST("assetlLessList"),
		NO_STOCKTACK_LIST("noStocktackList"),
		ALREADY_STOCKTACK_LIST("alreadyStocktackList"),
		WAR_WAREHOUSE_ID("warWarehouseId"),
		COUNT("count"),
		CALCULATE_SUM("calculateSum");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * id
	 */
	private String tackId;
	/**
	 * 設備盤點批號ID
	 */
	private String stocktackId;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 說明
	 */
	private String remark;
	/**
	 * 更新者ID
	 */
	private String updatedById;
	/**
	 * 更新者姓名
	 */
	private String updatedByName;
	/**
	 * 更新日期
	 */
	private Date updatedDate;
	/**
	 * 設備種類
	 */
	private String assetTypeId;
	/**
	 * 設備狀態
	 */
	private String assetStatus;
	/**
	 * 盤點狀態
	 */
	private int stocktakeStatus;
	/**
	 * 盤點狀態名稱
	 */
	private String stocktackStatusName;
	/**
	 * 設備種類名稱
	 */
	private String assetTypeName;
	/**
	 * 設備狀態名稱
	 */
	private String assetStatusName;
	/**
	 * 盤盈
	 */
	private Integer overage;
	/**
	 * 盤差
	 */
	private Integer assetlLess;
	/**
	 * 已盤點
	 */
	private Integer alreadyStocktack;
	/**
	 * 待盤點
	 */
	private Integer noStocktack;
	/**
	 * 總數
	 */
	private Integer totalStocktack;
	/**
	 * 倉管人員
	 */
	private String contact;
	/**
	 * 倉庫地址
	 */
	private String address;
	/**
	 * 倉庫名稱
	 */
	private String warHouseName;
	/**
	 * 這組設備的序號集合
	 */
	private String serialNumberList;
	/**
	 * 待盤點的設備序號集合
	 */
	private String noStocktackList;
	/**
	 * 已盤點的設備序號集合
	 */
	private String alreadyStocktackList;
	/**
	 * 盤盈的設備序號集合
	 */
	private String overageList;
	/**
	 * 盤差的設備序號集合tackId
	 */
	private String assetlLessList;
	/**
	 * 是否被選中
	 */
	private Boolean checked;
	/**
	 * 計算報表展示的頁數中的列的號碼 
	 */
	private Integer calculateSum;
	/**
	 * 当前的组别内有多少条数据
	 */
	private Integer sum;
	/**
	 * 子報表集合 
	 */
	private List<AssetInventoryReportDTO> reportDTOs;
	/**
	 * 更新日期
	 */
	private String updateDate;
	/**
	 * 倉庫ID
	 */
	private String warWarehouseId;
	/**
	 * 總條數
	 */
	private Integer count;
	/**
	 * 完修狀態
	 */
	private String completeStatus = "N";	
	/**
	 * Constructor:無參構造函數
	 */
	public DmmAssetStacktakeListDTO() {
		super();
	}
	
	/**
	 * Constructor:有參構造函數
	 */
	public DmmAssetStacktakeListDTO(String tackId, String stocktackId, String serialNumber, String remark, String updatedById,
			String updatedByName, Date updatedDate, String assetTypeId, String assetStatus, int stocktakeStatus,
			String stocktackStatusName, String assetTypeName, String assetStatusName, Integer overage, Integer assetlLess,
			Integer alreadyStocktack, Integer noStocktack,
			Integer totalStocktack, String contact, String address,
			String warHouseName, String serialNumberList,
			String noStocktackList, String alreadyStocktackList,
			String overageList, String assetlLessList, Boolean checked,
			Integer calculateSum, Integer sum,
			List<AssetInventoryReportDTO> reportDTOs, String updateDate,
			String warWarehouseId) {
		super();
		this.tackId = tackId;
		this.stocktackId = stocktackId;
		this.serialNumber = serialNumber;
		this.remark = remark;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.assetTypeId = assetTypeId;
		this.assetStatus = assetStatus;
		this.stocktakeStatus = stocktakeStatus;
		this.stocktackStatusName = stocktackStatusName;
		this.assetTypeName = assetTypeName;
		this.assetStatusName = assetStatusName;
		this.overage = overage;
		this.assetlLess = assetlLess;
		this.alreadyStocktack = alreadyStocktack;
		this.noStocktack = noStocktack;
		this.totalStocktack = totalStocktack;
		this.contact = contact;
		this.address = address;
		this.warHouseName = warHouseName;
		this.serialNumberList = serialNumberList;
		this.noStocktackList = noStocktackList;
		this.alreadyStocktackList = alreadyStocktackList;
		this.overageList = overageList;
		this.assetlLessList = assetlLessList;
		this.checked = checked;
		this.calculateSum = calculateSum;
		this.sum = sum;
		this.reportDTOs = reportDTOs;
		this.updateDate = updateDate;
		this.warWarehouseId = warWarehouseId;
	}
	
	/**
	 * @return the stocktakeStatus
	 */
	public int getStocktakeStatus() {
		return stocktakeStatus;
	}

	

	/**
	 * @param stocktakeStatus the stocktakeStatus to set
	 */
	public void setStocktakeStatus(int stocktakeStatus) {
		this.stocktakeStatus = stocktakeStatus;
	}

	/**
	 * @return the noStocktackList
	 */
	public String getNoStocktackList() {
		return noStocktackList;
	}

	/**
	 * @param noStocktackList the noStocktackList to set
	 */
	public void setNoStocktackList(String noStocktackList) {
		this.noStocktackList = noStocktackList;
	}

	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the sum
	 */
	public Integer getSum() {
		return sum;
	}
	
	/**
	 * @return the alreadyStocktackList
	 */
	public String getAlreadyStocktackList() {
		return alreadyStocktackList;
	}

	/**
	 * @param alreadyStocktackList the alreadyStocktackList to set
	 */
	public void setAlreadyStocktackList(String alreadyStocktackList) {
		this.alreadyStocktackList = alreadyStocktackList;
	}

	/**
	 * @return the overageList
	 */
	public String getOverageList() {
		return overageList;
	}

	/**
	 * @param overageList the overageList to set
	 */
	public void setOverageList(String overageList) {
		this.overageList = overageList;
	}

	/**
	 * @return the assetlLessList
	 */
	public String getAssetlLessList() {
		return assetlLessList;
	}

	/**
	 * @param assetlLessList the assetlLessList to set
	 */
	public void setAssetlLessList(String assetlLessList) {
		this.assetlLessList = assetlLessList;
	}

	/**
	 * @param sum the sum to set
	 */
	public void setSum(Integer sum) {
		this.sum = sum;
	}

	/**
	 * @return the serialNumberList
	 */
	public String getSerialNumberList() {
		return serialNumberList;
	}

	/**
	 * @param serialNumberList the serialNumberList to set
	 */
	public void setSerialNumberList(String serialNumberList) {
		this.serialNumberList = serialNumberList;
	}

	/**
	 * @return the reportDTOs
	 */
	public List<AssetInventoryReportDTO> getReportDTOs() {
		return reportDTOs;
	}

	/**
	 * @param reportDTOs the reportDTOs to set
	 */
	public void setReportDTOs(List<AssetInventoryReportDTO> reportDTOs) {
		this.reportDTOs = reportDTOs;
	}


	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @return the calculateSum
	 */
	public Integer getCalculateSum() {
		return calculateSum;
	}

	/**
	 * @param calculateSum the calculateSum to set
	 */
	public void setCalculateSum(Integer calculateSum) {
		this.calculateSum = calculateSum;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the warHouseName
	 */
	public String getWarHouseName() {
		return warHouseName;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param warHouseName the warHouseName to set
	 */
	public void setWarHouseName(String warHouseName) {
		this.warHouseName = warHouseName;
	}

	/**
	 * @return the overage
	 */
	public Integer getOverage() {
		return overage;
	}

	/**
	 * @return the assetlLess
	 */
	public Integer getAssetlLess() {
		return assetlLess;
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
	 * @return the alreadyStocktack
	 */
	public Integer getAlreadyStocktack() {
		return alreadyStocktack;
	}

	/**
	 * @return the noStocktack
	 */
	public Integer getNoStocktack() {
		return noStocktack;
	}

	/**
	 * @return the totalStocktack
	 */
	public Integer getTotalStocktack() {
		return totalStocktack;
	}

	/**
	 * @param overage the overage to set
	 */
	public void setOverage(Integer overage) {
		this.overage = overage;
	}

	/**
	 * @param assetlLess the assetlLess to set
	 */
	public void setAssetlLess(Integer assetlLess) {
		this.assetlLess = assetlLess;
	}

	/**
	 * @param alreadyStocktack the alreadyStocktack to set
	 */
	public void setAlreadyStocktack(Integer alreadyStocktack) {
		this.alreadyStocktack = alreadyStocktack;
	}

	/**
	 * @param noStocktack the noStocktack to set
	 */
	public void setNoStocktack(Integer noStocktack) {
		this.noStocktack = noStocktack;
	}

	/**
	 * @param totalStocktack the totalStocktack to set
	 */
	public void setTotalStocktack(Integer totalStocktack) {
		this.totalStocktack = totalStocktack;
	}

	/**
	 * @return the stocktackStatusName
	 */
	public String getStocktackStatusName() {
		return stocktackStatusName;
	}


	/**
	 * @param stocktackStatusName the stocktackStatusName to set
	 */
	public void setStocktackStatusName(String stocktackStatusName) {
		this.stocktackStatusName = stocktackStatusName;
	}

	/**
	 * @return the tackId
	 */
	public String getTackId() {
		return tackId;
	}

	/**
	 * @param tackId the tackId to set
	 */
	public void setTackId(String tackId) {
		this.tackId = tackId;
	}

	/**
	 * @return the stocktackId
	 */
	public String getStocktackId() {
		return stocktackId;
	}
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
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
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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


	/**
	 * @return the assetStatus
	 */
	public String getAssetStatus() {
		return assetStatus;
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

	/**
	 * @param assetStatus the assetStatus to set
	 */
	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
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
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
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
	
	/*public static void main(String[] args) {
		BindPageDataUtils.generateAttributeEnum(AssetStocktackInfoDTO.class);
	}*/
	/*public static enum ATTRIBUTE {
		STOCKTACK_ID("stocktackId"),
		WAR_WAREHOUSE_ID("warWarehouseId"),
		REMARK("remark"),
		HOUSE_NAME("houseName"),
		CONTACT("contact"),
		ASSET_TYPE_NAME("assetTypeName"),
		ASSET_STATUS_NAME("assetStatusName"),
		OVERAGE("overage"),
		ASSETL_LESS("assetlLess"),
		ALREADY_STOCKTACK("alreadyStocktack"),
		NO_STOCKTACK("noStocktack"),
		TOTAL_STOCKTACK("totalStocktack"),
		IS_EXISTS("isExists"),
		ADDRESS("address"),
		WAR_HOUSE_NAME("warHouseName"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		INVENTORY_WAR_HOUSE_NAME("inventoryWarHouseName"),
		COUNT("count"),
		COMPLETE_STATUS("completeStatus");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};


	private String stocktackId;//
	private String warWarehouseId;//
	private String remark;//
	private String houseName;//倉庫名稱
	private String contact;//倉庫管理員
	private String assetTypeName;//設備種類名稱
	private String assetStatusName;//設備狀態名稱
	
	private Integer overage;//盤盈
	private Integer assetlLess;//盤差
	private Integer alreadyStocktack;//已盤點
	private Integer noStocktack;//待盤點
	private Integer totalStocktack;//總數
	private String isExists;//是否存在此設備
	private String address;//倉庫地址
	private String warHouseName;//倉庫名稱
	
	private String updatedById;//更新者代號
	private String updatedByName;//更新者姓名
	private Date updatedDate;//更新日期
	private String inventoryWarHouseName;//盤點倉庫名稱
	private Integer count;//總條數
	private String completeStatus;
	
	*//**
	 * @return the completeStatus
	 *//*
	public String getCompleteStatus() {
		return completeStatus;
	}
	*//**
	 * @param completeStatus the completeStatus to set
	 *//*
	public void setCompleteStatus(String completeStatus) {
		this.completeStatus = completeStatus;
	}
	*//**
	 * @return the inventoryWarHouseName
	 *//*
	public String getInventoryWarHouseName() {
		return inventoryWarHouseName;
	}
	*//**
	 * @param inventoryWarHouseName the inventoryWarHouseName to set
	 *//*
	public void setInventoryWarHouseName(String inventoryWarHouseName) {
		this.inventoryWarHouseName = inventoryWarHouseName;
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
	 * @return the warWarehouseId
	 *//*
	public String getWarWarehouseId() {
		return warWarehouseId;
	}
	
	*//**
	 * @return the count
	 *//*
	public Integer getCount() {
		return count;
	}
	*//**
	 * @param count the count to set
	 *//*
	public void setCount(Integer count) {
		this.count = count;
	}
	*//**
	 * @return the remark
	 *//*
	public String getRemark() {
		return remark;
	}
	*//**
	 * @return the updatedById
	 *//*
	public String getUpdatedById() {
		return updatedById;
	}
	*//**
	 * @return the updatedByName
	 *//*
	public String getUpdatedByName() {
		return updatedByName;
	}
	*//**
	 * @return the updatedDate
	 *//*
	public Date getUpdatedDate() {
		return updatedDate;
	}
	*//**
	 * @param warWarehouseId the warWarehouseId to set
	 *//*
	public void setWarWarehouseId(String warWarehouseId) {
		this.warWarehouseId = warWarehouseId;
	}
	*//**
	 * @param remark the remark to set
	 *//*
	public void setRemark(String remark) {
		this.remark = remark;
	}
	*//**
	 * @param updatedById the updatedById to set
	 *//*
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	*//**
	 * @param updatedByName the updatedByName to set
	 *//*
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	*//**
	 * @param updatedDate the updatedDate to set
	 *//*
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	*//**
	 * @return the houseName
	 *//*
	public String getHouseName() {
		return houseName;
	}
	*//**
	 * @return the contact
	 *//*
	public String getContact() {
		return contact;
	}
	*//**
	 * @return the assetStatusName
	 *//*
	public String getAssetStatusName() {
		return assetStatusName;
	}
	*//**
	 * @param houseName the houseName to set
	 *//*
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	*//**
	 * @param contact the contact to set
	 *//*
	public void setContact(String contact) {
		this.contact = contact;
	}
	*//**
	 * @param assetStatusName the assetStatusName to set
	 *//*
	public void setAssetStatusName(String assetStatusName) {
		this.assetStatusName = assetStatusName;
	}
	public String getAssetTypeName() {
		return assetTypeName;
	}
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}
	*//**
	 * @return the overage
	 *//*
	public Integer getOverage() {
		return overage;
	}
	*//**
	 * @param overage the overage to set
	 *//*
	public void setOverage(Integer overage) {
		this.overage = overage;
	}
	*//**
	 * @return the assetlLess
	 *//*
	public Integer getAssetlLess() {
		return assetlLess;
	}
	*//**
	 * @param assetlLess the assetlLess to set
	 *//*
	public void setAssetlLess(Integer assetlLess) {
		this.assetlLess = assetlLess;
	}
	*//**
	 * @return the alreadyStocktack
	 *//*
	public Integer getAlreadyStocktack() {
		return alreadyStocktack;
	}
	*//**
	 * @param alreadyStocktack the alreadyStocktack to set
	 *//*
	public void setAlreadyStocktack(Integer alreadyStocktack) {
		this.alreadyStocktack = alreadyStocktack;
	}
	*//**
	 * @return the noStocktack
	 *//*
	public Integer getNoStocktack() {
		return noStocktack;
	}
	*//**
	 * @param noStocktack the noStocktack to set
	 *//*
	public void setNoStocktack(Integer noStocktack) {
		this.noStocktack = noStocktack;
	}
	*//**
	 * @return the totalStocktack
	 *//*
	public Integer getTotalStocktack() {
		return totalStocktack;
	}
	*//**
	 * @param totalStocktack the totalStocktack to set
	 *//*
	public void setTotalStocktack(Integer totalStocktack) {
		this.totalStocktack = totalStocktack;
	}
	*//**
	 * @return the isExists
	 *//*
	public String getIsExists() {
		return isExists;
	}
	*//**
	 * @param isExists the isExists to set
	 *//*
	public void setIsExists(String isExists) {
		this.isExists = isExists;
	}
	*//**
	 * @return the address
	 *//*
	public String getAddress() {
		return address;
	}
	*//**
	 * @param address the address to set
	 *//*
	public void setAddress(String address) {
		this.address = address;
	}
	*//**
	 * @return the warHouseName
	 *//*
	public String getWarHouseName() {
		return warHouseName;
	}
	*//**
	 * @param warHouseName the warHouseName to set
	 *//*
	public void setWarHouseName(String warHouseName) {
		this.warHouseName = warHouseName;
	}*/
	
	
}
