package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;

/**
 * Purpose: 設備品項主檔
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel HermanWang
 */
public class AssetTypeDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 5042661161914346062L;
	
	/**
	 * 
	 * Purpose: 产生枚举类
	 * @author HermanWang
	 * @param args
	 * @return void
	 */
	public static void main(String args[]){
		BindPageDataUtils.generateAttributeEnum(AssetTypeDTO.class);
	}
	
	/**
	 * 
	 * Purpose: 枚举类
	 * @author HermanWang
	 * @since  JDK 1.7
	 * @date   2016年7月12日
	 * @MaintenancePersonnel HermanWang
	 */
	public static enum ATTRIBUTE {
		ASSET_TYPE_ID("assetTypeId"),
		NAME("name"),
		BRAND("brand"),
		MODEL("model"),
		ASSET_CATEGORY("assetCategory"),
		ASSET_CATEGORY_NAME("assetCategoryName"),
		UNIT("unit"),
		SAFETY_STOCK("safetyStock"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		DELETED("deleted"),
		DELETED_BY_ID("deletedById"),
		DELETED_BY_NAME("deletedByName"),
		DELETED_DATE("deletedDate"),
		REMARK("remark"),
		COMPANY_ID("companyId"),
		COMPANY_NAME("companyName"),
		COMM_MODE_ID("commModeId"),
		COMM_MODE_NAME("commModeName"),
		FUNCTION_ID("functionId"),
		FUNCTION_NAME("functionName"),
		FUNCTION_IDS("functionIds"),
		COMPANY_IDS("companyIds"),
		COMM_MODE_IDS("commModeIds");
		/**
		 * value
		 */
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * 設備代碼
	 */
	private String assetTypeId;
	/**
	 * 設備名稱
	 */
	private String name;
	/**
	 * 設備廠牌
	 */
	private String brand;
	/**
	 * 設備型號
	 */
	private String model;
	/**
	 * 設備類別
	 */
	private String assetCategory;
	/**
	 * 設備類別名称
	 */
	private String assetCategoryName;
	/**
	 * 單位
	 */
	private String unit;
	/**
	 * 安全庫存
	 */
	private BigDecimal safetyStock;
	/**
	 * 创建人ID
	 */
	private String createdById;
	/**
	 * 创建人name
	 */
	private String createdByName;
	/**
	 * 创建日期
	 */
	private Timestamp createdDate;
	/**
	 * 更新人ID
	 */
	private String updatedById;
	/**
	 * 更新人name
	 */
	private String updatedByName;
	/**
	 * 更新日期
	 */
	private Timestamp updatedDate;
	/**
	 * 已删除
	 */
	private String deleted = "N";
	/**
	 * 删除ID
	 */
	private String deletedById;
	/**
	 * 删除name
	 */
	private String deletedByName;
	/**
	 * 删除日期
	 */
	private Timestamp deletedDate;
	/**
	 * 说明
	 */
	private String remark;
	/**
	 * 设备厂商id
	 */
	private String companyId;
	/**
	 * 設備廠商name
	 */
	private String companyName;
	/**
	 * 通訊模式id
	 */
	private String commModeId;
	/**
	 * 通訊模式name
	 */
	private String commModeName;
	/**
	 * 支援功能ID
	 */
	private String functionId;
	/**
	 * 支援功能name
	 */
	private String functionName;
	/**
	 * 支援功能id set
	 */
	private Set<String> functionIds;
	/**
	 * 设备厂商id set
	 */
	private Set<String> companyIds;
	/**
	 * 通讯模式id set
	 */
	private Set<String> commModeIds;
	
	/**
	 * Constructor: 無參構造函數
	 */
	public AssetTypeDTO() {
		super();
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
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}
	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}
	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
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
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the safetyStock
	 */
	public BigDecimal getSafetyStock() {
		return safetyStock;
	}
	/**
	 * @param safetyStock the safetyStock to set
	 */
	public void setSafetyStock(BigDecimal safetyStock) {
		this.safetyStock = safetyStock;
	}

	/**
	 * @return the assetCategoryName
	 */
	public String getAssetCategoryName() {
		return assetCategoryName;
	}

	/**
	 * @param assetCategoryName the assetCategoryName to set
	 */
	public void setAssetCategoryName(String assetCategoryName) {
		this.assetCategoryName = assetCategoryName;
	}

	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the commModeId
	 */
	public String getCommModeId() {
		return commModeId;
	}

	/**
	 * @param commModeId the commModeId to set
	 */
	public void setCommModeId(String commModeId) {
		this.commModeId = commModeId;
	}

	/**
	 * @return the commModeName
	 */
	public String getCommModeName() {
		return commModeName;
	}

	/**
	 * @param commModeName the commModeName to set
	 */
	public void setCommModeName(String commModeName) {
		this.commModeName = commModeName;
	}

	/**
	 * @return the functionId
	 */
	public String getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @return the functionIds
	 */
	public Set<String> getFunctionIds() {
		return functionIds;
	}

	/**
	 * @param functionIds the functionIds to set
	 */
	public void setFunctionIds(Set<String> functionIds) {
		this.functionIds = functionIds;
	}

	/**
	 * @return the companyIds
	 */
	public Set<String> getCompanyIds() {
		return companyIds;
	}

	/**
	 * @param companyIds the companyIds to set
	 */
	public void setCompanyIds(Set<String> companyIds) {
		this.companyIds = companyIds;
	}

	/**
	 * @return the commModeIds
	 */
	public Set<String> getCommModeIds() {
		return commModeIds;
	}

	/**
	 * @param commModeIds the commModeIds to set
	 */
	public void setCommModeIds(Set<String> commModeIds) {
		this.commModeIds = commModeIds;
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
	 * @return the deletedById
	 */
	public String getDeletedById() {
		return deletedById;
	}

	/**
	 * @param deletedById the deletedById to set
	 */
	public void setDeletedById(String deletedById) {
		this.deletedById = deletedById;
	}

	/**
	 * @return the deletedByName
	 */
	public String getDeletedByName() {
		return deletedByName;
	}

	/**
	 * @param deletedByName the deletedByName to set
	 */
	public void setDeletedByName(String deletedByName) {
		this.deletedByName = deletedByName;
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
}
