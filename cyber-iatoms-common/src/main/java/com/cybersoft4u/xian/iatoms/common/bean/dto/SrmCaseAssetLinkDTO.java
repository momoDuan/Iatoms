package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

public class SrmCaseAssetLinkDTO extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -6573917877835785323L;
	/**
	 * 空構造
	 */
	public SrmCaseAssetLinkDTO() {
	}
	
	public static enum ATTRIBUTE {
		ASSET_LINK_ID("assetLinkId"),
		CASE_ID("caseId"),
		ITEM_TYPE("itemType"),
		ITEM_ID("itemId"),
		CASE_CATEGORY("caseCategory"),
		ITEM_NAME("itemName"),
		ITEM_ID_SUPPLIES_NAME("itemIdSuppliesName"),
		ITEM_CATEGORY("itemCategory"),
		ITEM_CATEGORY_NAME("itemCategoryName"),
		SERIAL_NUMBER("serialNumber"),
		NUMBER("number"),
		ACTION("action"),
		IS_LINK("isLink"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		SIM_ENABLE_DATE("simEnableDate"),
		ENABLE_DATE("enableDate"),
		WAREHOUSE_ID("warehouseId"),
		WAREHOUSE_NAME("warehouseName"),
		CONTENT("content"),
		EDC_PEOPLE("edcPeople"),
		EDC_COMMENT("edcComment"),
		ASSET_TYPE_ID("assetTypeId"),
		NAME("name"),
		CARRIER("carrier"),
		CARRY_COMMENT("carrycomment"),
		PRICE("price"),
		UNINSTALL_SERIAL_NUMBER("uninstallSerialNumber"),
		HISTORY_ASSET_ID("historyAssetId"),
		CONTRACT_ID("contractId"),
		PROPERTY_ID("propertyId"),
		IS_UNINSTALL_AND_LOSS("isUninstallAndLoss"),
		IS_ACTION("isAction"),
		DTID("dtid"),
		TOTAL_PRICE("totalPrice");
		/**
		 * value
		 */
		private String value;
		/**
		 * @param value
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		/**
		 * @return this.value
		 */
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * ID
	 */
	private String assetLinkId;
	/**
	 * 案件單號
	 */
	private String caseId;
	/**
	 * 項目類別
	 */
	private String itemType;
	/**
	 * 設備編號/耗材編號
	 */
	private String itemId;
	/**
	 * 設備名稱/耗材名稱
	 */
	private String itemName;
	/**
	 * 設備類別/耗材分類
	 */
	private String itemCategory;
	/**
	 *設備序號
	 */
	private String serialNumber;
	/**
	 * 數量
	 */
	private Integer number;
	/**
	 * 動作
	 */
	private String action;
	/**
	 * 說明
	 */
	private String content;
	/**
	 * 說明
	 */
	private String removeContent;
	/**
	 * 連接
	 */
	private String isLink;
	/**
	 * 新增人員ID
	 */
	private String createdById;
	/**
	 * 新增人員姓名
	 */
	private String createdByName;
	/**
	 * 新增日期
	 */
	private Timestamp createdDate;
	/**
	 * 設備租賃日
	 */
	private Timestamp simEnableDate;
	/**
	 * 設備啟用日期
	 */
	private Timestamp enableDate;
	/**
	 * 倉庫代碼
	 */
	private String warehouseId;
	/**
	 * 合約編號
	 */
	private String contractId;
	/**
	 * 耗材分類名稱
	 */
	private String itemCategoryName;
	/**
	 * 耗材名稱
	 */
	private String itemIdSuppliesName;
	/**
	 * 領用/借用人
	 */
	private String edcPeople;
	/**
	 * 領用/借用說明
	 */
	private String edcComment;
	/**
	 * 設備序號
	 */
	private String assetTypeId;
	/**
	 * 設備名稱
	 */
	private String name;
	/**
	 * 倉庫名稱
	 */
	private String warehouseName;
	/**
	 * 
	 */
	private BigDecimal price;
	/**
	 * 拆機取消關聯的設備序號
	 */
	private String uninstallSerialNumber;
	/**
	 * 
	 */
	private String historyAssetId;
	/**
	 * 拆機和遺失
	 */
	private String isUninstallAndLoss;
	/**
	 * 設備的財產編號
	 */
	private String propertyId;
	/**
	 * dtid
	 */
	private String dtid;
	/**
	 *是否顯示動作下拉框標誌位
	 */
	private String isAction;
	/**
	 *是否重複取消鏈接
	 */
	private String isRepeatLink;
	/**
	 *actionValue
	 */
	private String actionValue;
	/**
	 * 總價（單價*數量）
	 */
	private BigDecimal totalPrice;
	/**
	 * 案件類別
	 */
	private String caseCategory;
	/**
	 * Constructor:建構子
	 */
	public SrmCaseAssetLinkDTO(String assetLinkId, String caseId, String itemType, 
			String itemId, String itemCategory, String serialNumber, Integer number, 
			String action, String content, String isLink, String createdById,
			String createdByName, Timestamp createdDate, Timestamp simEnableDate, Timestamp enableDate, 
					String warehouseId, String contractId, String itemCategoryName, String itemIdSuppliesName,
					String uninstallSerialNumber, String isUninstallAndLoss, String propertyId, String dtid, 
					String isRepeatLink, String actionValue) {
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
		this.simEnableDate = simEnableDate;
		this.enableDate = enableDate;
		this.warehouseId = warehouseId;
		this.contractId = contractId;
		this.itemCategoryName = itemCategoryName;
		this.itemIdSuppliesName = itemIdSuppliesName;
		this.uninstallSerialNumber = uninstallSerialNumber;
		this.isUninstallAndLoss = isUninstallAndLoss;
		this.propertyId = propertyId;
		this.dtid = dtid;
		this.isRepeatLink = isRepeatLink;
		this.actionValue = actionValue;
	}
	/**
	 * @return the assetLinkId
	 */
	public String getAssetLinkId() {
		return assetLinkId;
	}
	/**
	 * @param assetLinkId the assetLinkId to set
	 */
	public void setAssetLinkId(String assetLinkId) {
		this.assetLinkId = assetLinkId;
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
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the itemCategory
	 */
	public String getItemCategory() {
		return itemCategory;
	}
	/**
	 * @param itemCategory the itemCategory to set
	 */
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the isLink
	 */
	public String getIsLink() {
		return isLink;
	}
	/**
	 * @param isLink the isLink to set
	 */
	public void setIsLink(String isLink) {
		this.isLink = isLink;
	}
	/**
	 * @return the createdById
	 */
	public String getCreatedById() {
		return createdById;
	}
	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}
	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param timestamp the createdDate to set
	 */
	public void setCreatedDate(Timestamp timestamp) {
		this.createdDate = timestamp;
	}
	/**
	 * @return the simEnableDate
	 */
	public Timestamp getSimEnableDate() {
		return simEnableDate;
	}
	/**
	 * @param simEnableDate the simEnableDate to set
	 */
	public void setSimEnableDate(Timestamp simEnableDate) {
		this.simEnableDate = simEnableDate;
	}
	/**
	 * @return the enableDate
	 */
	public Timestamp getEnableDate() {
		return enableDate;
	}
	/**
	 * @param enableDate the enableDate to set
	 */
	public void setEnableDate(Timestamp enableDate) {
		this.enableDate = enableDate;
	}
	/**
	 * @return the warehouseId
	 */
	public String getWarehouseId() {
		return warehouseId;
	}
	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	/**
	 * @return the itemCategoryName
	 */
	public String getItemCategoryName() {
		return itemCategoryName;
	}
	/**
	 * @param itemCategoryName the itemCategoryName to set
	 */
	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
	}
	/**
	 * @return the itemIdSuppliesName
	 */
	public String getItemIdSuppliesName() {
		return itemIdSuppliesName;
	}
	/**
	 * @param itemIdSuppliesName the itemIdSuppliesName to set
	 */
	public void setItemIdSuppliesName(String itemIdSuppliesName) {
		this.itemIdSuppliesName = itemIdSuppliesName;
	}
	/**
	 * @return the edcPeople
	 */
	public String getEdcPeople() {
		return edcPeople;
	}
	/**
	 * @param edcPeople the edcPeople to set
	 */
	public void setEdcPeople(String edcPeople) {
		this.edcPeople = edcPeople;
	}
	/**
	 * @return the edcComment
	 */
	public String getEdcComment() {
		return edcComment;
	}
	/**
	 * @param edcComment the edcComment to set
	 */
	public void setEdcComment(String edcComment) {
		this.edcComment = edcComment;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the warehouseName
	 */
	public String getWarehouseName() {
		return warehouseName;
	}
	/**
	 * @param warehouseName the warehouseName to set
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
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
	 * @return the uninstallSerialNumber
	 */
	public String getUninstallSerialNumber() {
		return uninstallSerialNumber;
	}
	/**
	 * @param uninstallSerialNumber the uninstallSerialNumber to set
	 */
	public void setUninstallSerialNumber(String uninstallSerialNumber) {
		this.uninstallSerialNumber = uninstallSerialNumber;
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
	 * @return the isUninstallAndLoss
	 */
	public String getIsUninstallAndLoss() {
		return isUninstallAndLoss;
	}
	/**
	 * @param isUninstallAndLoss the isUninstallAndLoss to set
	 */
	public void setIsUninstallAndLoss(String isUninstallAndLoss) {
		this.isUninstallAndLoss = isUninstallAndLoss;
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
	 * @return the isAction
	 */
	public String getIsAction() {
		return isAction;
	}
	/**
	 * @param isAction the isAction to set
	 */
	public void setIsAction(String isAction) {
		this.isAction = isAction;
	}
	/**
	 * @return the isRepeatLink
	 */
	public String getIsRepeatLink() {
		return isRepeatLink;
	}
	/**
	 * @param isRepeatLink the isRepeatLink to set
	 */
	public void setIsRepeatLink(String isRepeatLink) {
		this.isRepeatLink = isRepeatLink;
	}
	/**
	 * @return the actionValue
	 */
	public String getActionValue() {
		return actionValue;
	}
	/**
	 * @param actionValue the actionValue to set
	 */
	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}
	/**
	 * @return the removeContent
	 */
	public String getRemoveContent() {
		return removeContent;
	}
	/**
	 * @param removeContent the removeContent to set
	 */
	public void setRemoveContent(String removeContent) {
		this.removeContent = removeContent;
	}
	/**
	 * @return the totalPrice
	 */
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	/**
	 * @return the caseCategory
	 */
	public String getCaseCategory() {
		return caseCategory;
	}
	/**
	 * @param caseCategory the caseCategory to set
	 */
	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
	}
	
}
