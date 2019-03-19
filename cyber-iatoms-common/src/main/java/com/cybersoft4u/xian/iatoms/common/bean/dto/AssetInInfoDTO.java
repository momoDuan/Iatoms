package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.DataTransferObject;
/**
 * 
 * Purpose: 設備入庫主檔DTO
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016/7/5
 * @MaintenancePersonnel CarrieDuan
 */
public class AssetInInfoDTO extends DataTransferObject<String> {

	public static enum ATTRIBUTE {
		ASSET_IN_ID("assetInId"),
		ASSET_LIST_ID("assetListId"),
		VENDOR_ID("companyId"),
		CONTRACT_ID("contractId"),
		CONTRACT_CODE("contractCode"),
		CUSTOMER_ID("customerId"),
		ASSET_TYPE_ID("assetTypeId"),
		ASSET_TYPE_NAME("assetTypeName"),
		KEEPER_NAME("keeperName"),
		WAREHOUSE_ID("warehouseId"),
		WAREHOUSE_NAME("warehouseName"),
		MA_TYPE("maType"),
		MA_TYPE_NAME("maTypeName"),
		GET_DATE("getDate"),
		APPROVE_DATE("cyberApprovedDate"),
		UPDATE_DATE("updatedDate"),
		UPDATE_DATE_STR("updateDateStr"),
		GUARANTEE_DATE("guaranteeDate"),
		ASSET_MODEL("assetModel"),
		GUARANTEE_DATE_STR("guaranteeDateStr"),
		CUSTOMER_GUARANTEE_DATE("customerGuaranteeDate"),
		CUSTOMER_GUARANTEE_DATE_STR("customerGuaranteeDateStr"),
		CUSTOMER_WARRANTY_DATE("customerWarrantyDate"),
		FACTORY_WARRANTY_DATE("factoryWarrantyDate"),
		CYBER_APPROVE_DATE("cyberApprovedDate"),
		COMMENT("comment"),
		CREATE_USER("createdById"),
		CREATE_USER_NAME("createdByName"),
		CREATE_DATE("createdDate"),
		UPDATE_USER("updatedById"),
		UPDATE_USER_NAME("updatedByName"),
		CUSTOME_APPROVE_DATE("customerApproveDate"),
		IS_DONE("isDone"),
		ASSETS_OWNER("owner"),
		USE_EMPLOYEE_NAME("userId"),
		SERIAL_NUMBER("serialNumber"),
		PROPERTY_ID("propertyId"),
		IS_CHECKED("isChecked"),
		IS_CUSTOMER_CHECKED("isCustomerChecked"),
		CHECKED_DATE("checkedDate"),
		IS_FINISHED("isFinished"),
		BRAND("brand"),
		VENDOR("vendor");
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 設備ID
	 */
	private String assetInId;
	/**
	 * 廠商編號
	 */
	private String companyId;
	/**
	 * 合約ID
	 */
	private String contractId;
	/**
	 * 合約編號
	 */
	private String contractCode;
	/**
	 * 設備類別ID
	 */
	private String assetTypeId;	
	/**
	 * 客戶ID
	 */
	private String customerId;
	/**
	 * 設備類別名稱
	 */
	private String assetTypeName;	
	/**
	 * 保管人
	 */
	private String keeperName;
	/**
	 * 倉庫代碼
	 */
	private String warehouseId;
	/**
	 * 倉庫名稱
	 */
	private String warehouseName;
	/**
	 * 資產Owner
	 */
	private String owner;
	/**
	 * 使用人
	 */
	private String userId;
	
	/**
	 * 維護模式
	 */
	private String maType;
	/**
	 * 維護模式名稱
	 */
	private String maTypeName;
	/**
	 * 
	 */
	private Timestamp getDate;
	/**
	 * 
	 */
	private Timestamp approveDate;
	/**
	 * 更新日期
	 */
	private Timestamp updateDate;
	/**
	 * 
	 */
	private Timestamp guaranteeDate;
	/**
	 * 說明
	 */
	private String comment;
	/**
	 * 創建人員ID
	 */
	private String createdById;
	/**
	 * 創建人員名稱
	 */
	private String createdByName;
	/**
	 * 創建時間
	 */
	private Date createdDate;
	/**
	 * 更新人員ID
	 */
	private String updatedById;
	/**
	 * 更新人員名稱
	 */
	private String updatedByName;
	/**
	 * 更新日期
	 */
	private Timestamp updatedDate;
	/**
	 * 客戶實際驗收日期
	 */
	private Date customeApproveDate;
	/**
	 * 是否已入庫
	 */
	private String isDone = "N";
	/**
	 * 是否驗收完成
	 */
	private String isFinished = "N";
	/**
	 * owner
	 */
	private String assetsOwner;
	/**
	 * 使用人名稱
	 */
	private String useEmployeeName;
	/**
	 * 
	 */
	private String assetListId;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 財產編號
	 */
	private String propertyId;
	/**
	 * 是否核檢
	 */
	private String isChecked;
	/**
	 * 是否客戶實際驗收
	 */
	private String isCustomerChecked;
	/**
	 * 廠商
	 */
	private String vendor;
	/**
	 * 
	 */
	private Timestamp customerGuaranteeDate;
	/**
	 * 
	 */
	private String customerGuaranteeDateStr;
	/**
	 * CYBER驗證日期
	 */
	private Date cyberApprovedDate;//cyber驗收
	/**
	 * 客戶驗收日期
	 */
	private Date customerApproveDate;//客戶驗收
	/**
	 * 客戶保固日期
	 */
	private Date customerWarrantyDate;//客戶保固
	/**
	 * 原廠保固日期
	 */
	private Date factoryWarrantyDate;//原廠保固
	/**
	 * 
	 */
	private String guaranteeDateStr;
	/**
	 * 
	 */
	private String updateDateStr;
	
	//實際驗收日期
	private Timestamp checkedDate;
	/**
	 * 
	 */
	private List<Parameter> assetTypeList;
	
	/**
	 * 設備型號
	 */
	private String assetModel;
	/**
	 * 設備廠牌
	 */
	private String brand;
	

	/**
	 * @return the assetInId
	 */
	public String getAssetInId() {
		return assetInId;
	}

	/**
	 * @param assetInId the assetInId to set
	 */
	public void setAssetInId(String assetInId) {
		this.assetInId = assetInId;
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
	 * @return the contractCode
	 */
	public String getContractCode() {
		return contractCode;
	}

	/**
	 * @param contractCode the contractCode to set
	 */
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
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
	 * @return the keeperName
	 */
	public String getKeeperName() {
		return keeperName;
	}

	/**
	 * @param keeperName the keeperName to set
	 */
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
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
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the maType
	 */
	public String getMaType() {
		return maType;
	}

	/**
	 * @param maType the maType to set
	 */
	public void setMaType(String maType) {
		this.maType = maType;
	}

	/**
	 * @return the maTypeName
	 */
	public String getMaTypeName() {
		return maTypeName;
	}

	/**
	 * @param maTypeName the maTypeName to set
	 */
	public void setMaTypeName(String maTypeName) {
		this.maTypeName = maTypeName;
	}

	/**
	 * @return the getDate
	 */
	public Timestamp getGetDate() {
		return getDate;
	}

	/**
	 * @param getDate the getDate to set
	 */
	public void setGetDate(Timestamp getDate) {
		this.getDate = getDate;
	}

	/**
	 * @return the approveDate
	 */
	public Timestamp getApproveDate() {
		return approveDate;
	}

	/**
	 * @param approveDate the approveDate to set
	 */
	public void setApproveDate(Timestamp approveDate) {
		this.approveDate = approveDate;
	}

	/**
	 * @return the updateDate
	 */
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the guaranteeDate
	 */
	public Timestamp getGuaranteeDate() {
		return guaranteeDate;
	}

	/**
	 * @param guaranteeDate the guaranteeDate to set
	 */
	public void setGuaranteeDate(Timestamp guaranteeDate) {
		this.guaranteeDate = guaranteeDate;
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
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
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
	 * @return the customeApproveDate
	 */
	public Date getCustomeApproveDate() {
		return customeApproveDate;
	}

	/**
	 * @param customeApproveDate the customeApproveDate to set
	 */
	public void setCustomeApproveDate(Date customeApproveDate) {
		this.customeApproveDate = customeApproveDate;
	}

	/**
	 * @return the isDone
	 */
	public String getIsDone() {
		return isDone;
	}

	/**
	 * @param isDone the isDone to set
	 */
	public void setIsDone(String isDone) {
		this.isDone = isDone;
	}

	/**
	 * @return the assetsOwner
	 */
	public String getAssetsOwner() {
		return assetsOwner;
	}

	/**
	 * @param assetsOwner the assetsOwner to set
	 */
	public void setAssetsOwner(String assetsOwner) {
		this.assetsOwner = assetsOwner;
	}

	/**
	 * @return the useEmployeeName
	 */
	public String getUseEmployeeName() {
		return useEmployeeName;
	}

	/**
	 * @param useEmployeeName the useEmployeeName to set
	 */
	public void setUseEmployeeName(String useEmployeeName) {
		this.useEmployeeName = useEmployeeName;
	}

	/**
	 * @return the assetListId
	 */
	public String getAssetListId() {
		return assetListId;
	}

	/**
	 * @param assetListId the assetListId to set
	 */
	public void setAssetListId(String assetListId) {
		this.assetListId = assetListId;
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
	 * @return the isChecked
	 */
	public String getIsChecked() {
		return isChecked;
	}

	/**
	 * @param isChecked the isChecked to set
	 */
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * @return the isCustomerChecked
	 */
	public String getIsCustomerChecked() {
		return isCustomerChecked;
	}

	/**
	 * @param isCustomerChecked the isCustomerChecked to set
	 */
	public void setIsCustomerChecked(String isCustomerChecked) {
		this.isCustomerChecked = isCustomerChecked;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the customerGuaranteeDate
	 */
	public Timestamp getCustomerGuaranteeDate() {
		return customerGuaranteeDate;
	}

	/**
	 * @param customerGuaranteeDate the customerGuaranteeDate to set
	 */
	public void setCustomerGuaranteeDate(Timestamp customerGuaranteeDate) {
		this.customerGuaranteeDate = customerGuaranteeDate;
	}

	/**
	 * @return the customerGuaranteeDateStr
	 */
	public String getCustomerGuaranteeDateStr() {
		return customerGuaranteeDateStr;
	}

	/**
	 * @param customerGuaranteeDateStr the customerGuaranteeDateStr to set
	 */
	public void setCustomerGuaranteeDateStr(String customerGuaranteeDateStr) {
		this.customerGuaranteeDateStr = customerGuaranteeDateStr;
	}

	/**
	 * @return the cyberApprovedDate
	 */
	public Date getCyberApprovedDate() {
		return cyberApprovedDate;
	}

	/**
	 * @param cyberApprovedDate the cyberApprovedDate to set
	 */
	public void setCyberApprovedDate(Date cyberApprovedDate) {
		this.cyberApprovedDate = cyberApprovedDate;
	}

	/**
	 * @return the customerApproveDate
	 */
	public Date getCustomerApproveDate() {
		return customerApproveDate;
	}

	/**
	 * @param customerApproveDate the customerApproveDate to set
	 */
	public void setCustomerApproveDate(Date customerApproveDate) {
		this.customerApproveDate = customerApproveDate;
	}

	/**
	 * @return the customerWarrantyDate
	 */
	public Date getCustomerWarrantyDate() {
		return customerWarrantyDate;
	}

	/**
	 * @param customerWarrantyDate the customerWarrantyDate to set
	 */
	public void setCustomerWarrantyDate(Date customerWarrantyDate) {
		this.customerWarrantyDate = customerWarrantyDate;
	}

	/**
	 * @return the factoryWarrantyDate
	 */
	public Date getFactoryWarrantyDate() {
		return factoryWarrantyDate;
	}

	/**
	 * @param factoryWarrantyDate the factoryWarrantyDate to set
	 */
	public void setFactoryWarrantyDate(Date factoryWarrantyDate) {
		this.factoryWarrantyDate = factoryWarrantyDate;
	}

	/**
	 * @return the guaranteeDateStr
	 */
	public String getGuaranteeDateStr() {
		return guaranteeDateStr;
	}

	/**
	 * @param guaranteeDateStr the guaranteeDateStr to set
	 */
	public void setGuaranteeDateStr(String guaranteeDateStr) {
		this.guaranteeDateStr = guaranteeDateStr;
	}

	/**
	 * @return the updateDateStr
	 */
	public String getUpdateDateStr() {
		return updateDateStr;
	}

	/**
	 * @param updateDateStr the updateDateStr to set
	 */
	public void setUpdateDateStr(String updateDateStr) {
		this.updateDateStr = updateDateStr;
	}

	/**
	 * @return the checkedDate
	 */
	public Timestamp getCheckedDate() {
		return checkedDate;
	}

	/**
	 * @param checkedDate the checkedDate to set
	 */
	public void setCheckedDate(Timestamp checkedDate) {
		this.checkedDate = checkedDate;
	}

	/**
	 * @return the assetTypeList
	 */
	public List<Parameter> getAssetTypeList() {
		return assetTypeList;
	}

	/**
	 * @param assetTypeList the assetTypeList to set
	 */
	public void setAssetTypeList(List<Parameter> assetTypeList) {
		this.assetTypeList = assetTypeList;
	}

	/**
	 * @return the isFinished
	 */
	public String getIsFinished() {
		return isFinished;
	}

	/**
	 * @param isFinished the isFinished to set
	 */
	public void setIsFinished(String isFinished) {
		this.isFinished = isFinished;
	}

	/**
	 * @return the assetModel
	 */
	public String getAssetModel() {
		return assetModel;
	}

	/**
	 * @param assetModel the assetModel to set
	 */
	public void setAssetModel(String assetModel) {
		this.assetModel = assetModel;
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
	
	
}
