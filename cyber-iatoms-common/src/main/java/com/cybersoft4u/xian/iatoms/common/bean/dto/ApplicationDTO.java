package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 程式版本维护DTO
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016-7-18
 * @MaintenancePersonnel jasonzhou
 */
public class ApplicationDTO extends DataTransferObject<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1917006250913820130L;
	public static enum ATTRIBUTE {
		APPLICATION_ID("applicationId"),
		VERSION("version"),
		NAME("name"),
		CUSTOMER_ID("customerId"),
		CUSTOMER_NAME("customerName"),
		COMMENT("comment"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		IS_ACTIVE("isActive"),
		ASSET_CATEGORY("assetCategory"),
		ASSET_TYPE_ID("assetTypeId"),
		ASSET_TYPE_NAME("assetTypeName"),
		SEARCH_DELETED_FLAG("searchDeletedFlag"),
		;

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * 主鍵編號
	 */
	private String applicationId;
	/**
	 * 版本編號
	 */
	private String version;
	/**
	 * 程式名稱
	 */
	private String name;
	/**
	 * 客戶編號 
	 */
	private String customerId;
	/**
	 * 客戶名稱
	 */
	private String customerName;
	/**
	 * 說明
	 */
	private String comment;
	/**
	 * 創建者賬號
	 */
	private String createdById;
	/**
	 * 創建者姓名
	 */
	private String createdByName;
	/**
	 * 創建日期
	 */
	private Timestamp createdDate;
	/**
	 * 更新者賬號
	 */
	private String updatedById;
	/**
	 * 更新者姓名
	 */
	private String updatedByName;
	/**
	 * 更新者日期
	 */
	private Timestamp updatedDate;
	/**
	 * 設備類別編號
	 */
	private String assetCategory;
	/**
	 * 適用設備編號
	 */
	private String assetTypeId;
	/**
	 * 適用設備名稱
	 */
	private String assetTypeName;
	/**
	 * 是否查詢已刪除資料標誌
	 */
	private String searchDeletedFlag;
	/**
	 * 刪除標記
	 */
	private String deleted = "N";
	/**
	 * Constructor:
	 */
	public ApplicationDTO() {
	}
	/**
	 * Constructor:
	 */
	public ApplicationDTO(String customerId, String version) {
		this.customerId = customerId;
		this.version = version;
	}
	/**
	 * Constructor:
	 */
	public ApplicationDTO(String applicationId, String version, String name,
			String customerId, String comment, String createdById,
			String createdByName, Timestamp createdDate, String updatedById,
			String updatedByName, Timestamp updatedDate,String assetCategory, 
			String assetTypeId ,String customerName,String assetTypeName) {
		this.applicationId = applicationId;
		this.version = version;
		this.name = name;
		this.customerId = customerId;
		this.comment = comment;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.assetCategory = assetCategory;
		this.assetTypeId = assetTypeId;
		this.customerName = customerName;
		this.assetTypeName = assetTypeName;
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the assetCategory
	 */
	public String getAssetCategory() {
		return assetCategory;
	}

	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
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
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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
	 * @return the searchDeletedFlag
	 */
	public String getSearchDeletedFlag() {
		return searchDeletedFlag;
	}

	/**
	 * @param searchDeletedFlag the searchDeletedFlag to set
	 */
	public void setSearchDeletedFlag(String searchDeletedFlag) {
		this.searchDeletedFlag = searchDeletedFlag;
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
	
}
