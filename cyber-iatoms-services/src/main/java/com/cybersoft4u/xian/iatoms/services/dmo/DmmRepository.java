package com.cybersoft4u.xian.iatoms.services.dmo;

// Generated 2017/1/24 �U�� 02:56:57 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;

import cafe.core.dmo.DomainModelObject;

/**
 * DmmRepository generated by hbm2java
 */
public class DmmRepository extends DomainModelObject<String,DmmRepositoryDTO> {

	private String assetId;
	private String serialNumber;
	private String assetTypeId;
	private String propertyId;
	private String warehouseId;
	private String contractId;
	private String maType;
	private String status;
	private String isEnabled = "N";
	private Date enableDate;
	private Date simEnableDate;
	private String simEnableNo;
	private String carrier;
	private Date carryDate;
	private String borrower;
	private Date borrowerStart;
	private Date borrowerEnd;
	private String borrowerEmail;
	private String borrowerMgrEmail;
	private Date backDate;
	private String assetOwner;
	private String assetUser;
	private String isCup = "N";
	private String retireReasonCode;
	private String caseId;
	private Date caseCompletionDate;
	private String tid;
	private String dtid;
	private String applicationId;
	private String merchantId;
	private String assetInId;
	private Date assetInTime;
	private String assetTransId;
	private String maintainCompany;
	private String repairVendor;
	private String description;
	private String action;
	private String faultComponent;
	private String faultDescription;
	private Date customerWarrantyDate;
	private Date factoryWarrantyDate;
	private String createUser;
	private String createUserName;
	private Date createDate;
	private String updateUser;
	private String updateUserName;
	private Date updateDate;
	private Date checkedDate;
	private String assetModel;
	private String installedAdress;
	private String installType;
	private String merchantHeaderId;
	private Date cyberApprovedDate;
	private String keeperName;
	private String installedAdressLocation;
	private String isChecked = "N";
	private String isCustomerChecked = "N";
	private String departmentId;
	private String repairCompany;
	private String brand;
	private String maintainUser;
	private Date analyzeDate;
	private String uninstallOrRepairReason;
	private String counter;
	private String cartonNo;
	private String installedDeptId;
	private String carryComment;
	private String disabledComment;
	private String repairComment;

	public DmmRepository() {
	}

	public DmmRepository(String assetId, String serialNumber,
			String assetTypeId, String warehouseId, String maType,
			String status, String isEnabled, String isCup, String assetInId,
			String createUser, String createUserName,
			Date createDate, String updateUser,
			String updateUserName, Date updateDate) {
		this.assetId = assetId;
		this.serialNumber = serialNumber;
		this.assetTypeId = assetTypeId;
		this.warehouseId = warehouseId;
		this.maType = maType;
		this.status = status;
		this.isEnabled = isEnabled;
		this.isCup = isCup;
		this.assetInId = assetInId;
		this.createUser = createUser;
		this.createUserName = createUserName;
		this.createDate = createDate;
		this.updateUser = updateUser;
		this.updateUserName = updateUserName;
		this.updateDate = updateDate;
	}

	public DmmRepository(String assetId, String serialNumber,
			String assetTypeId, String propertyId, String warehouseId,
			String contractId, String maType, String status, String isEnabled,
			Date enableDate, Date simEnableDate, String simEnableNo,
			String carrier, Date carryDate, String borrower,
			Date borrowerStart, Date borrowerEnd, String borrowerEmail,
			String borrowerMgrEmail, Date backDate, String assetOwner,
			String assetUser, String isCup, String retireReasonCode,
			String caseId, Date caseCompletionDate, String tid, String dtid,
			String applicationId, String merchantId, String assetInId,
			Date assetInTime, String assetTransId, String maintainCompany,
			String repairVendor, String description, String action,
			String faultComponent, String faultDescription,
			Date customerWarrantyDate, Date factoryWarrantyDate,
			String createUser, String createUserName, Date createDate,
			String updateUser, String updateUserName, Date updateDate,
			Date checkedDate, String assetModel, String installedAdress,
			String installType, String merchantHeaderId,
			Date cyberApprovedDate, String keeperName,
			String installedAdressLocation, String isChecked,
			String isCustomerChecked, String departmentId,
			String repairCompany, String brand, String maintainUser,
			Date analyzeDate, String uninstallOrRepairReason, String counter,
			String cartonNo, String installedDeptId, String carryComment,
			String disabledComment, String repairComment) {
		this.assetId = assetId;
		this.serialNumber = serialNumber;
		this.assetTypeId = assetTypeId;
		this.propertyId = propertyId;
		this.warehouseId = warehouseId;
		this.contractId = contractId;
		this.maType = maType;
		this.status = status;
		this.isEnabled = isEnabled;
		this.enableDate = enableDate;
		this.simEnableDate = simEnableDate;
		this.simEnableNo = simEnableNo;
		this.carrier = carrier;
		this.carryDate = carryDate;
		this.borrower = borrower;
		this.borrowerStart = borrowerStart;
		this.borrowerEnd = borrowerEnd;
		this.borrowerEmail = borrowerEmail;
		this.borrowerMgrEmail = borrowerMgrEmail;
		this.backDate = backDate;
		this.assetOwner = assetOwner;
		this.assetUser = assetUser;
		this.isCup = isCup;
		this.retireReasonCode = retireReasonCode;
		this.caseId = caseId;
		this.caseCompletionDate = caseCompletionDate;
		this.tid = tid;
		this.dtid = dtid;
		this.applicationId = applicationId;
		this.merchantId = merchantId;
		this.assetInId = assetInId;
		this.assetInTime = assetInTime;
		this.assetTransId = assetTransId;
		this.maintainCompany = maintainCompany;
		this.repairVendor = repairVendor;
		this.description = description;
		this.action = action;
		this.faultComponent = faultComponent;
		this.faultDescription = faultDescription;
		this.customerWarrantyDate = customerWarrantyDate;
		this.factoryWarrantyDate = factoryWarrantyDate;
		this.createUser = createUser;
		this.createUserName = createUserName;
		this.createDate = createDate;
		this.updateUser = updateUser;
		this.updateUserName = updateUserName;
		this.updateDate = updateDate;
		this.checkedDate = checkedDate;
		this.assetModel = assetModel;
		this.installedAdress = installedAdress;
		this.installType = installType;
		this.merchantHeaderId = merchantHeaderId;
		this.cyberApprovedDate = cyberApprovedDate;
		this.keeperName = keeperName;
		this.installedAdressLocation = installedAdressLocation;
		this.isChecked = isChecked;
		this.isCustomerChecked = isCustomerChecked;
		this.departmentId = departmentId;
		this.repairCompany = repairCompany;
		this.brand = brand;
		this.maintainUser = maintainUser;
		this.analyzeDate = analyzeDate;
		this.uninstallOrRepairReason = uninstallOrRepairReason;
		this.counter = counter;
		this.cartonNo = cartonNo;
		this.installedDeptId = installedDeptId;
		this.carryComment = carryComment;
		this.disabledComment = disabledComment;
		this.repairComment = repairComment;
	}

	public String getAssetId() {
		return this.assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
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

	public String getPropertyId() {
		return this.propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getContractId() {
		return this.contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getMaType() {
		return this.maType;
	}

	public void setMaType(String maType) {
		this.maType = maType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Date getEnableDate() {
		return this.enableDate;
	}

	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}

	public Date getSimEnableDate() {
		return this.simEnableDate;
	}

	public void setSimEnableDate(Date simEnableDate) {
		this.simEnableDate = simEnableDate;
	}

	public String getSimEnableNo() {
		return this.simEnableNo;
	}

	public void setSimEnableNo(String simEnableNo) {
		this.simEnableNo = simEnableNo;
	}

	public String getCarrier() {
		return this.carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public Date getCarryDate() {
		return this.carryDate;
	}

	public void setCarryDate(Date carryDate) {
		this.carryDate = carryDate;
	}

	public String getBorrower() {
		return this.borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public Date getBorrowerStart() {
		return this.borrowerStart;
	}

	public void setBorrowerStart(Date borrowerStart) {
		this.borrowerStart = borrowerStart;
	}

	public Date getBorrowerEnd() {
		return this.borrowerEnd;
	}

	public void setBorrowerEnd(Date borrowerEnd) {
		this.borrowerEnd = borrowerEnd;
	}

	public String getBorrowerEmail() {
		return this.borrowerEmail;
	}

	public void setBorrowerEmail(String borrowerEmail) {
		this.borrowerEmail = borrowerEmail;
	}

	public String getBorrowerMgrEmail() {
		return this.borrowerMgrEmail;
	}

	public void setBorrowerMgrEmail(String borrowerMgrEmail) {
		this.borrowerMgrEmail = borrowerMgrEmail;
	}

	public Date getBackDate() {
		return this.backDate;
	}

	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}

	public String getAssetOwner() {
		return this.assetOwner;
	}

	public void setAssetOwner(String assetOwner) {
		this.assetOwner = assetOwner;
	}

	public String getAssetUser() {
		return this.assetUser;
	}

	public void setAssetUser(String assetUser) {
		this.assetUser = assetUser;
	}

	public String getIsCup() {
		return this.isCup;
	}

	public void setIsCup(String isCup) {
		this.isCup = isCup;
	}

	public String getRetireReasonCode() {
		return this.retireReasonCode;
	}

	public void setRetireReasonCode(String retireReasonCode) {
		this.retireReasonCode = retireReasonCode;
	}

	public String getCaseId() {
		return this.caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public Date getCaseCompletionDate() {
		return this.caseCompletionDate;
	}

	public void setCaseCompletionDate(Date caseCompletionDate) {
		this.caseCompletionDate = caseCompletionDate;
	}

	public String getTid() {
		return this.tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getDtid() {
		return this.dtid;
	}

	public void setDtid(String dtid) {
		this.dtid = dtid;
	}

	public String getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getAssetInId() {
		return this.assetInId;
	}

	public void setAssetInId(String assetInId) {
		this.assetInId = assetInId;
	}

	public Date getAssetInTime() {
		return this.assetInTime;
	}

	public void setAssetInTime(Date assetInTime) {
		this.assetInTime = assetInTime;
	}

	public String getAssetTransId() {
		return this.assetTransId;
	}

	public void setAssetTransId(String assetTransId) {
		this.assetTransId = assetTransId;
	}

	public String getMaintainCompany() {
		return this.maintainCompany;
	}

	public void setMaintainCompany(String maintainCompany) {
		this.maintainCompany = maintainCompany;
	}

	public String getRepairVendor() {
		return this.repairVendor;
	}

	public void setRepairVendor(String repairVendor) {
		this.repairVendor = repairVendor;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFaultComponent() {
		return this.faultComponent;
	}

	public void setFaultComponent(String faultComponent) {
		this.faultComponent = faultComponent;
	}

	public String getFaultDescription() {
		return this.faultDescription;
	}

	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}

	public Date getCustomerWarrantyDate() {
		return this.customerWarrantyDate;
	}

	public void setCustomerWarrantyDate(Date customerWarrantyDate) {
		this.customerWarrantyDate = customerWarrantyDate;
	}

	public Date getFactoryWarrantyDate() {
		return this.factoryWarrantyDate;
	}

	public void setFactoryWarrantyDate(Date factoryWarrantyDate) {
		this.factoryWarrantyDate = factoryWarrantyDate;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUserName() {
		return this.createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateUserName() {
		return this.updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCheckedDate() {
		return this.checkedDate;
	}

	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}

	public String getAssetModel() {
		return this.assetModel;
	}

	public void setAssetModel(String assetModel) {
		this.assetModel = assetModel;
	}

	public String getInstalledAdress() {
		return this.installedAdress;
	}

	public void setInstalledAdress(String installedAdress) {
		this.installedAdress = installedAdress;
	}

	public String getInstallType() {
		return this.installType;
	}

	public void setInstallType(String installType) {
		this.installType = installType;
	}

	public String getMerchantHeaderId() {
		return this.merchantHeaderId;
	}

	public void setMerchantHeaderId(String merchantHeaderId) {
		this.merchantHeaderId = merchantHeaderId;
	}

	public Date getCyberApprovedDate() {
		return this.cyberApprovedDate;
	}

	public void setCyberApprovedDate(Date cyberApprovedDate) {
		this.cyberApprovedDate = cyberApprovedDate;
	}

	public String getKeeperName() {
		return this.keeperName;
	}

	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}

	public String getInstalledAdressLocation() {
		return this.installedAdressLocation;
	}

	public void setInstalledAdressLocation(String installedAdressLocation) {
		this.installedAdressLocation = installedAdressLocation;
	}

	public String getIsChecked() {
		return this.isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public String getIsCustomerChecked() {
		return this.isCustomerChecked;
	}

	public void setIsCustomerChecked(String isCustomerChecked) {
		this.isCustomerChecked = isCustomerChecked;
	}
	
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the repairCompany
	 */
	public String getRepairCompany() {
		return repairCompany;
	}

	/**
	 * @param repairCompany the repairCompany to set
	 */
	public void setRepairCompany(String repairCompany) {
		this.repairCompany = repairCompany;
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
	 * @return the maintainUser
	 */
	public String getMaintainUser() {
		return maintainUser;
	}

	/**
	 * @return the analyzeDate
	 */
	public Date getAnalyzeDate() {
		return analyzeDate;
	}

	/**
	 * @return the uninstallOrRepairReason
	 */
	public String getUninstallOrRepairReason() {
		return uninstallOrRepairReason;
	}

	/**
	 * @param maintainUser the maintainUser to set
	 */
	public void setMaintainUser(String maintainUser) {
		this.maintainUser = maintainUser;
	}

	/**
	 * @param analyzeDate the analyzeDate to set
	 */
	public void setAnalyzeDate(Date analyzeDate) {
		this.analyzeDate = analyzeDate;
	}

	/**
	 * @param uninstallOrRepairReason the uninstallOrRepairReason to set
	 */
	public void setUninstallOrRepairReason(String uninstallOrRepairReason) {
		this.uninstallOrRepairReason = uninstallOrRepairReason;
	}

	/**
	 * @return the counter
	 */
	public String getCounter() {
		return counter;
	}

	/**
	 * @return the cartonNo
	 */
	public String getCartonNo() {
		return cartonNo;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(String counter) {
		this.counter = counter;
	}

	/**
	 * @param cartonNo the cartonNo to set
	 */
	public void setCartonNo(String cartonNo) {
		this.cartonNo = cartonNo;
	}

	/**
	 * @return the installedDeptId
	 */
	public String getInstalledDeptId() {
		return installedDeptId;
	}

	/**
	 * @param installedDeptId the installedDeptId to set
	 */
	public void setInstalledDeptId(String installedDeptId) {
		this.installedDeptId = installedDeptId;
	}

	/**
	 * @return the carryComment
	 */
	public String getCarryComment() {
		return carryComment;
	}

	/**
	 * @param carryComment the carryComment to set
	 */
	public void setCarryComment(String carryComment) {
		this.carryComment = carryComment;
	}

	/**
	 * @return the disabledComment
	 */
	public String getDisabledComment() {
		return disabledComment;
	}

	/**
	 * @param disabledComment the disabledComment to set
	 */
	public void setDisabledComment(String disabledComment) {
		this.disabledComment = disabledComment;
	}

	/**
	 * @return the repairComment
	 */
	public String getRepairComment() {
		return repairComment;
	}

	/**
	 * @param repairComment the repairComment to set
	 */
	public void setRepairComment(String repairComment) {
		this.repairComment = repairComment;
	}

}
