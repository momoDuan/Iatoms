package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cafe.core.bean.dto.DataTransferObject;

import com.cybersoft4u.xian.iatoms.common.reportsetting.WorkFeeSetting;

/**
 * 
 * Purpose: 庫存主檔
 * @author amandawang
 * @since  JDK 1.7
 * @date   2016年7月19日
 * @MaintenancePersonnel amandawang
 */
public class DmmRepositoryDTO extends DataTransferObject<String> {
	
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 4689240955938721695L;
	
	/**
	 * 
	 * Purpose: 枚舉類
	 * @author ericdu
	 * @since  JDK 1.7
	 * @date   2016年7月19日
	 * @MaintenancePersonnel ericdu
	 */
	public static enum ATTRIBUTE {
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
		STATUS_NAME("statusName"),
		ASSET_TYPE_ID("assetTypeId"),
		ASSET_TYPE_NAME("assetTypeName"),
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
		REPAIR_NENDOR_ID("repairVendorId"),
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
		CONTRACT_CODE("contractCode"),
		BORROWER("borrower"),
		BORROWER_START("borrowerStart"),
		BORROWER_END("borrowerEnd"),
		BORROWER_COMMENT("borrowerComment"),
		BACK_DATE("backDate"),
		OLD_CYBER_APPROVED_DATE("oldCyberApprovedDate"),
		CYBER_APPROVED_DATE("cyberApprovedDate"),
		MAINTAIN_COMPANY("maintainCompany"),
		MAINTAIN_COMPANY_NAME("maintainCompanyName"),
		DESCRIPTION("description"),
		FAULT_COMPONENT("faultComponent"),
		FAULT_DESCRIPTION("faultDescription"),
		BORROWER_EMAIL("borrowerEmail"),
		BORROWER_NAME("borrowerName"),
		BORROWER_MGR_EMAIL("borrowerMgrEmail"),
		CUSTOMER_WARRANTY_DATE("customerWarrantyDate"),
		FACTORY_WARRANTY_DATE("factoryWarrantyDate"),
		ASSET_IN_TIME("assetInTime"),
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
		MER_INSTALL_ADDRESS("merInstallAddress"),
		ASSET_CATEGORY("assetCateGory"),
		EDC_PEOPLE("edcPeople"),
		EDC_COMMENT("edcComment"),
		INSTALLED_ADRESS("installAddress"),
		INSTALL_TYPE("installType"),
		ANALYZE_DATE("analyzeDate"),
		VENDOR_STAFF("vendorStaff"),
		PROBLEM_REASON("problemReason"),
		SOFTWARE_VERSION("softwareVersion"),
		INSTALLED_ADRESS_LOCATION("installedAdressLocation"),
		INSTALLED_ADRESS_LOCATION_NAME("installedAdressLocationName"),
		DEPARTMENT_ID("departmentId"),
		ITEM_CATEGORY_NAME("itemCategoryName"),
		WAREHOUSE_NAME("warehouseName"),
		BRAND("brand"),
		CARRY("carry"),
		AREA("area"),
		ASSET_BRAND("assetBrand"),
		REPAIR_COMPANY("repairCompany"),
		AREA_NAME("areaName"),
		BORROWERS("borrowers"),
		ASSET_MODEL("assetModel"),
		WAREHOUSE_CONFIRM("warehouseConfirm"),
		MAINTAIN_USER("maintainUser"),
		MAINTAIN_USER_NAME("maintainUserName"),
		ENAME("ename"),
		UNINSTALL_OR_REPAIRREASON("uninstallOrRepairReason"),
		COUNTER("counter"),
		CARTON_NO("cartonNo"),
		TAI_XIN_MAINTENANCE("taixinMaintenance"),
		JDW_MAINTENANCE("jdwMaintenance"),
		FEE("fee"),
		COMPANY_CODE("companyCode"),
		ROWID("rowId"),
		MERCHANT_NAME("merchantName"),
		BUSSINESS_ADDRESS("bussinessAddress"),
		MERCHANT_ADDRESS("merchantAddress"),
		CASE_STATUS("caseStatus"),
		TSAM("tSam"),
		AO_NAME("aoAame"),
		YEAR_MONTH_DAY("monthYear"),
		CUSTOMER_CODE("customerCode"),
		TAIXIN_CUSTOMER_CODE("TaiXincustomerCode"),
		MAINTENANCE_TAX("MaintenanceTax"),
		MAINTENANCE_NO_TAX("MaintenanceNoTax"),
		ACTION_VALUE("actionValue"),
		COMMMODE_ID("commModeId"),
		IN_SCRAP("inScrap"),
		IN_USE("inUse"),
		IN_ENABLE("inEnable"),
		INSTALLED_DEPT_ID("installedDeptId"),
		TOTAL("total"),
		COMM_MODE_NUM("commModeNum"),
		QUERY_CASE_FLAG("queryCaseFlag"),
		IS_BUSSINESS_ADDRESS("isBussinessAddress"),
		DISABLED_COMMENT("disabledComment"),
		KEEPER_NAME("keeperName"),
		UPDATE_TABLE_NAME("updateTableName"),
		UPDATE_TABLE("updateTable"),
		MAINTENANCE_MODE("maintenanceMode"),
		MERCHANT_HEADER_ID("merchantHeaderId"),
		INSTALL_ADRESS("installedAdress");
		/**
		 * value值
		 */
		private String value;
		/**
		 * Constructor:構造函數
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * Purpose: 根據Value獲得ATTRIBUTE的name
	 * @author amandawang
	 * @param value
	 * @return String
	 */
	public static String getATTRIBUTENameByValue (String value) {
		String name = ATTRIBUTE.ASSET_ID.name();
		for (DmmRepositoryDTO.ATTRIBUTE s : DmmRepositoryDTO.ATTRIBUTE.values()){
			if (s.getValue().equals(value)) {
				name = s.name();
				break;
			}
		}
		return name;
	}
	/**
	 * 區域名稱
	 */
	private String areaName;
	/**
	 * 區域
	 */
	private String area;
	//裝機地址--installAdress單詞不對
	private String installAddress;
	private Timestamp enableDate;
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
	/**
	 * 維護部門
	 */
	private String departmentId;
	/**
	 * 拆機/報修原因
	 */
	private String problemReason;
	/**
	 * 案件ID
	 */
	private String caseId;
	private String oldCaseId;
	/**
	 * 簽收日期
	 */
	private Date analyzeDate;
	private Date oldAnalyzeDate;
	/**
	 * 維護人員
	 */
	private String vendorStaff;
	/**
	 * 案件完成日期
	 */
	private Timestamp caseCompletionDate;
	private Timestamp oldCaseCompletionDate;
	/**
	 * 程式代碼
	 */
	private String applicationId;
	/**
	 * MID
	 */
	private String merchantId;
	/**
	 * 裝機地址
	 */
	private String installedAdress;
	private String oldInstalledAdress;
	/**
	 * 特店表頭id
	 */
	private String merchantHeaderId;
	private String oldMerchantHeaderId;
	/**
	 * 驗收日
	 */
	private Date checkedDate;
	/**
	 * 資產代碼
	 */
	private String assetId;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 財產編號
	 */
	private String propertyId;
	/**
	 * 是否支持3G、GPRS、Bluetooth(3G/WIFI)、音源孔(3G/WIFI)
	 */
	private Boolean is3G;
	/**
	 * 3g價錢
	 */
	private Integer threeGPrice;
	/**
	 * 庫存動作
	 */
	private String action;
	/**
	 * 狀態
	 */
	private String status;
	private String statusName;
	/**
	 * 設備代碼
	 */
	private String assetTypeId;
	/**
	 * 設備名稱
	 */
	private String assetTypeName;
	/**
	 * 保管人
	 */
	private String keeperName;
	/**
	 * 倉庫ID
	 */
	private String warehouseId;
	private String carryAccount;

	private String maType;
	private String isCup = "N";
	private String oldIsCup;
	/**
	 * char是否為銀聯
	 */
	private Character isCupChar;
	/**
	 * char是否啟用
	 */
	private Character isEnableChar;
	private String carrier;
	private String carryComment;
	private String tid;
	private String merInstallAddress;
	private String assetInId;
	private String assetTransId;
	private String isEnabled = "N";
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
	private String installedDeptId;
	/**
	 * 租賃啟用日
	 */
	private Timestamp simEnableDate;
	/**
	 * 原租賃啟用日
	 */
	private Date oldSimEnableDate;
	private String assetOwner;
	private String assetUser;
	private String dtid;
	private String oldDtid;
	private String contractId;
	private String contractCode;
	private String borrower;
	private Timestamp borrowerStart;
	private Timestamp borrowerEnd;
	private String borrowerComment;
	private Timestamp backDate;
	private String maintainCompany;
	private String oldMaintainCompany;
	private String maintainCompanyName;
	private String description;
	private String faultComponent;
	private String faultDescription;
	private String borrowerEmail;
	private String borrowerMgrEmail;
	private Date customerWarrantyDate;
	private Date factoryWarrantyDate;
	private Timestamp assetInTime;
	private String installType;
	private String itemName;
	private String appName;
	private String appVersion; 
	private String shortName; 
	private Date customerApproveDate;
	private String assetUserName;
	private String assetOwnerName; 
	private String model;
	private String oldModel;
	private String name;
	private String myDate;
	private String merName;
	private String headerName;
	private String itemValue;
	private String merchantCode;
	private String oldMerchantCode;
	private String merchantName;
	private String bussinessAddress;
	private String merchantAddress;
	private String isCustomerChecked = "N";
	private String isChecked = "N";
	private String repairVendorId;
	private String faultComponentId;
	private String faultDescriptionId;
	private String borrowSerialNumber;
	private String borrowerName;
	private String userId;
	
	private String oldPropertyId;
	private String oldAssetUser;
	private String oldAssetOwner;
	private String oldAssetType;
	private String oldContractCode;
	private String oldSimEnableNo;
	private Timestamp oldEnableDate;
	private Timestamp oldAssetInTime;
	private String message;
	private Date cyberApprovedDate;
	private Date oldCyberApprovedDate;
	//設備類別
	private String assetCateGory;
	//領用/借用人
	private String edcPeople;
	//領用/借用說明
	private String edcComment;
	/**
	 * 耗材分類名稱
	 */
	private String itemCategoryName;
	/**
	 * 倉庫名稱
	 */
	private String warehouseName;
	/**
	 * 軟體版本
	 */
	private String softwareVersion;
	/**
	 * 裝機地址市系
	 */
	private String installedAdressLocation;
	private String oldInstalledAdressLocation;
	/**
	 * 
	 */
	private String installedAdressLocationName;
	/**
	 * 所在位置
	 */
	private String brand;
	private String oldBrand;
	/**
	 * 領用/借用人
	 */
	private String carry;
	/**
	 * 設備廠牌
	 */
	private String assetBrand;
	private String assetModel;
	/**
	 * 倉管確認
	 */
	private String warehouseConfirm;
	private String repairCompany;
	/**
	 * 維護工程師
	 */
	private String maintainUser;
	private String oldMaintainUser;
	private String maintainUserName;
	/**
	 * 拆機/報修原因
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
	/**
	 * 台新租賃維護
	 */
	private String taixinMaintenance;
	/**
	 * 捷達威維護
	 */
	private String jdwMaintenance;
	/**
	 * 維護費
	 */
	private Integer fee;
	/**
	 * 公司code
	 */
	private String companyCode;
	/**
	 * 行號
	 */
	private String rowId;
	/**
	 * 案件狀態
	 */
	private String caseStatus;
	/**
	 * tSam
	 */
	private String tSam;
	/**
	 * 表頭的ao人員的名字
	 */
	private String aoAame;
	/**
	 * 維護費(含稅)
	 */
	private Integer maintenanceTax;
	/**
	 * 維護費(未稅)
	 */
	private Integer maintenanceNoTax;
	/**
	 * 維護費(含稅) 總計
	 */
	private Integer maintenanceTaxSum;
	/**
	 * 維護費(未稅) 總計
	 */
	private Integer maintenanceNoTaxSum;
	/**
	 * 門號月租費(含稅)
	 */
	private Integer numberFeeTax;
	/**
	 * 門號月租費(未稅)
	 */
	private Integer numberFeeNoTax;
	/**
	 * 門號月租費 (含稅)總計
	 */
	private Integer numberFeeTaxSum;
	/**
	 * 門號月租費 (未稅)總計
	 */
	private Integer numberFeeNoTaxSum;
	/**
	 * 備註 
	 */
	private String remark;
	/**
	 * 執行作業value
	 */
	private String actionValue;
	/**
	 * 通訊模式
	 */
	private String commModeId;
	/**
	 * 已報廢
	 */
	private int inScrap;
	/**
	 * 使用中
	 */
	private int inUse;
	/**
	 * 啟用中
	 */
	private int inEnable;
	/**
	 * 進貨數
	 */
	private int total;
	/**
	 * SIM卡(門號)月租費
	 */
	private Integer simCardPrice;
	/**
	 * 作业類别
	 */
	private String workCategory;
	/**
	 * 第一台單價
	 */
	private Integer firstPrice;
	/**
	 * 第一台數量
	 */
	private String firstCount;
	/**
	 * 其他台單價
	 */
	private Integer otherPrice;
	/**
	 * 其他台數量
	 */
	private String otherCount;
	/**
	 * 合計
	 */
	private Integer sum;
	/**
	 * 台數
	 */
	private Integer number;
	/**
	 * 免費TMS次數
	 */
	private String freeTmsFrequency;
	/**
	 * 剩餘免費次數
	 */
	private String surplusFreeFrequency;
	/**
	 * 子報表1list
	 */
	private List<WorkFeeSetting> workFeeSettingList;
	/**
	 * 子報表2list
	 */
	private List<WorkFeeSetting> workFeeSettingFeeList;
	/**
	 * 舊資料轉檔foms特點資料無值時，存儲aims特點代號的標誌位
	 */
	private String isNoMerId;
	
	/**
	 * 舊資料轉檔foms特點表頭資料無值時，存儲aims特點表頭的標誌位
	 */
	private String isNoMerHeaderId;
	/**
	 * 设备通訊模式数量
	 */
	private Integer commModeNum;
	/**
	 * 是否查詢案件編號存在flag
	 */
	private String queryCaseFlag;
	/**
	 * 
	 */
	private String isBussinessAddress;
	/**
	 * 
	 */
	private String ename;
	/**
	 * 報廢說明
	 */
	private String disabledComment;	
	/**
	 * 維護模式
	 */
	private String maintenanceMode;
	private String oldMaintenanceMode;
	/**
	 * 維護模式Code
	 */
	private String maintenanceModeCode;
	/**
	 * 設備資料批次異動需要修改的表(中文名稱)
	 */
	private String updateTableName;
	/**
	 * 設備資料批次異動需要修改的表
	 */
	private String updateTable;
	/**
	 * 設備資料批次異動需要修改的表爲歷史月表（需修改的月份）
	 */
	private String monthYear;
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
	 * @param assetCateGory the assetCateGory to set
	 */
	public void setAssetCateGory(String assetCateGory) {
		this.assetCateGory = assetCateGory;
	}

	/**
	 * @return the assetCateGory
	 */
	public String getAssetCateGory() {
		return assetCateGory;
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
	 * Constructor: 無參構造函數
	 */
	public DmmRepositoryDTO() {
		super();
	}

	/**
	 * Constructor:有參構造函數
	 */

	
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
	 * @return the borrowSerialNumber
	 */
	public String getBorrowSerialNumber() {
		return borrowSerialNumber;
	}

	/**
	 * @param borrowSerialNumber the borrowSerialNumber to set
	 */
	public void setBorrowSerialNumber(String borrowSerialNumber) {
		this.borrowSerialNumber = borrowSerialNumber;
	}

	/**
	 * @return the borrowerName
	 */
	public String getBorrowerName() {
		return borrowerName;
	}

	/**
	 * @param borrowerName the borrowerName to set
	 */
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
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
	 * @return the oldPropertyId
	 */
	public String getOldPropertyId() {
		return oldPropertyId;
	}

	/**
	 * @param oldPropertyId the oldPropertyId to set
	 */
	public void setOldPropertyId(String oldPropertyId) {
		this.oldPropertyId = oldPropertyId;
	}

	/**
	 * @return the oldAssetUser
	 */
	public String getOldAssetUser() {
		return oldAssetUser;
	}

	/**
	 * @param oldAssetUser the oldAssetUser to set
	 */
	public void setOldAssetUser(String oldAssetUser) {
		this.oldAssetUser = oldAssetUser;
	}

	/**
	 * @return the oldAssetOwner
	 */
	public String getOldAssetOwner() {
		return oldAssetOwner;
	}

	/**
	 * @param oldAssetOwner the oldAssetOwner to set
	 */
	public void setOldAssetOwner(String oldAssetOwner) {
		this.oldAssetOwner = oldAssetOwner;
	}

	/**
	 * @return the oldAssetType
	 */
	public String getOldAssetType() {
		return oldAssetType;
	}

	/**
	 * @param oldAssetType the oldAssetType to set
	 */
	public void setOldAssetType(String oldAssetType) {
		this.oldAssetType = oldAssetType;
	}

	/**
	 * @return the oldContractCode
	 */
	public String getOldContractCode() {
		return oldContractCode;
	}

	/**
	 * @param oldContractCode the oldContractCode to set
	 */
	public void setOldContractCode(String oldContractCode) {
		this.oldContractCode = oldContractCode;
	}

	/**
	 * @return the oldSimEnableNo
	 */
	public String getOldSimEnableNo() {
		return oldSimEnableNo;
	}

	/**
	 * @param oldSimEnableNo the oldSimEnableNo to set
	 */
	public void setOldSimEnableNo(String oldSimEnableNo) {
		this.oldSimEnableNo = oldSimEnableNo;
	}

	/**
	 * @return the oldEnableDate
	 */
	public Timestamp getOldEnableDate() {
		return oldEnableDate;
	}

	/**
	 * @param oldEnableDate the oldEnableDate to set
	 */
	public void setOldEnableDate(Timestamp oldEnableDate) {
		this.oldEnableDate = oldEnableDate;
	}

	/**
	 * @return the oldAssetInTime
	 */
	public Timestamp getOldAssetInTime() {
		return oldAssetInTime;
	}

	/**
	 * @param oldAssetInTime the oldAssetInTime to set
	 */
	public void setOldAssetInTime(Timestamp oldAssetInTime) {
		this.oldAssetInTime = oldAssetInTime;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the oldCyberApprovedDate
	 */
	public Date getOldCyberApprovedDate() {
		return oldCyberApprovedDate;
	}

	/**
	 * @param oldCyberApprovedDate the oldCyberApprovedDate to set
	 */
	public void setOldCyberApprovedDate(Date oldCyberApprovedDate) {
		this.oldCyberApprovedDate = oldCyberApprovedDate;
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
	 * @return the installAddress
	 */
	public String getInstallAddress() {
		return installAddress;
	}

	/**
	 * @param installAddress the installAddress to set
	 */
	public void setInstallAddress(String installAddress) {
		this.installAddress = installAddress;
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
	 * @return the carryAccount
	 */
	public String getCarryAccount() {
		return carryAccount;
	}

	/**
	 * @param carryAccount the carryAccount to set
	 */
	public void setCarryAccount(String carryAccount) {
		this.carryAccount = carryAccount;
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
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * @param softwareVersion the softwareVersion to set
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
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
	 * @return the warehouseConfirm
	 */
	public String getWarehouseConfirm() {
		return warehouseConfirm;
	}

	/**
	 * @param warehouseConfirm the warehouseConfirm to set
	 */
	public void setWarehouseConfirm(String warehouseConfirm) {
		this.warehouseConfirm = warehouseConfirm;
	}

	/**
	 * @return the oldSimEnableDate
	 */
	public Date getOldSimEnableDate() {
		return oldSimEnableDate;
	}

	/**
	 * @param date the oldSimEnableDate to set
	 */
	public void setOldSimEnableDate(Date oldSimEnableDate) {
		this.oldSimEnableDate = oldSimEnableDate;
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
	 * @return the fee
	 */
	public Integer getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(Integer fee) {
		this.fee = fee;
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
	 * @return the rowId
	 */
	public String getRowId() {
		return rowId;
	}

	/**
	 * @param rowId the rowId to set
	 */
	public void setRowId(String rowId) {
		this.rowId = rowId;
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
	 * @return the tSam
	 */
	public String gettSam() {
		return tSam;
	}

	/**
	 * @param tSam the tSam to set
	 */
	public void settSam(String tSam) {
		this.tSam = tSam;
	}

	/**
	 * @return the aoAame
	 */
	public String getAoAame() {
		return aoAame;
	}

	/**
	 * @param aoAame the aoAame to set
	 */
	public void setAoAame(String aoAame) {
		this.aoAame = aoAame;
	}
	/**
	 * @return the maintenanceTax
	 */
	public Integer getMaintenanceTax() {
		return maintenanceTax;
	}

	/**
	 * @param maintenanceTax the maintenanceTax to set
	 */
	public void setMaintenanceTax(Integer maintenanceTax) {
		this.maintenanceTax = maintenanceTax;
	}

	/**
	 * @return the maintenanceNoTax
	 */
	public Integer getMaintenanceNoTax() {
		return maintenanceNoTax;
	}

	/**
	 * @param maintenanceNoTax the maintenanceNoTax to set
	 */
	public void setMaintenanceNoTax(Integer maintenanceNoTax) {
		this.maintenanceNoTax = maintenanceNoTax;
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
	 * @return the numberFeeTax
	 */
	public Integer getNumberFeeTax() {
		return numberFeeTax;
	}

	/**
	 * @param numberFeeTax the numberFeeTax to set
	 */
	public void setNumberFeeTax(Integer numberFeeTax) {
		this.numberFeeTax = numberFeeTax;
	}

	/**
	 * @return the numberFeeNoTax
	 */
	public Integer getNumberFeeNoTax() {
		return numberFeeNoTax;
	}

	/**
	 * @param numberFeeNoTax the numberFeeNoTax to set
	 */
	public void setNumberFeeNoTax(Integer numberFeeNoTax) {
		this.numberFeeNoTax = numberFeeNoTax;
	}

	/**
	 * @return the maintenanceTaxSum
	 */
	public Integer getMaintenanceTaxSum() {
		return maintenanceTaxSum;
	}

	/**
	 * @param maintenanceTaxSum the maintenanceTaxSum to set
	 */
	public void setMaintenanceTaxSum(Integer maintenanceTaxSum) {
		this.maintenanceTaxSum = maintenanceTaxSum;
	}

	/**
	 * @return the maintenanceNoTaxSum
	 */
	public Integer getMaintenanceNoTaxSum() {
		return maintenanceNoTaxSum;
	}

	/**
	 * @param maintenanceNoTaxSum the maintenanceNoTaxSum to set
	 */
	public void setMaintenanceNoTaxSum(Integer maintenanceNoTaxSum) {
		this.maintenanceNoTaxSum = maintenanceNoTaxSum;
	}

	/**
	 * @return the numberFeeTaxSum
	 */
	public Integer getNumberFeeTaxSum() {
		return numberFeeTaxSum;
	}

	/**
	 * @param numberFeeTaxSum the numberFeeTaxSum to set
	 */
	public void setNumberFeeTaxSum(Integer numberFeeTaxSum) {
		this.numberFeeTaxSum = numberFeeTaxSum;
	}

	/**
	 * @return the numberFeeNoTaxSum
	 */
	public Integer getNumberFeeNoTaxSum() {
		return numberFeeNoTaxSum;
	}

	/**
	 * @param numberFeeNoTaxSum the numberFeeNoTaxSum to set
	 */
	public void setNumberFeeNoTaxSum(Integer numberFeeNoTaxSum) {
		this.numberFeeNoTaxSum = numberFeeNoTaxSum;
	}

	/**
	 * @return the merchantAddress
	 */
	public String getMerchantAddress() {
		return merchantAddress;
	}

	/**
	 * @param merchantAddress the merchantAddress to set
	 */
	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	/**
	 * @return the inScrap
	 */
	public int getInScrap() {
		return inScrap;
	}

	/**
	 * @param inScrap the inScrap to set
	 */
	public void setInScrap(int inScrap) {
		this.inScrap = inScrap;
	}

	/**
	 * @return the inUse
	 */
	public int getInUse() {
		return inUse;
	}

	/**
	 * @param inUse the inUse to set
	 */
	public void setInUse(int inUse) {
		this.inUse = inUse;
	}

	/**
	 * @return the inEnable
	 */
	public int getInEnable() {
		return inEnable;
	}

	/**
	 * @param inEnable the inEnable to set
	 */
	public void setInEnable(int inEnable) {
		this.inEnable = inEnable;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
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
	 * @return the simCardPrice
	 */
	public Integer getSimCardPrice() {
		return simCardPrice;
	}

	/**
	 * @param simCardPrice the simCardPrice to set
	 */
	public void setSimCardPrice(Integer simCardPrice) {
		this.simCardPrice = simCardPrice;
	}

	/**
	 * @return the workCategory
	 */
	public String getWorkCategory() {
		return workCategory;
	}

	/**
	 * @param workCategory the workCategory to set
	 */
	public void setWorkCategory(String workCategory) {
		this.workCategory = workCategory;
	}

	/**
	 * @return the firstPrice
	 */
	public Integer getFirstPrice() {
		return firstPrice;
	}

	/**
	 * @param firstPrice the firstPrice to set
	 */
	public void setFirstPrice(Integer firstPrice) {
		this.firstPrice = firstPrice;
	}

	/**
	 * @return the firstCount
	 */
	public String getFirstCount() {
		return firstCount;
	}

	/**
	 * @param firstCount the firstCount to set
	 */
	public void setFirstCount(String firstCount) {
		this.firstCount = firstCount;
	}

	/**
	 * @return the otherPrice
	 */
	public Integer getOtherPrice() {
		return otherPrice;
	}

	/**
	 * @param otherPrice the otherPrice to set
	 */
	public void setOtherPrice(Integer otherPrice) {
		this.otherPrice = otherPrice;
	}

	/**
	 * @return the otherCount
	 */
	public String getOtherCount() {
		return otherCount;
	}

	/**
	 * @param otherCount the otherCount to set
	 */
	public void setOtherCount(String otherCount) {
		this.otherCount = otherCount;
	}

	/**
	 * @return the sum
	 */
	public Integer getSum() {
		return sum;
	}

	/**
	 * @param sum the sum to set
	 */
	public void setSum(Integer sum) {
		this.sum = sum;
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
	 * @return the workFeeSettingList
	 */
	public List<WorkFeeSetting> getWorkFeeSettingList() {
		return workFeeSettingList;
	}

	/**
	 * @param workFeeSettingList the workFeeSettingList to set
	 */
	public void setWorkFeeSettingList(List<WorkFeeSetting> workFeeSettingList) {
		this.workFeeSettingList = workFeeSettingList;
	}

	/**
	 * @return the workFeeSettingFeeList
	 */
	public List<WorkFeeSetting> getWorkFeeSettingFeeList() {
		return workFeeSettingFeeList;
	}

	/**
	 * @param workFeeSettingFeeList the workFeeSettingFeeList to set
	 */
	public void setWorkFeeSettingFeeList(List<WorkFeeSetting> workFeeSettingFeeList) {
		this.workFeeSettingFeeList = workFeeSettingFeeList;
	}

	public Integer getCommModeNum() {
		return commModeNum;
	}

	public void setCommModeNum(Integer commModeNum) {
		this.commModeNum = commModeNum;
	}
	/**
	 * @return the is3G
	 */
	public Boolean getIs3G() {
		return is3G;
	}

	/**
	 * @param is3g the is3G to set
	 */
	public void setIs3G(Boolean is3g) {
		is3G = is3g;
	}

	/**
	 * @return the threeGPrice
	 */
	public Integer getThreeGPrice() {
		return threeGPrice;
	}

	/**
	 * @param threeGPrice the threeGPrice to set
	 */
	public void setThreeGPrice(Integer threeGPrice) {
		this.threeGPrice = threeGPrice;
	}

	/**
	 * @return the isNoMerId
	 */
	public String getIsNoMerId() {
		return isNoMerId;
	}

	/**
	 * @param isNoMerId the isNoMerId to set
	 */
	public void setIsNoMerId(String isNoMerId) {
		this.isNoMerId = isNoMerId;
	}

	/**
	 * @return the isNoMerHeaderId
	 */
	public String getIsNoMerHeaderId() {
		return isNoMerHeaderId;
	}

	/**
	 * @param isNoMerHeaderId the isNoMerHeaderId to set
	 */
	public void setIsNoMerHeaderId(String isNoMerHeaderId) {
		this.isNoMerHeaderId = isNoMerHeaderId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

	public String getTaixinMaintenance() {
		return taixinMaintenance;
	}

	public void setTaixinMaintenance(String taixinMaintenance) {
		this.taixinMaintenance = taixinMaintenance;
	}

	public String getJdwMaintenance() {
		return jdwMaintenance;
	}

	public void setJdwMaintenance(String jdwMaintenance) {
		this.jdwMaintenance = jdwMaintenance;
	}

	/**
	 * @return the maintainCompanyName
	 */
	public String getMaintainCompanyName() {
		return maintainCompanyName;
	}

	/**
	 * @param maintainCompanyName the maintainCompanyName to set
	 */
	public void setMaintainCompanyName(String maintainCompanyName) {
		this.maintainCompanyName = maintainCompanyName;
	}

	/**
	 * @return the maintainUserName
	 */
	public String getMaintainUserName() {
		return maintainUserName;
	}

	/**
	 * @param maintainUserName the maintainUserName to set
	 */
	public void setMaintainUserName(String maintainUserName) {
		this.maintainUserName = maintainUserName;
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
	 * @return the oldCaseId
	 */
	public String getOldCaseId() {
		return oldCaseId;
	}

	/**
	 * @param oldCaseId the oldCaseId to set
	 */
	public void setOldCaseId(String oldCaseId) {
		this.oldCaseId = oldCaseId;
	}

	/**
	 * @return the oldAnalyzeDate
	 */
	public Date getOldAnalyzeDate() {
		return oldAnalyzeDate;
	}

	/**
	 * @param oldAnalyzeDate the oldAnalyzeDate to set
	 */
	public void setOldAnalyzeDate(Date oldAnalyzeDate) {
		this.oldAnalyzeDate = oldAnalyzeDate;
	}

	/**
	 * @return the oldCaseCompletionDate
	 */
	public Timestamp getOldCaseCompletionDate() {
		return oldCaseCompletionDate;
	}

	/**
	 * @param oldCaseCompletionDate the oldCaseCompletionDate to set
	 */
	public void setOldCaseCompletionDate(Timestamp oldCaseCompletionDate) {
		this.oldCaseCompletionDate = oldCaseCompletionDate;
	}

	/**
	 * @return the oldInstalledAdress
	 */
	public String getOldInstalledAdress() {
		return oldInstalledAdress;
	}

	/**
	 * @param oldInstalledAdress the oldInstalledAdress to set
	 */
	public void setOldInstalledAdress(String oldInstalledAdress) {
		this.oldInstalledAdress = oldInstalledAdress;
	}

	/**
	 * @return the oldMerchantHeaderId
	 */
	public String getOldMerchantHeaderId() {
		return oldMerchantHeaderId;
	}

	/**
	 * @param oldMerchantHeaderId the oldMerchantHeaderId to set
	 */
	public void setOldMerchantHeaderId(String oldMerchantHeaderId) {
		this.oldMerchantHeaderId = oldMerchantHeaderId;
	}

	/**
	 * @return the oldIsCup
	 */
	public String getOldIsCup() {
		return oldIsCup;
	}

	/**
	 * @param oldIsCup the oldIsCup to set
	 */
	public void setOldIsCup(String oldIsCup) {
		this.oldIsCup = oldIsCup;
	}

	/**
	 * @return the oldDtid
	 */
	public String getOldDtid() {
		return oldDtid;
	}

	/**
	 * @param oldDtid the oldDtid to set
	 */
	public void setOldDtid(String oldDtid) {
		this.oldDtid = oldDtid;
	}

	/**
	 * @return the oldMaintainCompany
	 */
	public String getOldMaintainCompany() {
		return oldMaintainCompany;
	}

	/**
	 * @param oldMaintainCompany the oldMaintainCompany to set
	 */
	public void setOldMaintainCompany(String oldMaintainCompany) {
		this.oldMaintainCompany = oldMaintainCompany;
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
	 * @return the oldInstalledAdressLocation
	 */
	public String getOldInstalledAdressLocation() {
		return oldInstalledAdressLocation;
	}

	/**
	 * @param oldInstalledAdressLocation the oldInstalledAdressLocation to set
	 */
	public void setOldInstalledAdressLocation(String oldInstalledAdressLocation) {
		this.oldInstalledAdressLocation = oldInstalledAdressLocation;
	}

	/**
	 * @return the oldMaintainUser
	 */
	public String getOldMaintainUser() {
		return oldMaintainUser;
	}

	/**
	 * @param oldMaintainUser the oldMaintainUser to set
	 */
	public void setOldMaintainUser(String oldMaintainUser) {
		this.oldMaintainUser = oldMaintainUser;
	}
	/** (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DmmRepositoryDTO [areaName=" + areaName + ", area=" + area
				+ ", installAddress=" + installAddress + ", isSimEnable="
				+ isSimEnable + ", simEnableNo=" + simEnableNo + ", carryDate="
				+ carryDate + ", departmentId=" + departmentId
				+ ", problemReason=" + problemReason + ", caseId=" + caseId
				+ ", analyzeDate=" + analyzeDate + ", vendorStaff="
				+ vendorStaff + ", caseCompletionDate=" + caseCompletionDate
				+ ", applicationId=" + applicationId + ", merchantId="
				+ merchantId + ", installedAdress=" + installedAdress
				+ ", merchantHeaderId=" + merchantHeaderId + ", checkedDate="
				+ checkedDate + ", assetId=" + assetId + ", serialNumber="
				+ serialNumber + ", propertyId=" + propertyId + ", is3G="
				+ is3G + ", threeGPrice=" + threeGPrice + ", action=" + action
				+ ", status=" + status + ", statusName=" + statusName
				+ ", assetTypeId=" + assetTypeId + ", assetTypeName="
				+ assetTypeName + ", keeperName=" + keeperName
				+ ", warehouseId=" + warehouseId + ", carryAccount="
				+ carryAccount + ", maType=" + maType + ", isCup=" + isCup
				+ ", isCupChar=" + isCupChar + ", isEnableChar=" + isEnableChar
				+ ", carrier=" + carrier + ", carryComment=" + carryComment
				+ ", tid=" + tid + ", merInstallAddress=" + merInstallAddress
				+ ", assetInId=" + assetInId + ", assetTransId=" + assetTransId
				+ ", isEnabled=" + isEnabled + ", enableDate=" + enableDate
				+ ", repairVendor=" + repairVendor + ", repairComment="
				+ repairComment + ", retireReasonCode=" + retireReasonCode
				+ ", retireComment=" + retireComment + ", createUser="
				+ createUser + ", createUserName=" + createUserName
				+ ", createDate=" + createDate + ", updateUser=" + updateUser
				+ ", updateUserName=" + updateUserName + ", updateDate="
				+ updateDate + ", installedDeptId=" + installedDeptId
				+ ", simEnableDate=" + simEnableDate + ", oldSimEnableDate="
				+ oldSimEnableDate + ", assetOwner=" + assetOwner
				+ ", assetUser=" + assetUser + ", dtid=" + dtid
				+ ", contractId=" + contractId + ", contractCode="
				+ contractCode + ", borrower=" + borrower + ", borrowerStart="
				+ borrowerStart + ", borrowerEnd=" + borrowerEnd
				+ ", borrowerComment=" + borrowerComment + ", backDate="
				+ backDate + ", maintainCompany=" + maintainCompany
				+ ", description=" + description + ", faultComponent="
				+ faultComponent + ", faultDescription=" + faultDescription
				+ ", borrowerEmail=" + borrowerEmail + ", borrowerMgrEmail="
				+ borrowerMgrEmail + ", customerWarrantyDate="
				+ customerWarrantyDate + ", factoryWarrantyDate="
				+ factoryWarrantyDate + ", assetInTime=" + assetInTime
				+ ", installType=" + installType + ", itemName=" + itemName
				+ ", appName=" + appName + ", appVersion=" + appVersion
				+ ", shortName=" + shortName + ", customerApproveDate="
				+ customerApproveDate + ", assetUserName=" + assetUserName
				+ ", assetOwnerName=" + assetOwnerName + ", model=" + model
				+ ", name=" + name + ", myDate=" + myDate + ", merName="
				+ merName + ", headerName=" + headerName + ", itemValue="
				+ itemValue + ", merchantCode=" + merchantCode
				+ ", merchantName=" + merchantName + ", bussinessAddress="
				+ bussinessAddress + ", merchantAddress=" + merchantAddress
				+ ", isCustomerChecked=" + isCustomerChecked + ", isChecked="
				+ isChecked + ", repairVendorId=" + repairVendorId
				+ ", faultComponentId=" + faultComponentId
				+ ", faultDescriptionId=" + faultDescriptionId
				+ ", borrowSerialNumber=" + borrowSerialNumber
				+ ", borrowerName=" + borrowerName + ", userId=" + userId
				+ ", oldPropertyId=" + oldPropertyId + ", oldAssetUser="
				+ oldAssetUser + ", oldAssetOwner=" + oldAssetOwner
				+ ", oldAssetType=" + oldAssetType + ", oldContractCode="
				+ oldContractCode + ", oldSimEnableNo=" + oldSimEnableNo
				+ ", oldEnableDate=" + oldEnableDate + ", oldAssetInTime="
				+ oldAssetInTime + ", message=" + message
				+ ", cyberApprovedDate=" + cyberApprovedDate
				+ ", oldCyberApprovedDate=" + oldCyberApprovedDate
				+ ", assetCateGory=" + assetCateGory + ", edcPeople="
				+ edcPeople + ", edcComment=" + edcComment
				+ ", itemCategoryName=" + itemCategoryName + ", warehouseName="
				+ warehouseName + ", softwareVersion=" + softwareVersion
				+ ", installedAdressLocation=" + installedAdressLocation
				+ ", brand=" + brand + ", carry=" + carry + ", assetBrand="
				+ assetBrand + ", assetModel=" + assetModel
				+ ", warehouseConfirm=" + warehouseConfirm + ", repairCompany="
				+ repairCompany + ", maintainUser=" + maintainUser
				+ ", uninstallOrRepairReason=" + uninstallOrRepairReason
				+ ", counter=" + counter + ", oldCounter=" + oldCounter
				+ ", cartonNo=" + cartonNo + ", oldCartonNo=" + oldCartonNo
				+ ", fee=" + fee + ", companyCode=" + companyCode + ", rowId="
				+ rowId + ", caseStatus=" + caseStatus + ", tSam=" + tSam
				+ ", aoAame=" + aoAame + ", maintenanceTax=" + maintenanceTax
				+ ", maintenanceNoTax=" + maintenanceNoTax
				+ ", maintenanceTaxSum=" + maintenanceTaxSum
				+ ", maintenanceNoTaxSum=" + maintenanceNoTaxSum
				+ ", numberFeeTax=" + numberFeeTax + ", numberFeeNoTax="
				+ numberFeeNoTax + ", numberFeeTaxSum=" + numberFeeTaxSum
				+ ", numberFeeNoTaxSum=" + numberFeeNoTaxSum + ", remark="
				+ remark + ", actionValue=" + actionValue + ", commModeId="
				+ commModeId + ", inScrap=" + inScrap + ", inUse=" + inUse
				+ ", inEnable=" + inEnable + ", total=" + total
				+ ", simCardPrice=" + simCardPrice + ", workCategory="
				+ workCategory + ", firstPrice=" + firstPrice + ", firstCount="
				+ firstCount + ", otherPrice=" + otherPrice + ", otherCount="
				+ otherCount + ", sum=" + sum + ", number=" + number
				+ ", freeTmsFrequency=" + freeTmsFrequency
				+ ", surplusFreeFrequency=" + surplusFreeFrequency
				+ ", workFeeSettingList=" + workFeeSettingList
				+ ", workFeeSettingFeeList=" + workFeeSettingFeeList
				+ ", isNoMerId=" + isNoMerId + ", isNoMerHeaderId="
				+ isNoMerHeaderId + ", commModeNum=" + commModeNum
				+ ", queryCaseFlag=" + queryCaseFlag + ", maintenanceMode=" + maintenanceMode + "]";
	}

	/**
	 * @return the ename
	 */
	public String getEname() {
		return ename;
	}

	/**
	 * @param ename the ename to set
	 */
	public void setEname(String ename) {
		this.ename = ename;
	}

	/**
	 * @return the freeTmsFrequency
	 */
	public String getFreeTmsFrequency() {
		return freeTmsFrequency;
	}

	/**
	 * @param freeTmsFrequency the freeTmsFrequency to set
	 */
	public void setFreeTmsFrequency(String freeTmsFrequency) {
		this.freeTmsFrequency = freeTmsFrequency;
	}

	/**
	 * @return the surplusFreeFrequency
	 */
	public String getSurplusFreeFrequency() {
		return surplusFreeFrequency;
	}

	/**
	 * @param surplusFreeFrequency the surplusFreeFrequency to set
	 */
	public void setSurplusFreeFrequency(String surplusFreeFrequency) {
		this.surplusFreeFrequency = surplusFreeFrequency;
	}

	/**
	 * @return the oldModel
	 */
	public String getOldModel() {
		return oldModel;
	}

	/**
	 * @param oldModel the oldModel to set
	 */
	public void setOldModel(String oldModel) {
		this.oldModel = oldModel;
	}

	/**
	 * @return the oldBrand
	 */
	public String getOldBrand() {
		return oldBrand;
	}

	/**
	 * @param oldBrand the oldBrand to set
	 */
	public void setOldBrand(String oldBrand) {
		this.oldBrand = oldBrand;
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
	 * @return the updateTableName
	 */
	public String getUpdateTableName() {
		return updateTableName;
	}

	/**
	 * @param updateTableName the updateTableName to set
	 */
	public void setUpdateTableName(String updateTableName) {
		this.updateTableName = updateTableName;
	}

	/**
	 * @return the monthYear
	 */
	public String getMonthYear() {
		return monthYear;
	}

	/**
	 * @param monthYear the monthYear to set
	 */
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	/**
	 * @return the updateTable
	 */
	public String getUpdateTable() {
		return updateTable;
	}

	/**
	 * @param updateTable the updateTable to set
	 */
	public void setUpdateTable(String updateTable) {
		this.updateTable = updateTable;
	}

	/**
	 * @return the maintenanceMode
	 */
	public String getMaintenanceMode() {
		return maintenanceMode;
	}

	/**
	 * @param maintenanceMode the maintenanceMode to set
	 */
	public void setMaintenanceMode(String maintenanceMode) {
		this.maintenanceMode = maintenanceMode;
	}

	/**
	 * @return the oldMaintenanceMode
	 */
	public String getOldMaintenanceMode() {
		return oldMaintenanceMode;
	}

	/**
	 * @param oldMaintenanceMode the oldMaintenanceMode to set
	 */
	public void setOldMaintenanceMode(String oldMaintenanceMode) {
		this.oldMaintenanceMode = oldMaintenanceMode;
	}

	/**
	 * @return the maintenanceModeCode
	 */
	public String getMaintenanceModeCode() {
		return maintenanceModeCode;
	}

	/**
	 * @param maintenanceModeCode the maintenanceModeCode to set
	 */
	public void setMaintenanceModeCode(String maintenanceModeCode) {
		this.maintenanceModeCode = maintenanceModeCode;
	}
	
	
}
