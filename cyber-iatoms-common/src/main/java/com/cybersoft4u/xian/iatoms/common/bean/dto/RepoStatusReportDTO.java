package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 設備狀態報表DTO
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月25日
 * @MaintenancePersonnel ericdu
 */
public class RepoStatusReportDTO extends DataTransferObject<String> {

	/**
	 *  序列號
	 */
	private static final long serialVersionUID = -1883298888813159898L;
	
	/**
	 * Purpose: 枚舉類型
	 * @author barryzhang
	 * @since  JDK 1.6
	 * @date   2016/9/7
	 * @MaintenancePersonnel cybersoft
	 */
	public static enum ATTRIBUTE {
		CASE_ID("caseId"),
		MERCHANT_CODE("merchantCode"),
		MERCHANT_NAME("merchantName"),
		MERCHANT_HEADER_NAME("merchantHeaderName"),
		CASE_COMPLETION_DATE("caseCompletionDate"),
		IS_SIM_ENABLED("isSimEnabled"),
		SIM_ENABLE_DATE("simEnableDate"),
		SIM_ENABLE_NO("simEnableNo"),
		CREATE_USER("createUser"),
		CYBER_CHECKED("cyberChecked"),
		CYBER_CHECKED_DATE("cyberCheckedDate"),
		CUSTOMER_CHECKED("customerChecked"),
		CUSTOMER_CHECKED_DATE("customerCheckedDate"),
		ACTION("action"),
		FAULT_COMPONENT("faultComponent"),
		FAULT_DESCRIPTION("faultDescription"),
		DESCRIPTION("description"),
		MAINTAIN_COMPANY("mainTainCompany"),
		HARDWARE_COMPANY("hardWareCompany"),
		ASSET_OWNER("assetOwner"),
		ASSET_USER("assetUser"),
		FACTORY_WARRANTY("factoryWarranty"),
		CUSTOMER_WARRANTY_DATE("customerWarrantyDate"),
		IS_CUP("isCup"),
		MODEL("model"),
		YYYY_MM("yyyyMM"),
		COMPANY_ID("companyId"),
		MA_TYPE_CODE("maTypeCode"),
		COMM_MODE_ID("commModeId"),
		SHORT_NAME("shortName"),
		COMM_MODE_NAME("commModeName"),
		ASSET_TYPE_ID("assetTypeId"),
		COMPANY_NAME("companyName"),
		WAREHOUSE_NAME("warehouseName"),
		ASSET_TYPE_NAME("assetTypeName"),
		ASSET_CATEGORY("assetCategory"),
		IN_STORAGE("inStorage"),
		ON_WAY("onWay"),
		IN_APPLY("inApply"),
		IN_TRANS("inTrans"),
		IN_USE("inUse"),
		IN_BORROW("inBorrow"),
		IN_FAULT("inFault"),
		TO_SCRAP("toScrap"),
		IN_LOSS("inLoss"),
		TO_IDENTIFY("toIdentify"),
		IN_REPAIR("inRepair"),
		IN_REPAIRED("inRepaired"),
		IN_DESTROY("inDestroy"),
		IN_SCRAP("inScrap"),
		IN_PREP("inPrep"),
		TOTAL("total"),
		STATUS_NAME("statusName"),
		SERIAL_NUMBER("serialNumber"),
		PROPERTY_ID("propertyId"),
		ASSET_NAME("assetName"),
		ADDRESS("address"),
		KEEPER_NAME("keeperName"),
		CONTRACT_ID("contractId"),
		MA_TYPE_NAME("maTypeName"),
		IS_ENABLED("isEnabled"),
		ENABLE_DATE("enableDate"),
		TID_ENABLE_DATE("tidEnableDate"),
		IS_SPARE_PARTS("isSpareParts"),
		CARRIER("carrier"),
		CARRY_START("carryStart"),
		CARRY_END("carryEnd"),
		CARRY_COMMENT("carryComment"),
		BORROWER("borrower"),
		BORROW_START("borrowStart"),
		BORROW_END("borrowEnd"),
		BORROW_COMMENT("borrowComment"),
		BACK_DATE("backDate"),
		TICKET_ID("ticketId"),
		TICKET_COMPLETION_DATE("ticketCompletionDate"),
		TID("tid"),
		DTID("dtid"),
		MER_ADDRESS("merAddress"),
		MER_LOCATION("merLocation"),
		MER_INSTALL_POSITION_DESC("merInstallPositionDesc"),
		ASSET_IN_ID("assetInId"),
		ORDER_NO("orderNo"),
		APP_NAME("appName"),
		APP_VERSION("appVersion"),
		GET_DATE("getDate"),
		IS_CHECKED("isChecked"),
		APPROVE_DATE("approveDate"),
		GUARANTEE_DATE("guaranteeDate"),
		REPAIR_COMMENT("repairComment"),
		RETIRE_REASON_CODE("retireReasonCode"),
		RETIRE_COMMENT("retireComment"),
		UPDATE_USER_NAME("updateUserName"),
		DELETED("deleted"),
		BRAND("brand"),
		UPDATE_DATE("updateDate"),
		WAREHOUSE_ID("warehouseId"),
		IN_MAINTENANCE("inMaintenance"),
		IN_RETURNED("inReturned"),
		IN_LOST("inLost");
		
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * 查詢月份
	 */
	private String yyyyMM;
	/**
	 * 公司編號
	 */
	private String companyId;
	/**
	 * 公司簡稱
	 */
	private String shortName;
	/**
	 * 行號
	 */
	private Integer number;
	/**
	 * 建檔人員
	 */
	private String createUser;
	/**
	 * 案件編號
	 */
	private String caseId;
	
	/**
	 * 維護模式
	 */
	private String maTypeCode;
	/**
	 * 通訊模式
	 */
	private String commModeId;
	/**
	 * 通訊名稱
	 */
	private String commModeName;
	
	/**
	 * 設備類型
	 */
	private String assetTypeId;
	
	/**
	 * logonUserId當前登入者id
	 */
	private String logonUserId;
	
	/**
	 * 公司名稱
	 */
	private String companyName;
	
	/**
	 * 倉庫名稱
	 */
	private String warehouseName;
	
	/**
	 * 設備名稱
	 */
	private String assetTypeName;
	
	/**
	 * 設備類別
	 */
	private String assetCategory;
	
	/**
	 * 設備型號
	 */
	private String model;
	
	/**
	 * 原廠保固日期
	 */
	private Date factoryWarranty;
	
	/**
	 * 客戶保固日期
	 */
	private Date customerWarrantyDate;
	/**
	 * 使用人
	 */
	private String assetUser;
	
	/**
	 * 資產Owner
	 */
	private String assetOwner;
	/**
	 * 維護廠商
	 */
	private  String mainTainCompany;
	
	/**
	 * 維修廠商
	 */
	private String hardWareCompany;
	
	/**
	 * 故障現象
	 */
	private String faultDescription;
	
	/**
	 * 故障組件
	 */
	private String faultComponent;
	
	/**
	 * 說明排除方式
	 */
	private String description;
	
	/**
	 * 執行作業
	 */
	private String action;
	
	/**
	 * 實際驗收
	 */
	private String  cyberChecked;
	
	/**
	 * 實際驗收日期
	 */
	private Date  cyberCheckedDate;
	
	/**
	 * 客戶實際驗收
	 */
	private String  customerChecked;
	
	/**
	 * 客戶實際驗收日期
	 */
	private Timestamp customerCheckedDate;

	/**
	 * 完修日期
	 */
	private Timestamp  caseCompletionDate;
	
	/**
	 * 庫存
	 */
	private int inStorage;
	
	/**
	 * 領用中
	 */
	private int inApply;
	/**
	 * 在途中
	 */
	private int inTrans;
	/**
	 * 使用中
	 */
	private int inUse;
	/**
	 * 借用中
	 */
	private int inBorrow;
	/**
	 * 廠牌
	 */
	private String brand;
	
	private int inFault;
	/**
	 * 待報廢
	 */
	private int toScrap;
	private int inLoss;
	private int toIdentify;
	/**
	 * 送修中
	 */
	private int inRepair;
	/**
	 * 維修中
	 */
	private int inRepaired;
	/**
	 * 已報廢
	 */
	private int inScrap;
	private int inPrep;
	private int inDestroy;
	/**
	 * 總計
	 */
	private int total;
	/**
	 * 狀態名稱
	 */
	private String statusName;
	/**
	 * 設備編號
	 */
	private String serialNumber;
	/**
	 * 財產編號
	 */
	private String propertyId;
	/**
	 * 設備名稱
	 */
	private String assetName;
	/**
	 * 所在位置
	 */
	private String address;
	
	/**
	 * 案件編號
	 */
	private String keeperName;
	/**
	 * 合約編號
	 */
	private String contractId;
	/**
	 * 維護模式名稱
	 */
	private String maTypeName;
	/**
	 * 銀聯
	 */
	private String isCup;
	private String isEnabled;
	/**
	 * 租賃已啟用
	 */
	private String isSimEnabled;
	/**
	 * 租賃編號
	 */
	private String simEnableNo;
	/**
	 * 設備啟用日
	 */
	private Timestamp enableDate;
	/**
	 * 租賃啟用日
	 */
	private Timestamp simEnableDate;
	private Timestamp tidEnableDate;
	private String isSpareParts;
	private String carrier;
	private Timestamp carryStart;
	private Timestamp carryEnd;
	private String carryComment;
	private String borrower;
	private Timestamp borrowStart;
	private Timestamp borrowEnd;
	/**
	 * 歸還日期
	 */
	private Timestamp backDate;
	private String borrowComment;
	private String ticketId;
	private Timestamp ticketCompletionDate;
	private String tid;
	private String dtid;
	/**
	 * 特店代號
	 */
	private String merchantCode;
	/**
	 * 特店名稱
	 */
	private String merchantName;
	/**
	 * 特店表頭
	 */
	private String merchantHeaderName;
	private String merAddress;
	private String merLocation;
	private String merInstallPositionDesc;
	private String assetInId;
	private String orderNo;
	private String appName;
	private String appVersion;
	private Timestamp getDate;
	private String isChecked;
	private Timestamp approveDate;
	
	/**
	 * 入庫日期
	 */
	private Timestamp guaranteeDate;
	private String repairComment;
	private String retireReasonCode;
	private String retireComment;
	/**
	 * 異動人員名稱
	 */
	private String updateUserName;
	/**
	 * 異動時間
	 */
	private Timestamp updateDate;
	/**
	 *刪除標誌 
	 */
	private String deleted;
	/**
	 * 倉庫id
	 */
	private String warehouseId;
	/**
	 * 送修數
	 */
	private Integer inMaintenance;
	/**
	 * ireport分組時使用，將倉庫名稱和設備類別合併
	 */
	private String assetAndWarehouse;
	/**
	 * ireport分組時使用，將倉庫名稱和倉庫id合併
	 */
	private String warehouseNameAndId;
	
	/**
	 * 已拆回--Task #3242
	 */
	private int inReturned;
	/**
	 * 已遗失--Task #3242
	 */
	private int inLost;
	
	
	/**
	 * Constructor: 無參構造
	 */
	public RepoStatusReportDTO() {
		
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
	 * @return the inApply
	 */
	public Integer getInApply() {
		return inApply;
	}
	/**
	 * @param inApply the inApply to set
	 */
	public void setInApply(Integer inApply) {
		this.inApply = inApply;
	}
	/**
	 * @return the inTrans
	 */
	public Integer getInTrans() {
		return inTrans;
	}
	/**
	 * @param inTrans the inTrans to set
	 */
	public void setInTrans(Integer inTrans) {
		this.inTrans = inTrans;
	}
	/**
	 * @return the inUse
	 */
	public Integer getInUse() {
		return inUse;
	}
	/**
	 * @param inUse the inUse to set
	 */
	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}
	/**
	 * @return the inBorrow
	 */
	public Integer getInBorrow() {
		return inBorrow;
	}
	/**
	 * @param inBorrow the inBorrow to set
	 */
	public void setInBorrow(Integer inBorrow) {
		this.inBorrow = inBorrow;
	}
	/**
	 * @return the inFault
	 */
	public Integer getInFault() {
		return inFault;
	}
	/**
	 * @param inFault the inFault to set
	 */
	public void setInFault(Integer inFault) {
		this.inFault = inFault;
	}
	/**
	 * @return the toScrap
	 */
	public Integer getToScrap() {
		return toScrap;
	}
	/**
	 * @param toScrap the toScrap to set
	 */
	public void setToScrap(Integer toScrap) {
		this.toScrap = toScrap;
	}
	/**
	 * @return the inLoss
	 */
	public Integer getInLoss() {
		return inLoss;
	}
	/**
	 * @param inLoss the inLoss to set
	 */
	public void setInLoss(Integer inLoss) {
		this.inLoss = inLoss;
	}
	/**
	 * @return the toIdentify
	 */
	public Integer getToIdentify() {
		return toIdentify;
	}
	/**
	 * @param toIdentify the toIdentify to set
	 */
	public void setToIdentify(Integer toIdentify) {
		this.toIdentify = toIdentify;
	}
	/**
	 * @return the inRepair
	 */
	public Integer getInRepair() {
		return inRepair;
	}
	/**
	 * @param inRepair the inRepair to set
	 */
	public void setInRepair(Integer inRepair) {
		this.inRepair = inRepair;
	}
	/**
	 * @return the inScrap
	 */
	public Integer getInScrap() {
		return inScrap;
	}
	/**
	 * @param inScrap the inScrap to set
	 */
	public void setInScrap(Integer inScrap) {
		this.inScrap = inScrap;
	}

	/**
	 * @return the inStorage
	 */
	public Integer getInStorage() {
		return inStorage;
	}

	/**
	 * @param inStorage the inStorage to set
	 */
	public void setInStorage(Integer inStorage) {
		this.inStorage = inStorage;
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
	 * @return the maTypeCode
	 */
	public String getMaTypeCode() {
		return maTypeCode;
	}

	/**
	 * @param maTypeCode the maTypeCode to set
	 */
	public void setMaTypeCode(String maTypeCode) {
		this.maTypeCode = maTypeCode;
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
	 * @return the yyyyMM
	 */
	public String getYyyyMM() {
		return yyyyMM;
	}

	/**
	 * @param yyyyMM the yyyyMM to set
	 */
	public void setYyyyMM(String yyyyMM) {
		this.yyyyMM = yyyyMM;
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
	 * @return the total
	 */
	public Integer getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Integer total) {
		this.total = total;
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
	 * @return the merAddress
	 */
	public String getMerAddress() {
		return merAddress;
	}

	/**
	 * @param merAddress the merAddress to set
	 */
	public void setMerAddress(String merAddress) {
		this.merAddress = merAddress;
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
	 * @return the inPrep
	 */
	public int getInPrep() {
		return inPrep;
	}
	/**
	 * @param inPrep the inPrep to set
	 */
	public void setInPrep(int inPrep) {
		this.inPrep = inPrep;
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
	 * @return the mainTainCompany
	 */
	public String getMainTainCompany() {
		return mainTainCompany;
	}

	/**
	 * @param mainTainCompany the mainTainCompany to set
	 */
	public void setMainTainCompany(String mainTainCompany) {
		this.mainTainCompany = mainTainCompany;
	}

	/**
	 * @return the hardWareCompany
	 */
	public String getHardWareCompany() {
		return hardWareCompany;
	}

	/**
	 * @param hardWareCompany the hardWareCompany to set
	 */
	public void setHardWareCompany(String hardWareCompany) {
		this.hardWareCompany = hardWareCompany;
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
	 * @return the cyberCheckedDate
	 */
	public Date getCyberCheckedDate() {
		return cyberCheckedDate;
	}
	/**
	 * @param cyberCheckedDate the cyberCheckedDate to set
	 */
	public void setCyberCheckedDate(Date cyberCheckedDate) {
		this.cyberCheckedDate = cyberCheckedDate;
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
	 * @return the isSimEnabled
	 */
	public String getIsSimEnabled() {
		return isSimEnabled;
	}
	/**
	 * @param isSimEnabled the isSimEnabled to set
	 */
	public void setIsSimEnabled(String isSimEnabled) {
		this.isSimEnabled = isSimEnabled;
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
	 * @return the inDestroy
	 */
	public int getInDestroy() {
		return inDestroy;
	}
	/**
	 * @param inDestroy the inDestroy to set
	 */
	public void setInDestroy(int inDestroy) {
		this.inDestroy = inDestroy;
	}
	/**
	 * @return the factoryWarranty
	 */
	public Date getFactoryWarranty() {
		return factoryWarranty;
	}
	/**
	 * @param factoryWarranty the factoryWarranty to set
	 */
	public void setFactoryWarranty(Date factoryWarranty) {
		this.factoryWarranty = factoryWarranty;
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
	 * @return the assetAndWarehouse
	 */
	public String getAssetAndWarehouse() {
		return assetAndWarehouse;
	}
	/**
	 * @param assetAndWarehouse the assetAndWarehouse to set
	 */
	public void setAssetAndWarehouse(String assetAndWarehouse) {
		this.assetAndWarehouse = assetAndWarehouse;
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
	 * @return the logonUserId
	 */
	public String getLogonUserId() {
		return logonUserId;
	}
	/**
	 * @param logonUserId the logonUserId to set
	 */
	public void setLogonUserId(String logonUserId) {
		this.logonUserId = logonUserId;
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
	 * @return the warehouseNameAndId
	 */
	public String getWarehouseNameAndId() {
		return warehouseNameAndId;
	}

	/**
	 * @param warehouseNameAndId the warehouseNameAndId to set
	 */
	public void setWarehouseNameAndId(String warehouseNameAndId) {
		this.warehouseNameAndId = warehouseNameAndId;
	}

	/**
	 * @return the inMaintenance
	 */
	public Integer getInMaintenance() {
		return inMaintenance;
	}

	/**
	 * @param inMaintenance the inMaintenance to set
	 */
	public void setInMaintenance(Integer inMaintenance) {
		this.inMaintenance = inMaintenance;
	}
	/**
	 * @return the inRepaired
	 */
	public int getInRepaired() {
		return inRepaired;
	}

	/**
	 * @param inRepaired the inRepaired to set
	 */
	public void setInRepaired(int inRepaired) {
		this.inRepaired = inRepaired;
	}

	/**
	 * @return the inReturned
	 */
	public int getInReturned() {
		return inReturned;
	}

	/**
	 * @param inReturned the inReturned to set
	 */
	public void setInReturned(int inReturned) {
		this.inReturned = inReturned;
	}

	/**
	 * @return the inLost
	 */
	public int getInLost() {
		return inLost;
	}

	/**
	 * @param inLost the inLost to set
	 */
	public void setInLost(int inLost) {
		this.inLost = inLost;
	}
	
}
