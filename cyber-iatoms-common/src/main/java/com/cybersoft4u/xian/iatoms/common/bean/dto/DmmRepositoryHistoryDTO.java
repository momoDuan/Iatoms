package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;

public class DmmRepositoryHistoryDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -8546110288543141032L;
	/**
	 * 舊DTO中內容，暫未刪除
	 */
	private String keeperName;
	private String purchaseContractId;
	private String maintenanceContractId;
	private String isSpareParts;
	private String isCvs;
	private Timestamp carryStart;
	private Timestamp carryEnd;
	private String ticketId;
	private Timestamp ticketCompletionDate;
	private Timestamp ticketCloseDate;
	private String mid;
	private String merRegisteredName;
	private String merAnnouncedName;
	private String installAdress;
	private String merInstallPositionDesc;
	private String merLocation;
	private String orderNo;
	private Timestamp tidEnableDate;
	private String vendorId;
	private Timestamp getDate;
	private Timestamp approveDate;
	private Timestamp guaranteeDate;
	private String rootTicketId;
	private String wareHouseName;
	private String installType;
	private String address;
	private String statusName;
	private String isLoaned;
	private String maTypeName;
	private Timestamp borrowStart;
	private Timestamp borrowEnd;
	private String borrowComment;
	private String companyId;
	private String hardwareCompany;
	private String cyberChecked;
	private Timestamp cyberCheckedDate;
	private String customerChecked;
	private Timestamp customerCheckedDate;
	private String assetCategory;
	private Timestamp assetInDate;
	private Timestamp doneDate;
	private String assetCategoryName;
	private String supportedFunction;
	private String maTypeValue;
	private String brand;
	private String ticketType;
	private String carry;
	
	/**
	 * 區域名稱
	 */
	private String areaName;
	/**
	 * 租賃啟用
	 */
	private String isSimEnable;
	/**
	 * 租賃編號
	 */
	private String simEnableNo;
	/**
	 * 領用日期
	 */
	private Timestamp carryDate;

	private String caseId;
	private Timestamp caseCompletionDate;

	private String applicationId;
	/**
	 * MID
	 */
	private String merchantId;
	/**
	 * 特店表頭id
	 */
	private String merchantHeaderId;
	/**
	 * 驗收日
	 */
	private Date checkedDate;
	/**
	 * 資產代碼
	 */
	private String assetId;
	/**
	 * 設備代號
	 */
	private String serialNumber;
	/**
	 * 財產編號
	 */
	private String propertyId;
	/**
	 * 庫存動作
	 */
	private String action;
	/**
	 * 狀態
	 */
	private String status;
	/**
	 * 設備代碼
	 */
	private String assetTypeId;
	/**
	 * 保管人
	 */
	//private String keeperName;
	/**
	 * 倉庫ID
	 */
	private String warehouseId;
	//private String purchaseContractId;
	//private String maintenanceContractId;
	private String maType;
	//private String isSpareParts;
	private String isCup = "N";
	/**
	 * char是否為銀聯
	 */
	private Character isCupChar;
	/**
	 * char是否啟用
	 */
	private Character isEnableChar;
	//private String isCvs;
	private String carrier;
	//private Timestamp carryStart;
	//private Timestamp carryEnd;
	private String carryComment;
	//private String ticketId;
	//private Timestamp ticketCompletionDate;
	//private Timestamp ticketCloseDate;
	private String tid;
	//private String mid;
	//private String merRegisteredName;
	//private String merAnnouncedName;
	private String installedAdress;
	private String area;
	//private String merInstallPositionDesc;
	//private String merLocation;
	private String assetInId;
	private String assetTransId;
	//private String orderNo;
	private String isEnabled = "N";
	private Timestamp enableDate;
	/*private Timestamp tidEnableDate;
	private int vendorId;
	private String appName;
	private String appVersion;
	private Date getDate;
	private String isChecked;
	private Timestamp approvedDate;
	private Timestamp guaranteeDate;*/
	private String repairVendor;
	private String repairComment;
	private String retireReasonCode;
	private String retireComment;
	private String createUser;
	private String createUserName;
	private Timestamp createDate;
	private String updateUser;
	private String updateUserName;
	private Timestamp updateDate;
	private Timestamp simEnableDate;
	//private String rootTicketId;
	private String assetOwner;
	private String assetUser;
	private String dtid;
	/*private String companyId;
	private String itemName;
	private String itemValue;
	private String name;*/
	private String contractId;
	//private String address;
	private String borrower;
	private Timestamp borrowerStart;
	private Timestamp borrowerEnd;
	private String borrowerComment;
	private Timestamp backDate;
	private String maintainCompany;
	//private String hardwareCompany;
	private String description;
	private String faultComponent;
	private String faultDescription;
	/*private String cyberChecked;
	private Timestamp cyberCheckedDate;
	private String customerChecked;
	private Timestamp customerCheckedDate;
	private String shortName;
	private String assetCategory;
	private Timestamp assetInDate;
	private Timestamp doneDate;
	private String assetCategoryName;
	private String supportedFunction;
	private String maTypeValue;
	private Timestamp customerGuaranteeDate;*/
	private String borrowerEmail;
	private String borrowerMgrEmail;
	/*private String brand;
	private String model;
	private String assetOwnerName;
	private String assetUserName;*/
	private Date customerWarrantyDate;
	private Date factoryWarrantyDate;
	private Timestamp assetInTime;
	/*private String installType;
	private String ticketType;
	private String borrow;
	private Date myDate;
	private String borrowSerialNumber;*/
	private String itemName;
	private String appName;
	private String appVersion; 
	private String shortName; 
	private Date customerApproveDate;
	private String assetUserName;
	private String assetOwnerName; 
	private String model;
	private String name;
	private String myDate;
	private String merName;
	private String headerName;
	private String itemValue;
	private String merchantCode;
	private String isCustomerChecked = "N";
	private String isChecked = "N";
	private String historyId;
	private String repairVendorId;
	private String faultComponentId;
	private String faultDescriptionId;
	private String contractCode;
	private String merInstallAddress;
	/**
	 * 維護部門
	 */
	private String departmentId;
	/**
	 * 拆機/報修原因
	 */
	private String problemReason;
	
	/**
	 * 簽收日期
	 */
	private Date analyzeDate;
	
	/**
	 * 維護人員
	 */
	private String vendorStaff;
	
	/**
	 * 設備廠牌
	 */
	private String assetBrand;
	/**
	 * 裝機地址--縣市
	 */
	private String installedAdressLocation;
	/**
	 * 原廠
	 */
	private String repairCompany;
	/**
	 * 廠牌
	 */
	private String assetModel;
	/**
	 * cyber驗收日期
	 */
	private Timestamp cyberApprovedDate;
	/**
	 * 維護人員
	 */
	private String maintainUser;
	/**
	 * 報修/拆機原因
	 */
	private String uninstallOrRepairReason;
	/**
	 * 櫃位
	 */
	private String counter;
	/**
	 * 原櫃位
	 */
	private String oldCounter;
	/**
	 * 箱號
	 */
	private String cartonNo;
	/**
	 * 原箱號
	 */
	private String oldCartonNo;
	private String oldMerchantCode;
	private String oldMerchantHeader;
	private String oldMerchantName;
	private String oldMerchantAddress;
	/**
	 * 公司code
	 */
	private String companyCode;
	/**
	 * 執行作業value
	 */
	private String actionValue;

	/**
	 * 是否查詢案件編號存在flag
	 */
	private String queryCaseFlag;
	/**
	 * 報廢說明
	 */
	private String disabledComment;

	/**
	 * 
	 * Purpose: 枚舉類
	 * @author ericdu
	 * @since  JDK 1.7
	 * @date   2016年7月19日
	 * @MaintenancePersonnel ericdu
	 */
	public static enum ATTRIBUTE {
		HISTORY_ID("historyId"),
		IS_SIM_ENABLE("isSimEnable"),
		SIM_ENABLE_NO("simEnableNo"),
		CARRY_DATE("carryDate"),
		CASE_ID("caseId"),
		CASE_COMPLETION_DATE("caseCompletionDate"),
		APPLICATION_ID("applicationId"),
		MERCHANT_ID("merchantId"),
		CHECKED_DATE("checkedDate"),
		ASSET_ID("assetId"),
		SERIAL_NUMBER("serialNumber"),
		PROPERTY_ID("propertyId"),
		ACTION("action"),
		STATUS("status"),
		ASSET_TYPE_ID("assetTypeId"),
		WAREHOUSE_ID("warehouseId"),
		MA_TYPE("maType"),
		IS_CUP("isCup"),
		IS_CUP_CHAR("isCupChar"),
		IS_ENABLE_CHAR("isEnableChar"),
		CARRIER("carrier"),
		CARRY_COMMENT("carryComment"),
		TID("tid"),
		ASSET_IN_ID("assetInId"),
		ASSET_TRANS_ID("assetTransId"),
		IS_ENABLED("isEnabled"),
		ENABLE_DATE("enableDate"),
		REPAIR_VENDOR("repairVendor"),
		REPAIR_COMMENT("repairComment"),
		RETIRE_REASON_CODE("retireReasonCode"),
		RETIRE_COMMENT("retireComment"),
		CREATE_USER("createUser"),
		CREATE_USER_NAME("createUserName"),
		CREATE_DATE("createDate"),
		UPDATE_USER("updateUser"),
		UPDATE_USER_NAME("updateUserName"),
		UPDATE_DATE("updateDate"),
		SIM_ENABLE_DATE("simEnableDate"),
		ASSET_OWNER("assetOwner"),
		ASSET_USER("assetUser"),
		DTID("dtid"),
		CONTRACT_ID("contractId"),
		BORROWER("borrower"),
		BORROWER_START("borrowerStart"),
		BORROWER_END("borrowerEnd"),
		BORROWER_COMMENT("borrowerComment"),
		BACK_DATE("backDate"),
		MAINTAIN_COMPANY("maintainCompany"),
		DESCRIPTION("description"),
		FAULT_COMPONENT("faultComponent"),
		FAULT_DESCRIPTION("faultDescription"),
		BORROWER_EMAIL("borrowerEmail"),
		BORROWER_MGR_EMAIL("borrowerMgrEmail"),
		CUSTOMER_WARRANTY_DATE("customerWarrantyDate"),
		FACTORY_WARRANTY_DATE("factoryWarrantyDate"),
		ASSET_IN_TIME("assetInTime"),
		REPAIR_NENDOR_ID("repairVendorId"),
		FAULT_COMPONENT_ID("faultComponentId"),
		FAULT_DESCRIPTION_ID("faultDescriptionId"),
		ITEM_NAME("itemName"),
		APP_NAME("appName"),
		APP_VERSION("appVersion"), 
		SHORT_NAME("shortName"), 
		CUSTOMER_APPROVE_DATE("customerApproveDate"),
		ASSET_USER_NAME("assetUserName"),
		ASSET_OWNER_NAME("assetOwnerName"),
		NAME("name"),
		MODEL("model"),
		MER_NAME("merName"),
		HEADER_NAME("headerName"),
		ITEM_VALUE("itemValue"),
		MERCHANT_CODE("merchantCode"),
		IS_CUSTOMER_CHECKED("isCustomerChecked"),
		IS_CHECKED("isChecked"),
		CONTRACT_CODE("contractCode"),
		INSTALLED_ADRESS("installedAdress"),
		ANALYZE_DATE("analyzeDate"),
		VENDOR_STAFF("vendorStaff"),
		DEPARTMENT_ID("departmentId"),
		PROBLEM_REASON("problemReason"),
		AREA("area"),
		MER_INSTALL_ADDRESS("merInstallAddress"),
		//暫未刪除部分
		PURCHASE_CONTRACT_ID("purchaseContractId"),
		MAINTENANCE_CONTRACT_ID("maintenanceContractId"),
		MA_TYPE_NAME("maTypeName"),
		IS_SPARE_PARTS("isSpareParts"),
		IS_CVS("isCvs"),
		CARRY_START("carryStart"),
		CARRY_END("carryEnd"),
		TICKET_ID("ticketId"),
		TICKET_COMPLETION_DATE("ticketCompletionDate"),
		TICKET_CLOSE_DATE("ticketCloseDate"),
		MID("mid"),
		MER_REGISTERED_NAME("merRegisteredName"),
		MER_ANNOUNCED_NAME("merAnnouncedName"),
		MER_INSTALL_POSITION_DESC("merInstallPositionDesc"),
		MER_LOCATION("merLocation"),
		ORDER_NO("orderNo"),
		TID_ENABLE_DATE("tidEnableDate"),
		VENDOR_ID("vendorId"),
		GET_DATE("getDate"),
		APPROVE_DATE("approveDate"),
		GUARANTEE_DATE("guaranteeDate"),
		ROOT_TICKET_ID("rootTicketId"),
		ADDRESS("address"),
		WAREHOUSE_NAME("wareHouseName"),
		INSTALL_TYPE("installType"),
		STATUS_NAME("statusName"),
		COMPANY_ID("companyId"),
		HARDWARE_COMPANY("hardwareCompany"),
		CYBER_CHECKED("cyberChecked"),
		CYBER_CHECKED_DATE("cyberCheckedDate"),
		CUSTOMER_CHECKED("customerChecked"),
		CUSTOMER_CHECKED_DATE("customerCheckedDate"),
		ASSET_CATEGORY("assetCategory"),
		ASSET_IN_DATE("assetInDate"),
		DONE_DATE("doneDate"),
		ASSET_CATEGORY_NAME("assetCategoryName"),
		SUPPORTED_FUNCTION("supportedFunction"),
		MA_TYPE_VALUE("maTypeValue"),
		BRAND("brand"),
		IS_LOANED("isLoaned"),
		BORROW_START("borrowStart"),
		BORROW_END("borrowEnd"),
		BORROW_COMMENT("borrowComment"),
		TICKET_TYPE("ticketType"),
		CARRY("carry"),
		ASSET_BRAND("assetBrand"),
		AREA_NAME("areaName"),
		INSTALLED_ADRESS_LOCATION("installedAdressLocation"),
		COUNTER("counter"),
		CARTON_NO("cartonNo"),
		OLD_MERCHANT_CODE("oldMerchantCode"),
		OLD_MERCHANT_HEADER("oldMerchantHeader"),
		OLD_MERCHANT_NAME("oldMerchantName"),
		OLD_MERCHANT_ADDRESS("oldMerchantAddress"),
		COMPANY_CODE("companyCode"),
		ACTION_VALUE("actionValue"),
		QUERY_CASE_FLAG("queryCaseFlag"),
		; 

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	}

	/**
	 * Constructor: 無參構造
	 */
	public DmmRepositoryHistoryDTO() {
		super();
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
	 * @return the purchaseContractId
	 */
	public String getPurchaseContractId() {
		return purchaseContractId;
	}

	/**
	 * @param purchaseContractId the purchaseContractId to set
	 */
	public void setPurchaseContractId(String purchaseContractId) {
		this.purchaseContractId = purchaseContractId;
	}

	/**
	 * @return the maintenanceContractId
	 */
	public String getMaintenanceContractId() {
		return maintenanceContractId;
	}

	/**
	 * @param maintenanceContractId the maintenanceContractId to set
	 */
	public void setMaintenanceContractId(String maintenanceContractId) {
		this.maintenanceContractId = maintenanceContractId;
	}

	/**
	 * @return the isSpareParts
	 */
	public String getIsSpareParts() {
		return isSpareParts;
	}

	/**
	 * @param isSpareParts the isSpareParts to set
	 */
	public void setIsSpareParts(String isSpareParts) {
		this.isSpareParts = isSpareParts;
	}

	/**
	 * @return the isCvs
	 */
	public String getIsCvs() {
		return isCvs;
	}

	/**
	 * @param isCvs the isCvs to set
	 */
	public void setIsCvs(String isCvs) {
		this.isCvs = isCvs;
	}

	/**
	 * @return the carryStart
	 */
	public Timestamp getCarryStart() {
		return carryStart;
	}

	/**
	 * @param carryStart the carryStart to set
	 */
	public void setCarryStart(Timestamp carryStart) {
		this.carryStart = carryStart;
	}

	/**
	 * @return the carryEnd
	 */
	public Timestamp getCarryEnd() {
		return carryEnd;
	}

	/**
	 * @param carryEnd the carryEnd to set
	 */
	public void setCarryEnd(Timestamp carryEnd) {
		this.carryEnd = carryEnd;
	}

	/**
	 * @return the ticketId
	 */
	public String getTicketId() {
		return ticketId;
	}

	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	/**
	 * @return the ticketCompletionDate
	 */
	public Timestamp getTicketCompletionDate() {
		return ticketCompletionDate;
	}

	/**
	 * @param ticketCompletionDate the ticketCompletionDate to set
	 */
	public void setTicketCompletionDate(Timestamp ticketCompletionDate) {
		this.ticketCompletionDate = ticketCompletionDate;
	}

	/**
	 * @return the ticketCloseDate
	 */
	public Timestamp getTicketCloseDate() {
		return ticketCloseDate;
	}

	/**
	 * @param ticketCloseDate the ticketCloseDate to set
	 */
	public void setTicketCloseDate(Timestamp ticketCloseDate) {
		this.ticketCloseDate = ticketCloseDate;
	}

	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}

	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}

	/**
	 * @return the merRegisteredName
	 */
	public String getMerRegisteredName() {
		return merRegisteredName;
	}

	/**
	 * @param merRegisteredName the merRegisteredName to set
	 */
	public void setMerRegisteredName(String merRegisteredName) {
		this.merRegisteredName = merRegisteredName;
	}

	/**
	 * @return the merAnnouncedName
	 */
	public String getMerAnnouncedName() {
		return merAnnouncedName;
	}

	/**
	 * @param merAnnouncedName the merAnnouncedName to set
	 */
	public void setMerAnnouncedName(String merAnnouncedName) {
		this.merAnnouncedName = merAnnouncedName;
	}

	/**
	 * @return the installAdress
	 */
	public String getInstallAdress() {
		return installAdress;
	}

	/**
	 * @param installAdress the installAdress to set
	 */
	public void setInstallAdress(String installAdress) {
		this.installAdress = installAdress;
	}

	/**
	 * @return the merInstallPositionDesc
	 */
	public String getMerInstallPositionDesc() {
		return merInstallPositionDesc;
	}

	/**
	 * @param merInstallPositionDesc the merInstallPositionDesc to set
	 */
	public void setMerInstallPositionDesc(String merInstallPositionDesc) {
		this.merInstallPositionDesc = merInstallPositionDesc;
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
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the tidEnableDate
	 */
	public Timestamp getTidEnableDate() {
		return tidEnableDate;
	}

	/**
	 * @param tidEnableDate the tidEnableDate to set
	 */
	public void setTidEnableDate(Timestamp tidEnableDate) {
		this.tidEnableDate = tidEnableDate;
	}

	/**
	 * @return the vendorId
	 */
	public String getVendorId() {
		return vendorId;
	}

	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
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
	 * @return the rootTicketId
	 */
	public String getRootTicketId() {
		return rootTicketId;
	}

	/**
	 * @param rootTicketId the rootTicketId to set
	 */
	public void setRootTicketId(String rootTicketId) {
		this.rootTicketId = rootTicketId;
	}

	/**
	 * @return the wareHouseName
	 */
	public String getWareHouseName() {
		return wareHouseName;
	}

	/**
	 * @param wareHouseName the wareHouseName to set
	 */
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	/**
	 * @return the installType
	 */
	public String getInstallType() {
		return installType;
	}

	/**
	 * @param installType the installType to set
	 */
	public void setInstallType(String installType) {
		this.installType = installType;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the statusName
	 */
	public String getStatusName() {
		return statusName;
	}

	/**
	 * @param statusName the statusName to set
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * @return the isLoaned
	 */
	public String getIsLoaned() {
		return isLoaned;
	}

	/**
	 * @param isLoaned the isLoaned to set
	 */
	public void setIsLoaned(String isLoaned) {
		this.isLoaned = isLoaned;
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
	 * @return the borrowStart
	 */
	public Timestamp getBorrowStart() {
		return borrowStart;
	}

	/**
	 * @param borrowStart the borrowStart to set
	 */
	public void setBorrowStart(Timestamp borrowStart) {
		this.borrowStart = borrowStart;
	}

	/**
	 * @return the borrowEnd
	 */
	public Timestamp getBorrowEnd() {
		return borrowEnd;
	}

	/**
	 * @param borrowEnd the borrowEnd to set
	 */
	public void setBorrowEnd(Timestamp borrowEnd) {
		this.borrowEnd = borrowEnd;
	}

	/**
	 * @return the borrowComment
	 */
	public String getBorrowComment() {
		return borrowComment;
	}

	/**
	 * @param borrowComment the borrowComment to set
	 */
	public void setBorrowComment(String borrowComment) {
		this.borrowComment = borrowComment;
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
	 * @return the hardwareCompany
	 */
	public String getHardwareCompany() {
		return hardwareCompany;
	}

	/**
	 * @param hardwareCompany the hardwareCompany to set
	 */
	public void setHardwareCompany(String hardwareCompany) {
		this.hardwareCompany = hardwareCompany;
	}

	/**
	 * @return the cyberChecked
	 */
	public String getCyberChecked() {
		return cyberChecked;
	}

	/**
	 * @param cyberChecked the cyberChecked to set
	 */
	public void setCyberChecked(String cyberChecked) {
		this.cyberChecked = cyberChecked;
	}

	/**
	 * @return the cyberCheckedDate
	 */
	public Timestamp getCyberCheckedDate() {
		return cyberCheckedDate;
	}

	/**
	 * @param cyberCheckedDate the cyberCheckedDate to set
	 */
	public void setCyberCheckedDate(Timestamp cyberCheckedDate) {
		this.cyberCheckedDate = cyberCheckedDate;
	}

	/**
	 * @return the customerChecked
	 */
	public String getCustomerChecked() {
		return customerChecked;
	}

	/**
	 * @param customerChecked the customerChecked to set
	 */
	public void setCustomerChecked(String customerChecked) {
		this.customerChecked = customerChecked;
	}

	/**
	 * @return the customerCheckedDate
	 */
	public Timestamp getCustomerCheckedDate() {
		return customerCheckedDate;
	}

	/**
	 * @param customerCheckedDate the customerCheckedDate to set
	 */
	public void setCustomerCheckedDate(Timestamp customerCheckedDate) {
		this.customerCheckedDate = customerCheckedDate;
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
	 * @return the assetInDate
	 */
	public Timestamp getAssetInDate() {
		return assetInDate;
	}

	/**
	 * @param assetInDate the assetInDate to set
	 */
	public void setAssetInDate(Timestamp assetInDate) {
		this.assetInDate = assetInDate;
	}

	/**
	 * @return the doneDate
	 */
	public Timestamp getDoneDate() {
		return doneDate;
	}

	/**
	 * @param doneDate the doneDate to set
	 */
	public void setDoneDate(Timestamp doneDate) {
		this.doneDate = doneDate;
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
	 * @return the supportedFunction
	 */
	public String getSupportedFunction() {
		return supportedFunction;
	}

	/**
	 * @param supportedFunction the supportedFunction to set
	 */
	public void setSupportedFunction(String supportedFunction) {
		this.supportedFunction = supportedFunction;
	}

	/**
	 * @return the maTypeValue
	 */
	public String getMaTypeValue() {
		return maTypeValue;
	}

	/**
	 * @param maTypeValue the maTypeValue to set
	 */
	public void setMaTypeValue(String maTypeValue) {
		this.maTypeValue = maTypeValue;
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
	 * @return the ticketType
	 */
	public String getTicketType() {
		return ticketType;
	}

	/**
	 * @param ticketType the ticketType to set
	 */
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * @return the isSimEnable
	 */
	public String getIsSimEnable() {
		return isSimEnable;
	}

	/**
	 * @param isSimEnable the isSimEnable to set
	 */
	public void setIsSimEnable(String isSimEnable) {
		this.isSimEnable = isSimEnable;
	}

	/**
	 * @return the simEnableNo
	 */
	public String getSimEnableNo() {
		return simEnableNo;
	}

	/**
	 * @param simEnableNo the simEnableNo to set
	 */
	public void setSimEnableNo(String simEnableNo) {
		this.simEnableNo = simEnableNo;
	}

	/**
	 * @return the carryDate
	 */
	public Timestamp getCarryDate() {
		return carryDate;
	}

	/**
	 * @param carryDate the carryDate to set
	 */
	public void setCarryDate(Timestamp carryDate) {
		this.carryDate = carryDate;
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
	 * @return the caseCompletionDate
	 */
	public Timestamp getCaseCompletionDate() {
		return caseCompletionDate;
	}

	/**
	 * @param caseCompletionDate the caseCompletionDate to set
	 */
	public void setCaseCompletionDate(Timestamp caseCompletionDate) {
		this.caseCompletionDate = caseCompletionDate;
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
	 * @return the checkedDate
	 */
	public Date getCheckedDate() {
		return checkedDate;
	}

	/**
	 * @param checkedDate the checkedDate to set
	 */
	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}

	/**
	 * @return the assetId
	 */
	public String getAssetId() {
		return assetId;
	}

	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(String assetId) {
		this.assetId = assetId;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the isCup
	 */
	public String getIsCup() {
		return isCup;
	}

	/**
	 * @param isCup the isCup to set
	 */
	public void setIsCup(String isCup) {
		this.isCup = isCup;
	}

	/**
	 * @return the isCupChar
	 */
	public Character getIsCupChar() {
		return isCupChar;
	}

	/**
	 * @param isCupChar the isCupChar to set
	 */
	public void setIsCupChar(Character isCupChar) {
		this.isCupChar = isCupChar;
	}

	/**
	 * @return the isEnableChar
	 */
	public Character getIsEnableChar() {
		return isEnableChar;
	}

	/**
	 * @param isEnableChar the isEnableChar to set
	 */
	public void setIsEnableChar(Character isEnableChar) {
		this.isEnableChar = isEnableChar;
	}

	/**
	 * @return the carrier
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * @param carrier the carrier to set
	 */
	public void setCarrier(String carrier) {
		this.carrier = carrier;
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
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
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
	 * @return the assetTransId
	 */
	public String getAssetTransId() {
		return assetTransId;
	}

	/**
	 * @param assetTransId the assetTransId to set
	 */
	public void setAssetTransId(String assetTransId) {
		this.assetTransId = assetTransId;
	}

	/**
	 * @return the isEnabled
	 */
	public String getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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
	 * @return the repairVendor
	 */
	public String getRepairVendor() {
		return repairVendor;
	}

	/**
	 * @param repairVendor the repairVendor to set
	 */
	public void setRepairVendor(String repairVendor) {
		this.repairVendor = repairVendor;
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

	/**
	 * @return the retireReasonCode
	 */
	public String getRetireReasonCode() {
		return retireReasonCode;
	}

	/**
	 * @param retireReasonCode the retireReasonCode to set
	 */
	public void setRetireReasonCode(String retireReasonCode) {
		this.retireReasonCode = retireReasonCode;
	}

	/**
	 * @return the retireComment
	 */
	public String getRetireComment() {
		return retireComment;
	}

	/**
	 * @param retireComment the retireComment to set
	 */
	public void setRetireComment(String retireComment) {
		this.retireComment = retireComment;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the createDate
	 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateUser
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateUserName
	 */
	public String getUpdateUserName() {
		return updateUserName;
	}

	/**
	 * @param updateUserName the updateUserName to set
	 */
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
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
	 * @return the assetUser
	 */
	public String getAssetUser() {
		return assetUser;
	}

	/**
	 * @param assetUser the assetUser to set
	 */
	public void setAssetUser(String assetUser) {
		this.assetUser = assetUser;
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
	 * @return the borrower
	 */
	public String getBorrower() {
		return borrower;
	}

	/**
	 * @param borrower the borrower to set
	 */
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	/**
	 * @return the borrowerStart
	 */
	public Timestamp getBorrowerStart() {
		return borrowerStart;
	}

	/**
	 * @param borrowerStart the borrowerStart to set
	 */
	public void setBorrowerStart(Timestamp borrowerStart) {
		this.borrowerStart = borrowerStart;
	}

	/**
	 * @return the borrowerEnd
	 */
	public Timestamp getBorrowerEnd() {
		return borrowerEnd;
	}

	/**
	 * @param borrowerEnd the borrowerEnd to set
	 */
	public void setBorrowerEnd(Timestamp borrowerEnd) {
		this.borrowerEnd = borrowerEnd;
	}

	/**
	 * @return the borrowerComment
	 */
	public String getBorrowerComment() {
		return borrowerComment;
	}

	/**
	 * @param borrowerComment the borrowerComment to set
	 */
	public void setBorrowerComment(String borrowerComment) {
		this.borrowerComment = borrowerComment;
	}

	/**
	 * @return the backDate
	 */
	public Timestamp getBackDate() {
		return backDate;
	}

	/**
	 * @param backDate the backDate to set
	 */
	public void setBackDate(Timestamp backDate) {
		this.backDate = backDate;
	}

	/**
	 * @return the maintainCompany
	 */
	public String getMaintainCompany() {
		return maintainCompany;
	}

	/**
	 * @param maintainCompany the maintainCompany to set
	 */
	public void setMaintainCompany(String maintainCompany) {
		this.maintainCompany = maintainCompany;
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
	 * @return the faultComponent
	 */
	public String getFaultComponent() {
		return faultComponent;
	}

	/**
	 * @param faultComponent the faultComponent to set
	 */
	public void setFaultComponent(String faultComponent) {
		this.faultComponent = faultComponent;
	}

	/**
	 * @return the faultDescription
	 */
	public String getFaultDescription() {
		return faultDescription;
	}

	/**
	 * @param faultDescription the faultDescription to set
	 */
	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}

	/**
	 * @return the borrowerEmail
	 */
	public String getBorrowerEmail() {
		return borrowerEmail;
	}

	/**
	 * @param borrowerEmail the borrowerEmail to set
	 */
	public void setBorrowerEmail(String borrowerEmail) {
		this.borrowerEmail = borrowerEmail;
	}

	/**
	 * @return the borrowerMgrEmail
	 */
	public String getBorrowerMgrEmail() {
		return borrowerMgrEmail;
	}

	/**
	 * @param borrowerMgrEmail the borrowerMgrEmail to set
	 */
	public void setBorrowerMgrEmail(String borrowerMgrEmail) {
		this.borrowerMgrEmail = borrowerMgrEmail;
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
	 * @return the assetInTime
	 */
	public Timestamp getAssetInTime() {
		return assetInTime;
	}

	/**
	 * @param assetInTime the assetInTime to set
	 */
	public void setAssetInTime(Timestamp assetInTime) {
		this.assetInTime = assetInTime;
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
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
	 * @return the assetUserName
	 */
	public String getAssetUserName() {
		return assetUserName;
	}

	/**
	 * @param assetUserName the assetUserName to set
	 */
	public void setAssetUserName(String assetUserName) {
		this.assetUserName = assetUserName;
	}

	/**
	 * @return the assetOwnerName
	 */
	public String getAssetOwnerName() {
		return assetOwnerName;
	}

	/**
	 * @param assetOwnerName the assetOwnerName to set
	 */
	public void setAssetOwnerName(String assetOwnerName) {
		this.assetOwnerName = assetOwnerName;
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
	 * @return the myDate
	 */
	public String getMyDate() {
		return myDate;
	}

	/**
	 * @param myDate the myDate to set
	 */
	public void setMyDate(String myDate) {
		this.myDate = myDate;
	}

	/**
	 * @return the merName
	 */
	public String getMerName() {
		return merName;
	}

	/**
	 * @param merName the merName to set
	 */
	public void setMerName(String merName) {
		this.merName = merName;
	}

	/**
	 * @return the headerName
	 */
	public String getHeaderName() {
		return headerName;
	}

	/**
	 * @param headerName the headerName to set
	 */
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	/**
	 * @return the itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}

	/**
	 * @param itemValue the itemValue to set
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
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
	 * @return the historyId
	 */
	public String getHistoryId() {
		return historyId;
	}

	/**
	 * @param historyId the historyId to set
	 */
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	/**
	 * @return the repairVendorId
	 */
	public String getRepairVendorId() {
		return repairVendorId;
	}

	/**
	 * @param repairVendorId the repairVendorId to set
	 */
	public void setRepairVendorId(String repairVendorId) {
		this.repairVendorId = repairVendorId;
	}

	/**
	 * @return the faultComponentId
	 */
	public String getFaultComponentId() {
		return faultComponentId;
	}

	/**
	 * @param faultComponentId the faultComponentId to set
	 */
	public void setFaultComponentId(String faultComponentId) {
		this.faultComponentId = faultComponentId;
	}

	/**
	 * @return the faultDescriptionId
	 */
	public String getFaultDescriptionId() {
		return faultDescriptionId;
	}

	/**
	 * @param faultDescriptionId the faultDescriptionId to set
	 */
	public void setFaultDescriptionId(String faultDescriptionId) {
		this.faultDescriptionId = faultDescriptionId;
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
	 * @return the problemReason
	 */
	public String getProblemReason() {
		return problemReason;
	}

	/**
	 * @param problemReason the problemReason to set
	 */
	public void setProblemReason(String problemReason) {
		this.problemReason = problemReason;
	}

	/**
	 * @return the analyzeDate
	 */
	public Date getAnalyzeDate() {
		return analyzeDate;
	}

	/**
	 * @param analyzeDate the analyzeDate to set
	 */
	public void setAnalyzeDate(Date analyzeDate) {
		this.analyzeDate = analyzeDate;
	}

	/**
	 * @return the vendorStaff
	 */
	public String getVendorStaff() {
		return vendorStaff;
	}

	/**
	 * @param vendorStaff the vendorStaff to set
	 */
	public void setVendorStaff(String vendorStaff) {
		this.vendorStaff = vendorStaff;
	}

	/**
	 * @return the carry
	 */
	public String getCarry() {
		return carry;
	}

	/**
	 * @param carry the carry to set
	 */
	public void setCarry(String carry) {
		this.carry = carry;
	}

	/**
	 * @return the assetBrand
	 */
	public String getAssetBrand() {
		return assetBrand;
	}

	/**
	 * @param assetBrand the assetBrand to set
	 */
	public void setAssetBrand(String assetBrand) {
		this.assetBrand = assetBrand;
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
	 * @return the merInstallAddress
	 */
	public String getMerInstallAddress() {
		return merInstallAddress;
	}

	/**
	 * @param merInstallAddress the merInstallAddress to set
	 */
	public void setMerInstallAddress(String merInstallAddress) {
		this.merInstallAddress = merInstallAddress;
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
	 * @return the cyberApprovedDate
	 */
	public Timestamp getCyberApprovedDate() {
		return cyberApprovedDate;
	}

	/**
	 * @param cyberApprovedDate the cyberApprovedDate to set
	 */
	public void setCyberApprovedDate(Timestamp cyberApprovedDate) {
		this.cyberApprovedDate = cyberApprovedDate;
	}

	/**
	 * @return the maintainUser
	 */
	public String getMaintainUser() {
		return maintainUser;
	}

	/**
	 * @param maintainUser the maintainUser to set
	 */
	public void setMaintainUser(String maintainUser) {
		this.maintainUser = maintainUser;
	}

	/**
	 * @return the uninstallOrRepairReason
	 */
	public String getUninstallOrRepairReason() {
		return uninstallOrRepairReason;
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
	 * @param counter the counter to set
	 */
	public void setCounter(String counter) {
		this.counter = counter;
	}

	/**
	 * @return the oldCounter
	 */
	public String getOldCounter() {
		return oldCounter;
	}

	/**
	 * @param oldCounter the oldCounter to set
	 */
	public void setOldCounter(String oldCounter) {
		this.oldCounter = oldCounter;
	}

	/**
	 * @return the cartonNo
	 */
	public String getCartonNo() {
		return cartonNo;
	}

	/**
	 * @param cartonNo the cartonNo to set
	 */
	public void setCartonNo(String cartonNo) {
		this.cartonNo = cartonNo;
	}

	/**
	 * @return the oldCartonNo
	 */
	public String getOldCartonNo() {
		return oldCartonNo;
	}

	/**
	 * @param oldCartonNo the oldCartonNo to set
	 */
	public void setOldCartonNo(String oldCartonNo) {
		this.oldCartonNo = oldCartonNo;
	}

	/**
	 * @return the oldMerchantCode
	 */
	public String getOldMerchantCode() {
		return oldMerchantCode;
	}

	/**
	 * @param oldMerchantCode the oldMerchantCode to set
	 */
	public void setOldMerchantCode(String oldMerchantCode) {
		this.oldMerchantCode = oldMerchantCode;
	}

	/**
	 * @return the oldMerchantHeader
	 */
	public String getOldMerchantHeader() {
		return oldMerchantHeader;
	}

	/**
	 * @param oldMerchantHeader the oldMerchantHeader to set
	 */
	public void setOldMerchantHeader(String oldMerchantHeader) {
		this.oldMerchantHeader = oldMerchantHeader;
	}

	/**
	 * @return the oldMerchantName
	 */
	public String getOldMerchantName() {
		return oldMerchantName;
	}

	/**
	 * @param oldMerchantName the oldMerchantName to set
	 */
	public void setOldMerchantName(String oldMerchantName) {
		this.oldMerchantName = oldMerchantName;
	}

	/**
	 * @return the oldMerchantAddress
	 */
	public String getOldMerchantAddress() {
		return oldMerchantAddress;
	}

	/**
	 * @param oldMerchantAddress the oldMerchantAddress to set
	 */
	public void setOldMerchantAddress(String oldMerchantAddress) {
		this.oldMerchantAddress = oldMerchantAddress;
	}

	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
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
	 * @return the queryCaseFlag
	 */
	public String getQueryCaseFlag() {
		return queryCaseFlag;
	}

	/**
	 * @param queryCaseFlag the queryCaseFlag to set
	 */
	public void setQueryCaseFlag(String queryCaseFlag) {
		this.queryCaseFlag = queryCaseFlag;
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
	
}
