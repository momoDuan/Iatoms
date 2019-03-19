package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 案件處理DTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/11/8
 * @MaintenancePersonnel CrissZhang
 */
public class RestApiCaseHandleInfoDTO extends DataTransferObject<String>{
	

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 3858358005201703585L;


	
	/**
	 * 案件單號
	 */
	private String caseId;
	
	/**
	 * 案件種類 ex:新裝機,倂機等等
	 */
	private String caseCategory;
	
	/**
	 * 客戶編號
	 */
	private String customerId;  
	
	/**
	 * 合約ID
	 */
	private String contractId;
	
	/**
	 * 需求單號
	 */
	private String requirementNo;
	
	/**
	 * 裝機/拆機類型
	 */
	private String installOrUninstalledType;
	/**
	 * 裝機/拆機地址
	 */
	private String installOrUninstalledAddress;
	/**
	 * 裝機/拆機聯絡人
	 */
	private String installOrUninstalledContact;
	/**
	 * 裝機/拆機聯絡人電話
	 */
	private String installOrUninstalledPhone;
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 維護廠商
	 */
	private String companyId;
	/**
	 * 維護廠商名稱
	 */
	private String companyName;
	/**
	 * 維護部門
	 */
	private String departmentId;
	/**
	 * 維護部門名稱
	 */
	private String departmentName;
	/**
	 * 維護部門
	 */
	private String vendorDept;
	/**
	 * 維護部門名稱
	 */
	private String vendorDeptName;
	
	/**
	 * 案件類型ex:急件/預約/一般
	 */
	private String caseType;
	/**
	 * 案件類型名稱
	 */
	private String caseTypeName;
	
	/**
	 * 特店代號
	 */
	private String merchantCode;
	
	/**
	 * 特店標頭主鍵
	 */
	private String merchantHeaderId;
	/**
	 * 特點表頭名稱
	 */
	private String merchantHeaderName;
	/**
	 * 營業地址
	 */
	private String bussinessAddress;
	/**
	 * 營業地址-市區
	 */
	private String location;
	/**
	 * 裝機地址
	 */
	private String installedAdress;
	/**
	 * 裝機地址-市區 
	 */
	private String installedAdressLocation;
	/**
	 * 裝機地址-市區 名稱
	 */
	private String installedAdressLocationName;
	/**
	 * 是否同營業地址
	 */
	private String isBussinessAddress = "E";
	/**
	 * 裝機聯絡人
	 */
	private String installedContact;
	/**
	 * 是否同特店聯絡人
	 */
	private String isBussinessContact = "E";
	/**
	 * 裝機人聯絡電話
	 */
	private String installedContactPhone;
	/**
	 * 是否同特店聯絡人電話
	 */
	private String isBussinessContactPhone = "E";
	
	/**
	 * 其他說明
	 */
	private String description;
	
	/**
	 * 報修原因
	 */
	private String repairReason;
	/**
	 * 報修原因名稱
	 */
	private String repairReasonName;
	/**
	 * 報修次數
	 */
	private Integer repairTimes;
	/**
	 * 拆機類型
	 */
	private String uninstallType;
	/**
	 * 拆機類型名稱
	 */
	private String uninstallTypeName;
	/**
	 * 新增人員編號
	 */
	private String createdById;
	/**
	 * 新增人員名稱
	 */
	private String createdByName;
	/**
	 * 新增日期
	 */
	private Timestamp createdDate;
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
	
	
	/**
	 * 特點名稱
	 */
	private String merchantName;
	
	/**
	 * 設備名稱
	 */
	private String assetName;
	/**
	 * 設備Owner
	 */
	private String assetOwner;
	/**
	 * 設備型號
	 */
	private String model;
	
	/**
	 * 設備序號
	 */
	private String serialNumber;
	
	/**
	 * 設備啟用日期
	 */
	private Timestamp enableDate;
	
	/**
	 * 特店ID 
	 */
	private String merchantId;
	
	/**
	 * 特店區域
	 */
	private String area;
	/**
	 * 特店區域名稱
	 */
	private String areaName;
	/**
	 * 特店聯絡人
	 */
	private String contact;
	/**
	 * 特點聯絡人電話1
	 */
	private String contactTel;
	/**
	 * 特店聯絡人電話2
	 */
	private String contactTel2;
	/**
	 * 移動電話
	 */
	private String phone;
	/**
	 * 營業地址
	 */
	private String businessAddress;
	/**
	 * 營業時間-起
	 */
	private String openHour;
	/**
	 * 營業時間-止
	 */
	private String closeHour;
	/**
	 * 營業時間
	 */
	private String businessHours;
	/**
	 * AOEmail
	 */
	private String aoemail;
	/**
	 * AOName
	 */
	private String aoName;
	
	/**
	 * 異動說明
	 */
	private String updatedDescription;
	/**
	 * 特點區域
	 */
	private String merLocation;
	/**
	 * 
	 */
	private String locationName;
	/**
	 * 
	 */
	private String contactAddressLocationName;
	
	
	
	/**
	 * 案件狀態
	 */
	private String caseStatus;
	
	/**
	 * 案件狀態名稱
	 */
	private String caseStatusName;
	
	/**
	 * 程式名稱
	 */
	private String applicationName;
	
	
	/**
	 * 聯絡地址縣市
	 */
	private String uninstalledAdressLocation;
	/**
	 * 聯絡地址縣市 
	 */
	private String contactAddressLocation;
	/**
	 * 聯繫地址
	 */
	private String contactAddress;
	/**
	 * 聯繫聯絡人
	 */
	private String contactUser;
	/**
	 * 聯絡人電話
	 */
	private String contactUserPhone;
	
	
	/**
	 * 聯繫同營業地址
	 */
	private String contactIsBussinessAddress = "E";
	/**
	 * 聯繫同特店聯絡人
	 */
	private String contactIsBussinessContact = "E";
	/**
	 * 聯繫同特店聯絡電話
	 */
	private String contactIsBussinessContactPhone = "E";
	
	/**
	 * 最近第一次處理說明
	 */
	private String firstDescription;
	/** 
	 * 最近第二次處理說明
	 */
	private String secondDescription;
	/**
	 * 最近第三次處理說明
	 */
	private String thirdDescription;
	
	/**
	 * 授權密文
	 */
	private String verificationCode;
	/**
	 * 令牌
	 */
	private String token;
	
	/**
	 * Constructor:無參構造函數
	 */
	public RestApiCaseHandleInfoDTO() {
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
	 * @return the requirementNo
	 */
	public String getRequirementNo() {
		return requirementNo;
	}

	/**
	 * @param requirementNo the requirementNo to set
	 */
	public void setRequirementNo(String requirementNo) {
		this.requirementNo = requirementNo;
	}

	/**
	 * @return the installOrUninstalledType
	 */
	public String getInstallOrUninstalledType() {
		return installOrUninstalledType;
	}

	/**
	 * @param installOrUninstalledType the installOrUninstalledType to set
	 */
	public void setInstallOrUninstalledType(String installOrUninstalledType) {
		this.installOrUninstalledType = installOrUninstalledType;
	}

	/**
	 * @return the installOrUninstalledAddress
	 */
	public String getInstallOrUninstalledAddress() {
		return installOrUninstalledAddress;
	}

	/**
	 * @param installOrUninstalledAddress the installOrUninstalledAddress to set
	 */
	public void setInstallOrUninstalledAddress(String installOrUninstalledAddress) {
		this.installOrUninstalledAddress = installOrUninstalledAddress;
	}

	/**
	 * @return the installOrUninstalledContact
	 */
	public String getInstallOrUninstalledContact() {
		return installOrUninstalledContact;
	}

	/**
	 * @param installOrUninstalledContact the installOrUninstalledContact to set
	 */
	public void setInstallOrUninstalledContact(String installOrUninstalledContact) {
		this.installOrUninstalledContact = installOrUninstalledContact;
	}

	/**
	 * @return the installOrUninstalledPhone
	 */
	public String getInstallOrUninstalledPhone() {
		return installOrUninstalledPhone;
	}

	/**
	 * @param installOrUninstalledPhone the installOrUninstalledPhone to set
	 */
	public void setInstallOrUninstalledPhone(String installOrUninstalledPhone) {
		this.installOrUninstalledPhone = installOrUninstalledPhone;
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
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the vendorDept
	 */
	public String getVendorDept() {
		return vendorDept;
	}

	/**
	 * @param vendorDept the vendorDept to set
	 */
	public void setVendorDept(String vendorDept) {
		this.vendorDept = vendorDept;
	}

	/**
	 * @return the vendorDeptName
	 */
	public String getVendorDeptName() {
		return vendorDeptName;
	}

	/**
	 * @param vendorDeptName the vendorDeptName to set
	 */
	public void setVendorDeptName(String vendorDeptName) {
		this.vendorDeptName = vendorDeptName;
	}

	/**
	 * @return the caseType
	 */
	public String getCaseType() {
		return caseType;
	}

	/**
	 * @param caseType the caseType to set
	 */
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	/**
	 * @return the caseTypeName
	 */
	public String getCaseTypeName() {
		return caseTypeName;
	}

	/**
	 * @param caseTypeName the caseTypeName to set
	 */
	public void setCaseTypeName(String caseTypeName) {
		this.caseTypeName = caseTypeName;
	}

	/**
	 * @return the merchantCode
	 */
	public String getMerchantCode() {
		return merchantCode;
	}

	/**
	 * @param merchantCode the merchantCode to set
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * @return the merchantHeaderId
	 */
	public String getMerchantHeaderId() {
		return merchantHeaderId;
	}

	/**
	 * @param merchantHeaderId the merchantHeaderId to set
	 */
	public void setMerchantHeaderId(String merchantHeaderId) {
		this.merchantHeaderId = merchantHeaderId;
	}

	/**
	 * @return the merchantHeaderName
	 */
	public String getMerchantHeaderName() {
		return merchantHeaderName;
	}

	/**
	 * @param merchantHeaderName the merchantHeaderName to set
	 */
	public void setMerchantHeaderName(String merchantHeaderName) {
		this.merchantHeaderName = merchantHeaderName;
	}

	/**
	 * @return the bussinessAddress
	 */
	public String getBussinessAddress() {
		return bussinessAddress;
	}

	/**
	 * @param bussinessAddress the bussinessAddress to set
	 */
	public void setBussinessAddress(String bussinessAddress) {
		this.bussinessAddress = bussinessAddress;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the installedAdress
	 */
	public String getInstalledAdress() {
		return installedAdress;
	}

	/**
	 * @param installedAdress the installedAdress to set
	 */
	public void setInstalledAdress(String installedAdress) {
		this.installedAdress = installedAdress;
	}

	/**
	 * @return the installedAdressLocation
	 */
	public String getInstalledAdressLocation() {
		return installedAdressLocation;
	}

	/**
	 * @param installedAdressLocation the installedAdressLocation to set
	 */
	public void setInstalledAdressLocation(String installedAdressLocation) {
		this.installedAdressLocation = installedAdressLocation;
	}

	/**
	 * @return the installedAdressLocationName
	 */
	public String getInstalledAdressLocationName() {
		return installedAdressLocationName;
	}

	/**
	 * @param installedAdressLocationName the installedAdressLocationName to set
	 */
	public void setInstalledAdressLocationName(String installedAdressLocationName) {
		this.installedAdressLocationName = installedAdressLocationName;
	}

	/**
	 * @return the isBussinessAddress
	 */
	public String getIsBussinessAddress() {
		return isBussinessAddress;
	}

	/**
	 * @param isBussinessAddress the isBussinessAddress to set
	 */
	public void setIsBussinessAddress(String isBussinessAddress) {
		this.isBussinessAddress = isBussinessAddress;
	}

	/**
	 * @return the installedContact
	 */
	public String getInstalledContact() {
		return installedContact;
	}

	/**
	 * @param installedContact the installedContact to set
	 */
	public void setInstalledContact(String installedContact) {
		this.installedContact = installedContact;
	}

	/**
	 * @return the isBussinessContact
	 */
	public String getIsBussinessContact() {
		return isBussinessContact;
	}

	/**
	 * @param isBussinessContact the isBussinessContact to set
	 */
	public void setIsBussinessContact(String isBussinessContact) {
		this.isBussinessContact = isBussinessContact;
	}

	/**
	 * @return the installedContactPhone
	 */
	public String getInstalledContactPhone() {
		return installedContactPhone;
	}

	/**
	 * @param installedContactPhone the installedContactPhone to set
	 */
	public void setInstalledContactPhone(String installedContactPhone) {
		this.installedContactPhone = installedContactPhone;
	}

	/**
	 * @return the isBussinessContactPhone
	 */
	public String getIsBussinessContactPhone() {
		return isBussinessContactPhone;
	}

	/**
	 * @param isBussinessContactPhone the isBussinessContactPhone to set
	 */
	public void setIsBussinessContactPhone(String isBussinessContactPhone) {
		this.isBussinessContactPhone = isBussinessContactPhone;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the repairReason
	 */
	public String getRepairReason() {
		return repairReason;
	}

	/**
	 * @param repairReason the repairReason to set
	 */
	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}

	/**
	 * @return the repairReasonName
	 */
	public String getRepairReasonName() {
		return repairReasonName;
	}

	/**
	 * @param repairReasonName the repairReasonName to set
	 */
	public void setRepairReasonName(String repairReasonName) {
		this.repairReasonName = repairReasonName;
	}

	/**
	 * @return the repairTimes
	 */
	public Integer getRepairTimes() {
		return repairTimes;
	}

	/**
	 * @param repairTimes the repairTimes to set
	 */
	public void setRepairTimes(Integer repairTimes) {
		this.repairTimes = repairTimes;
	}

	/**
	 * @return the uninstallType
	 */
	public String getUninstallType() {
		return uninstallType;
	}

	/**
	 * @param uninstallType the uninstallType to set
	 */
	public void setUninstallType(String uninstallType) {
		this.uninstallType = uninstallType;
	}

	/**
	 * @return the uninstallTypeName
	 */
	public String getUninstallTypeName() {
		return uninstallTypeName;
	}

	/**
	 * @param uninstallTypeName the uninstallTypeName to set
	 */
	public void setUninstallTypeName(String uninstallTypeName) {
		this.uninstallTypeName = uninstallTypeName;
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
	 * @return the merchantName
	 */
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * @param merchantName the merchantName to set
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/**
	 * @return the assetName
	 */
	public String getAssetName() {
		return assetName;
	}

	/**
	 * @param assetName the assetName to set
	 */
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	/**
	 * @return the assetOwner
	 */
	public String getAssetOwner() {
		return assetOwner;
	}

	/**
	 * @param assetOwner the assetOwner to set
	 */
	public void setAssetOwner(String assetOwner) {
		this.assetOwner = assetOwner;
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
	 * @return the merchantId
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the areaName
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * @param areaName the areaName to set
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the contactTel
	 */
	public String getContactTel() {
		return contactTel;
	}

	/**
	 * @param contactTel the contactTel to set
	 */
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	/**
	 * @return the contactTel2
	 */
	public String getContactTel2() {
		return contactTel2;
	}

	/**
	 * @param contactTel2 the contactTel2 to set
	 */
	public void setContactTel2(String contactTel2) {
		this.contactTel2 = contactTel2;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the businessAddress
	 */
	public String getBusinessAddress() {
		return businessAddress;
	}

	/**
	 * @param businessAddress the businessAddress to set
	 */
	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	/**
	 * @return the openHour
	 */
	public String getOpenHour() {
		return openHour;
	}

	/**
	 * @param openHour the openHour to set
	 */
	public void setOpenHour(String openHour) {
		this.openHour = openHour;
	}

	/**
	 * @return the closeHour
	 */
	public String getCloseHour() {
		return closeHour;
	}

	/**
	 * @param closeHour the closeHour to set
	 */
	public void setCloseHour(String closeHour) {
		this.closeHour = closeHour;
	}

	/**
	 * @return the businessHours
	 */
	public String getBusinessHours() {
		return businessHours;
	}

	/**
	 * @param businessHours the businessHours to set
	 */
	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

	/**
	 * @return the aoemail
	 */
	public String getAoemail() {
		return aoemail;
	}

	/**
	 * @param aoemail the aoemail to set
	 */
	public void setAoemail(String aoemail) {
		this.aoemail = aoemail;
	}

	/**
	 * @return the aoName
	 */
	public String getAoName() {
		return aoName;
	}

	/**
	 * @param aoName the aoName to set
	 */
	public void setAoName(String aoName) {
		this.aoName = aoName;
	}

	/**
	 * @return the updatedDescription
	 */
	public String getUpdatedDescription() {
		return updatedDescription;
	}

	/**
	 * @param updatedDescription the updatedDescription to set
	 */
	public void setUpdatedDescription(String updatedDescription) {
		this.updatedDescription = updatedDescription;
	}

	/**
	 * @return the merLocation
	 */
	public String getMerLocation() {
		return merLocation;
	}

	/**
	 * @param merLocation the merLocation to set
	 */
	public void setMerLocation(String merLocation) {
		this.merLocation = merLocation;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @return the contactAddressLocationName
	 */
	public String getContactAddressLocationName() {
		return contactAddressLocationName;
	}

	/**
	 * @param contactAddressLocationName the contactAddressLocationName to set
	 */
	public void setContactAddressLocationName(String contactAddressLocationName) {
		this.contactAddressLocationName = contactAddressLocationName;
	}

	/**
	 * @return the caseStatus
	 */
	public String getCaseStatus() {
		return caseStatus;
	}

	/**
	 * @param caseStatus the caseStatus to set
	 */
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	/**
	 * @return the caseStatusName
	 */
	public String getCaseStatusName() {
		return caseStatusName;
	}

	/**
	 * @param caseStatusName the caseStatusName to set
	 */
	public void setCaseStatusName(String caseStatusName) {
		this.caseStatusName = caseStatusName;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the uninstalledAdressLocation
	 */
	public String getUninstalledAdressLocation() {
		return uninstalledAdressLocation;
	}

	/**
	 * @param uninstalledAdressLocation the uninstalledAdressLocation to set
	 */
	public void setUninstalledAdressLocation(String uninstalledAdressLocation) {
		this.uninstalledAdressLocation = uninstalledAdressLocation;
	}

	/**
	 * @return the contactAddressLocation
	 */
	public String getContactAddressLocation() {
		return contactAddressLocation;
	}

	/**
	 * @param contactAddressLocation the contactAddressLocation to set
	 */
	public void setContactAddressLocation(String contactAddressLocation) {
		this.contactAddressLocation = contactAddressLocation;
	}

	/**
	 * @return the contactAddress
	 */
	public String getContactAddress() {
		return contactAddress;
	}

	/**
	 * @param contactAddress the contactAddress to set
	 */
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	/**
	 * @return the contactUser
	 */
	public String getContactUser() {
		return contactUser;
	}

	/**
	 * @param contactUser the contactUser to set
	 */
	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}

	/**
	 * @return the contactUserPhone
	 */
	public String getContactUserPhone() {
		return contactUserPhone;
	}

	/**
	 * @param contactUserPhone the contactUserPhone to set
	 */
	public void setContactUserPhone(String contactUserPhone) {
		this.contactUserPhone = contactUserPhone;
	}

	/**
	 * @return the contactIsBussinessAddress
	 */
	public String getContactIsBussinessAddress() {
		return contactIsBussinessAddress;
	}

	/**
	 * @param contactIsBussinessAddress the contactIsBussinessAddress to set
	 */
	public void setContactIsBussinessAddress(String contactIsBussinessAddress) {
		this.contactIsBussinessAddress = contactIsBussinessAddress;
	}

	/**
	 * @return the contactIsBussinessContact
	 */
	public String getContactIsBussinessContact() {
		return contactIsBussinessContact;
	}

	/**
	 * @param contactIsBussinessContact the contactIsBussinessContact to set
	 */
	public void setContactIsBussinessContact(String contactIsBussinessContact) {
		this.contactIsBussinessContact = contactIsBussinessContact;
	}

	/**
	 * @return the contactIsBussinessContactPhone
	 */
	public String getContactIsBussinessContactPhone() {
		return contactIsBussinessContactPhone;
	}

	/**
	 * @param contactIsBussinessContactPhone the contactIsBussinessContactPhone to set
	 */
	public void setContactIsBussinessContactPhone(
			String contactIsBussinessContactPhone) {
		this.contactIsBussinessContactPhone = contactIsBussinessContactPhone;
	}

	/**
	 * @return the firstDescription
	 */
	public String getFirstDescription() {
		return firstDescription;
	}

	/**
	 * @param firstDescription the firstDescription to set
	 */
	public void setFirstDescription(String firstDescription) {
		this.firstDescription = firstDescription;
	}

	/**
	 * @return the secondDescription
	 */
	public String getSecondDescription() {
		return secondDescription;
	}

	/**
	 * @param secondDescription the secondDescription to set
	 */
	public void setSecondDescription(String secondDescription) {
		this.secondDescription = secondDescription;
	}

	/**
	 * @return the thirdDescription
	 */
	public String getThirdDescription() {
		return thirdDescription;
	}

	/**
	 * @param thirdDescription the thirdDescription to set
	 */
	public void setThirdDescription(String thirdDescription) {
		this.thirdDescription = thirdDescription;
	}

	/**
	 * @return the verificationCode
	 */
	public String getVerificationCode() {
		return verificationCode;
	}

	/**
	 * @param verificationCode the verificationCode to set
	 */
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	

}