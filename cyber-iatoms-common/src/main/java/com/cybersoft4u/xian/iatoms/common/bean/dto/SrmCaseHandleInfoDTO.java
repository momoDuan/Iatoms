package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.DataTransferObject;

import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.reportsetting.WorkFeeSetting;

/**
 * Purpose: 案件處理DTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/11/8
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseHandleInfoDTO extends DataTransferObject<String>{
	
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 8119520869284568672L;
	/**
	 * Purpose:enum 
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2016年12月9日
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		CASE_ID("caseId"),
		REPEAT_CASE_ID("repeatCaseId"),
		CASE_CATEGORY("caseCategory"),
		CASE_CATEGORY_NAME("caseCategoryName"),
		CUSTOMER_ID("customerId"),
		CUSTOMER_NAME("customerName"),
		COMPANY_CODE("companyCode"),
		HEADER_NAME("headerName"),
		CONTRACT_ID("contractId"),
		CONTRACT_CODE("contractCode"),
		TID("tid"),
		REQUIREMENT_NO("requirementNo"),
		PROCESS_TYPE("processType"),
		PROCESS_TYPE_NAME("processTypeName"),
		INSTALL_TYPE("installType"),
		INSTALL_TYPE_NAME("installTypeName"),
		INSTALL_OR_UNINSTALLED_TYPE("installOrUninstalledType"),
		INSTALL_OR_UNINSTALLED_ADDRESS("installOrUninstalledAddress"),
		INSTALL_OR_UNINSTALLED_CONTACT("installOrUninstalledContact"),
		INSTALL_OR_UNINSTALLED_PHONE("installOrUninstalledPhone"),
		DTID("dtid"),
		COMPANY_ID("companyId"),
		COMPANY_NAME("companyName"),
		DEPARTMENT_ID("departmentId"),
		DEPARTMENT_NAME("departmentName"),
		VENDOR_DEPT("vendorDept"),
		VENDOR_DEPT_NAME("vendorDeptName"),
		VENDOR_STAFF("vendorStaff"),
		CASE_TYPE("caseType"),
		CASE_TYPE_NAME("caseTypeName"),
		EXPECTED_COMPLETION_DATE("expectedCompletionDate"),
		MERCHANT_CODE("merchantCode"),
		OLD_MERCHANT_CODE("oldMerchantCode"),
		MERCHANT_HEADER_ID("merchantHeaderId"),
		MERCHANT_HEADER_NAME("merchantHeaderName"),
		BUSSINESS_ADDRESS("bussinessAddress"),
		LOCATION("location"),
		LOCATION_NAME("locationName"),
		INSTALLED_ADRESS("installedAdress"),
		INSTALL_LOCATION("installLocation"),
		INSTALLED_ADRESS_LOCATION("installedAdressLocation"),
		IS_BUSSINESS_ADDRESS("isBussinessAddress"),
		INSTALLED_CONTACT("installedContact"),
		IS_BUSSINESS_CONTACT("isBussinessContact"),
		INSTALLED_CONTACT_PHONE("installedContactPhone"),
		IS_BUSSINESS_CONTACT_PHONE("isBussinessContactPhone"),
		EDC_TYPE_NAME("edcTypeName"),
		SOFTWARE_VERSION_NAME("softwareVersionName"),
		BUILT_IN_FEATURE_NAME("builtInFeatureName"),
		MULTI_MODULE_NAME("multiModuleName"),
		PERIPHERALS_NAME("peripheralsName"),
		PERIPHERALS_FUNCTION_NAME("peripheralsFunctionName"),
		PERIPHERALS_SERIAL_NUMBER("peripheralsSerialNumber"),
		PERIPHERALS_CONTRACT_CODE("peripheralsContractCode"),
		ECR_CONNECTION_NAME("ecrConnectionName"),
		PERIPHERALS2_NAME("peripherals2Name"),
		PERIPHERALS_FUNCTION2_NAME("peripheralsFunction2Name"),
		PERIPHERALS2_SERIAL_NUMBER("peripherals2SerialNumber"),
		PERIPHERALS2_CONTRACT_CODE("peripherals2ContractCode"),
		CONNECTION_TYPE_NAME("connectionTypeName"),
		PERIPHERALS3_NAME("peripherals3Name"),
		PERIPHERALS_FUNCTION3_NAME("peripheralsFunction3Name"),
		PERIPHERALS3_SERIAL_NUMBER("peripherals3SerialNumber"),
		PERIPHERALS3_CONTRACT_CODE("peripherals3ContractCode"),
		EDC_TYPE("edcType"),
		SOFTWARE_VERSION("softwareVersion"),
		BUILT_IN_FEATURE("builtInFeature"),
		MULTI_MODULE("multiModule"),
		PERIPHERALS("peripherals"),
		PERIPHERALS_FUNCTION("peripheralsFunction"),
		ECR_CONNECTION("ecrConnection"),
		PERIPHERALS2("peripherals2"),
		PERIPHERALS_FUNCTION2("peripheralsFunction2"),
		CONNECTION_TYPE("connectionType"),
		PERIPHERALS3("peripherals3"),
		PERIPHERALS_FUNCTION3("peripheralsFunction3"),
		LOCALHOST_IP("localhostIp"),
		NET_VENDOR_ID("netVendorId"),
		NET_VENDOR_NAME("netVendorName"),
		GATEWAY("gateway"),
		NETMASK("netmask"),
		DESCRIPTION("description"),
		ATTENDANCE_TIMES("attendanceTimes"),
		REPAIR_REASON("repairReason"),
		REPAIR_REASON_NAME("repairReasonName"),
		REPAIR_TIMES("repairTimes"),
		UNINSTALL_TYPE("uninstallType"),
		UNINSTALL_TYPE_NAME("uninstallTypeName"),
		UNINSTALLED_ADRESS("uninstalledAdress"),
		UNINSTALLED_ADDRESS_LOCATION("uninstalledAddressLocation"),
		UNINSTALLED_IS_BUSSINESS_ADDRESS("uninstalledIsBussinessAddress"),
		UNINSTALLED_CONTACT("uninstalledContact"),
		UNINSTALLED_IS_BUSSINESS_CONTACT("uninstalledIsBussinessContact"),
		UNINSTALLED_CONTACT_PHONE("uninstalledContactPhone"),
		UNINSTALLED_IS_BUSSINESS_PHONE("uninstalledIsBussinessPhone"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		CREATED_FINISH_DATE("createdFinishDate"),
		ACCEPTABLE_RESPONSE_DATE("acceptableResponseDate"),
		ACCEPTABLE_ARRIVE_DATE("acceptableArriveDate"),
		ACCEPTABLE_FINISH_DATE("acceptableFinishDate"),
		MERCHANT_NAME("merchantName"),
		ASSET_NAME("assetName"),
		ASSET_OWNER("assetOwner"),
		MODEL("model"),
		SERIAL_NUMBER("serialNumber"),
		ENABLE_DATE("enableDate"),
		MERCHANT_ID("merchantId"),
		IS_VIP("isVip"),
		AREA("area"),
		AREA_NAME("areaName"),
		CONTACT("contact"),
		CONTACT_TEL("contactTel"),
		CONTACT_TEL2("contactTel2"),
		PHONE("phone"),
		BUSINESS_ADDRESS("businessAddress"),
		OPEN_HOUR("openHour"),
		CLOSE_HOUR("closeHour"),
		BUSINESS_HOURS("businessHours"),
		AO_EMAIL("aoemail"),
		AO_NAME("aoName"),
		PROJECT_NAME("projectName"),
		PROJECT_CODE("projectCode"),
		IS_PROJECT("isProject"),
		UPDATED_DESCRIPTION("updatedDescription"),
		MER_LOCATION("merLocation"),
		CASE_TRANSACTION_PARAMETER_STR("caseTransactionParameterStr"),
		CASE_NUMBER("caseNumber"),
		IS_SINGLE("isSingle"),
		CASE_STATUS("caseStatus"),
		CASE_STATUS_NAME("caseStatusName"),
		APPLICATION_NAME("applicationName"),
		TO_MAIL("toMail"),
		CC_MAIL("ccMail"),
		TO_NAME("toName"),
		FROM_MAIL("fromMail"),
		FROM_NAME("fromName"),
		MAIL_SUBJECT("mailSubject"),
		MAIL_CONTEXT("mailContext"),
		MAIL_CONTEXT1("mailContext1"),
		MAIL_CONTEXT2("mailContext2"),
		HAVE_TRANS_PARAMETER("haveTransParameter"),
		CASE_TRANSACTION_D_T_OS("caseTransactionDTOs"),
		CASE_TRANSACTION_STR("caseTransactionStr"),
		HAVE_CASE_TRANSACTION("haveCaseTransaction"),
		DISPATCH_DEPT_ID("dispatchDeptId"),
		UNINSTALLED_ADRESS_LOCATION("uninstalledAdressLocation"),
		CONTACT_ADDRESS_LOCATION("contactAddressLocation"),
		CONTACT_ADDRESS("contactAddress"),
		CONTACT_ADDRESS_INSTALL("contactAddressInstall"),
		CONTACT_USER("contactUser"),
		CONTACT_USER_PHONE("contactUserPhone"),
		DISPATCH_PROCESS_USER("dispatchProcessUser"),
		DISPATCH_PROCESS_USERNAME("dispatchProcessUsername"),
		DISPATCH_USER("dispatchUser"),
		DISPATCH_USER_NAME("dispatchUserName"),
		DISPATCH_DATE("dispatchDate"),
		RESPONSE_USER("responseUser"),
		RESPONSE_USER_NAME("responseUserName"),
		RESPONSE_DATE("responseDate"),
		ARRIVE_USER("arriveUser"),
		ARRIVE_USER_NAME("arriveUserName"),
		ARRIVE_DATE("arriveDate"),
		COMPLETE_USER("completeUser"),
		COMPLETE_USER_NAME("completeUserName"),
		COMPLETE_DATE("completeDate"),
		ANALYZE_USER("analyzeUser"),
		ANALYZE_USER_NAME("analyzeUserName"),
		ANALYZE_DATE("analyzeDate"),
		CLOSE_USER("closeUser"),
		CLOSE_USER_NAME("closeUserName"),
		INSTALLED_DEPT_ID("installedDeptId"),
		INSTALLED_USER("installedUser"),
		IS_TMS("isTms"),
		PROBLEM_REASON("problemReason"),
		PROBLEM_REASON_NAME("problemReasonName"),
		PROBLEM_REASON_CATEGORY_NAME("problemReasonCategoryName"),
		PROBLEM_SOLUTION("problemSolution"),
		PROBLEM_SOLUTION_NAME("problemSolutionName"),
		PROBLEM_SOLUTION_CATEGORY_NAME("problemSolutionCategoryName"),
		RESPONSE_STATUS("responseStatus"),
		ARRIVE_STATUS("arriveStatus"),
		COMPLETE_STATUS("completeStatus"),
		CASE_DEAL_STATUS("caseDealStatus"),
		VENDOR_NAME("vendorName"),
		WAREHOUSE_ID("wareHouseId"),
		WAREHOUSE_NAME("wareHouseName"),
		REPAIR_ADRESS("repairAdress"),
		EDC_TYPE_CONTRACT("edcTypeContract"),
		PERIPHERALS_CONTRACT("peripheralsContract"),
		PERIPHERALS2_CONTRACT("peripherals2Contract"),
		PERIPHERALS3_CONTRACT("peripherals3Contract"),
		OPEN_FUNCTION_NAME("openFunctionName"),
		CUP_TRANS_TYPE("cupTransType"),
		DCC_TRANS_TYPE("dccTransType"),
		RESPONSIBITY("responsibity"),
		RESPONSIBITY_NAME("responsibityName"),
		DEAL_DATE("dealDate"),
		TRANSACTION_DESCRIPTION("transactionDescription"),
		CLOSE_DATE("closeDate"),
		RESPONSE_WARNNING("responseWarnning"),
		ARRIVE_WARNNING("arriveWarnning"),
		COMPLETE_WARNNING("completeWarnning"),
		RESPONSE_WARN_DATE("responseWarnDate"),
		ARRIVE_WARN_DATE("arriveWarnDate"),
		COMPLETE_WARN_DATE("completeWarnDate"),
		CASE_COUNT("caseCount"),
		CLOSE_COUNT("closeCount"),
		RESPONSE_COUNT("responseCount"),
		RESPONSE_PER("responsePer"),
		FINISH_COUNT("finishCount"),
		FINISH_PER("finishPer"),
		RUSH_REPAIR("rushRepair"),
		RESPONSE_CONDITION("responseCondition"),
		ARRIVE_CONDITION("arriveCondition"),
		COMPLETE_CONDITION("completeCondition"),
		CONTACT_IS_BUSSINESS_ADDRESS("contactIsBussinessAddress"),
		CONTACT_IS_BUSSINESS_CONTACT("contactIsBussinessContact"),
		CONTACT_IS_BUSSINESS_CONTACT_PHONE("contactIsBussinessContactPhone"),
		INSTALLED_DATE("installedDate"),
		UNINSTALLED_DATE("uninstalledDate"),
		CUP_ENABLE_DATE("cupEnableDate"),
		CUP_DISABLE_DATE("cupDisableDate"),
		UPDATE_ITEM("updateItem"),
		NETWORK_LINE_NUMBER("networkLineNumber"),
		OPEN_TRANSACTION_LIST("openTransactionList"),
		INSTALLED_USER_NAME("installedUserName"),
		IS_HISTORY("isHistory"),
		MER_MID("merMid"),
		PROBLEM_REASON_CODE("problemReasonCode"),
		PROBLEM_SOLUTION_CODE("problemSolutionCode"),
		ELECTRONIC_INVOICE("electronicInvoice"),
		CUP_QUICK_PASS("cupQuickPass"),
		DISPATCH_DEPT_NAME("dispatchDeptName"),
		PROPERTY_ID("propertyId"),
		SAME_INSTALLED("sameInstalled"),
		TMS_PARAM_DESC("tmsParamDesc"),
		EDC_SERIAL_NUMBER("edcSerialNumber"),
		OLD_INSTALLED_DEPT_AND_USER("oldInstalledDeptAndUser"),
		FUNCTIONTYPE_LIST("functionTypeList"),
		ATTENDANCEDESC("attendanceDesc"),
		RUSHREPAIRDESC("rushRepairDesc"),
		ECRLINE("ecrLine"),
		NETLINE("netLine"),
		OTHERLINE("otherLine"),
		DELAYTIME("delayTime"),
		IS_FIRST_INSTALLED("isFirstInstalled"),
		POSPRICE("posPrice"),
		POSPRICE_INFEE("posPriceInFee"),
		STARTDATE("startDate"),
		ENDDATE("endDate"),
		BUSSION_ADDRESS("bussionAddress"),
		INSTALLED_ADDRESS("installedAddress"),
		UNINSTALLED_COMPANYNAME("unInstalledCompanyName"),
		UNINSTALLED_ADDRESS("unInstalledAddress"),
		IS_OPEN_ENCRYPT("isOpenEncrypt"),
		ELECTRONIC_PAY_PLATFORM("electronicPayPlatform"),
		LOGO_STYLE("logoStyle"),
		USERDDAYS90("userdDays90"),
		USERDDAYS120("userdDays120"),
		USERDDAYS120_PRICE("userdDays120Price"),
		IS_UPDATE_ASSET("isUpdateAsset"),
		MERCHANT_CODE_OTHER("merchantCodeOther"),
		FOMS_CASE("fomsCase"),
		UNION_PAY_FLAG("unionPayFlag"),
		SMART_PAY_FLAG("smartPayFlag"),
		SMID("smid"),
		STID("stid"),
		ASSET_TYPE_ID("assetTypeId"),
		INSTALL_DEPT_NAME("installDeptName"),
		CHECKED_DATE("checkedDate"),
		ASSET_STATUS("assetStatus"),
		UPDATE_DATE("updateDate"),
		ROWID("rowId"),
		IS_SAME_PLACE_INSTALL("isSamePlaceInstall"),
		INSTALLP_RICE("installPrice"),
		FIRST_INSTALL("firstInstall"),
		FIRST_UNINSTALL("firstUnInstall"),
		PERIPHERALS_UPDATE("peripheralsUpdate"),
		ROW_INDEX("rowIndex"),
		SIM_CARD_PRICE("simCardPrice"),
		COMPLETE_DEPARTMENT_ID("completeDepartmentId"),
		COMPLETE_DEPARTMENT_NAME("completeDepartmentName"),
		AE_MID("aeMid"),
		AE_TID("aeTid"),
		HAS_ONLINE_EXCLUSION("hasOnlineExclusion"),
		IS_NEW_CASE("isNewCase"),
		FEE_PRICE("feePrice"),
		ECR_LINE_IN_FEE("ecrLineInFee"),
		NET_LINE_IN_FEE("netLineInFee"),
		INSTALLED_ADRESS_LOCATION_NAME("installedAdressLocationName"),
		CONTACT_ADDRESS_LOCATION_NAME("contactAddressLocationName"),
		VENDOR_SERVICE_CUSTOMER("vendorServiceCustomer"),
		FIRST_DESCRIPTION("firstDescription"),
		SECOND_DESCRIPTION("secondDescription"),
		THIRD_DESCRIPTION("thirdDescription"),
		//Task #3089
		MAIN_REQUIREMENT_NO("mainRequirementNo"),
		//Task #3110
		HAS_RETREAT("hasRetreat"),
		// Task #3205 是否執行過延期
		HAS_DELAY("hasDelay"),
		CASE_AREA("caseArea"),
		IS_BUSSINESS_CONTACT_MOBILE_PHONE("isBussinessContactMobilePhone"),
		IS_BUSSINESS_CONTACT_EMAIL("isBussinessContactEmail"),
		INSTALLED_CONTACT_MOBILE_PHONE("installedContactMobilePhone"),
		INSTALLED_CONTACT_EMAIL("installedContactEmail"),
		CONTACT_IS_BUSSINESS_CONTACT_MOBILE_PHONE("contactIsBussinessContactMobilePhone"),
		CONTACT_IS_BUSSINESS_CONTACT_EMAIL("contactIsBussinessContactEmail"),
		CONTACT_MOBILE_PHONE("contactMobilePhone"),
		CONTACT_USER_EMAIL("contactUserEmail"),
		CONTACT_EMAIL("contactEmail"),
		CMS_CASE("cmsCase"),
		UNITY_NUMBER("unityNumber"),
		CONFIRM_AUTHORIZES("confirmAuthorizes"),
		INSTALL_CASE_ID("installCaseId"),
		INSTALL_COMPLETE_DATE("installCompleteDate"),
		IS_IATOMS_CREATE_CMS("isIatomsCreateCms"),
		PRELOAD_SERIAL_NUMBER("preloadSerialNumber"),
		SIM_SERIAL_NUMBER("simSerialNumber"),
		INSTALLED_POST_CODE("installedPostCode"),
		CONTACT_POST_CODE("contactPostCode"),
		INSTALLED_POST_CODE_NAME("installedPostCodeName"),
		CONTACT_POST_CODE_NAME("contactPostCodeName"),
		LOCATION_POST_CODE("postCodeId"),
		CONTACT_POST_AREA("contactPostArea"),
		CONTACT_AREA_CODE("contactAreaCode"),
		INSTALL_CONTACT_AREA_CODE("installContactAreaCode"),
		INSTALL_CONTACT_AREA_NAME("installContactAreaName"),
		HAS_ARRIVE("hasArrive"),
		LOGISTICS_NUMBER("logisticsNumber"),
		LOGISTICS_VENDOR_NAME("logisticsVendorName"),
		LOGISTICS_VENDOR("logisticsVendor"),
		RECEIPT_TYPE("receiptType"),
		RECEIPT_TYPE_NAME("receiptTypeName"),
		CASETRANSACTION_PARAMETER_DTOS("caseTransactionParameterDTOs"),
		SOFTWARE_VERSIONS("softwareVersions");

		/**
		 * value 值
		 */
		private String value;
		
		/**
		 * Constructor:夠函數
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
	 * 裝機案件-郵遞區號
	 */
	private String installContactAreaCode;
	/**
	 * 裝機案件-郵遞區域
	 */
	private String installContactAreaName;
	/**
	 * “原裝機單位及人員
	 */
	private String oldInstalledDeptAndUser;
	/**
	 * 案件序號---案件匯入
	 */
	private String caseNo;
	/**
	 * 案件單號
	 */
	private String caseId;
	/**
	 * 重複進件caseId
	 */
	private String repeatCaseId;
	/**
	 * 案件種類 ex:新裝機,倂機等等
	 */
	private String caseCategory;
	/**
	 * 案件種類名稱
	 */
	private String caseCategoryName;
	/**
	 * 客戶編號
	 */
	private String customerId;  
	/**
	 * 客戶名稱
	 */
	private String customerName;
	/**
	 * 特店表頭名稱
	 */
	private String headerName;
	/**
	 * 合約ID
	 */
	private String contractId;
	/**
	 * 合約編號
	 */
	private String contractCode;
	/**
	 * tid
	 */
	private String tid;
	/**
	 * 需求單號
	 */
	private String requirementNo;
	/**
	 * 處理方式
	 */
	private String processType;
	/**
	 * 處理方式名稱
	 */
	private String processTypeName;
	/**
	 * 裝機類型
	 */
	private String installType;
	/**
	 * 裝機類型名稱
	 */
	private String installTypeName;
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
	 * 維護人員
	 */
	private String vendorStaff;
	/**
	 * 案件類型ex:急件/預約/一般
	 */
	private String caseType;
	/**
	 * 案件類型名稱
	 */
	private String caseTypeName;
	/**
	 * 預計完成日
	 */
	private Timestamp expectedCompletionDate;
	/**
	 * 特店代號
	 */
	private String merchantCode;
	/**
	 * 舊特店代號
	 */
	private String oldMerchantCode;
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
	 * 刷卡機類型
	 */
	private String edcTypeName;
	/**
	 * 軟體版本
	 */
	private String softwareVersionName;
	/**
	 * 內建功能
	 */
	private String builtInFeatureName;
	/**
	 * 雙模組模式
	 */
	private String multiModuleName;
	/**
	 * 週邊設備
	 */
	private String peripheralsName;
	/**
	 * 週邊設備功能
	 */
	private String peripheralsFunctionName;
	/**
	 * 週邊設備序號
	 */
	private String peripheralsSerialNumber;
	/**
	 * 週邊設備1合約編號
	 */
	private String peripheralsContractCode;
	/**
	 * ECR連接
	 */
	private String ecrConnectionName;
	/**
	 * 週邊設備2
	 */
	private String peripherals2Name;
	/**
	 * 週邊設備功能2
	 */
	private String peripheralsFunction2Name;
	/**
	 * 週邊設備2序號
	 */
	private String peripherals2SerialNumber;
	/**
	 * 週邊設備2合約編號
	 */
	private String peripherals2ContractCode;
	/**
	 * 通訊模式
	 */
	private String connectionTypeName;
	/**
	 * 週邊設備3
	 */
	private String peripherals3Name;
	/**
	 * 週邊設備功能3
	 */
	private String peripheralsFunction3Name;
	/**
	 * 週邊設備3序號
	 */
	private String peripherals3SerialNumber;
	/**
	 * 週邊設備3合約編號
	 */
	private String peripherals3ContractCode;
	/**
	 * 刷卡機類型
	 */
	private String edcType;
	/**
	 * 軟體版本
	 */
	private String softwareVersion;
	/**
	 * 內建功能
	 */
	private String builtInFeature;
	/**
	 * 雙模組模式
	 */
	private String multiModule;
	/**
	 * 週邊設備
	 */
	private String peripherals;
	/**
	 * 週邊設備功能
	 */
	private String peripheralsFunction;
	/**
	 * ECR連接
	 */
	private String ecrConnection;
	/**
	 * 週邊設備2
	 */
	private String peripherals2;
	/**
	 * 週邊設備功能2
	 */
	private String peripheralsFunction2;
	/**
	 * 通訊模式
	 */
	private String connectionType;
	/**
	 * 週邊設備3
	 */
	private String peripherals3;
	/**
	 * 週邊設備功能3
	 */
	private String peripheralsFunction3;
	/**
	 * 本機IP
	 */
	private String localhostIp;
	/**
	 * 寬頻連接
	 */
	private String netVendorId;
	/**
	 * 寬頻連接名稱
	 */
	private String netVendorName;
	/**
	 * Gateway
	 */
	private String gateway;
	/**
	 * Netmask
	 */
	private String netmask;
	/**
	 * 其他說明
	 */
	private String description;
	/**
	 * 到場次數
	 */
	private Integer attendanceTimes;
	/**
	 * 到場費用
	 */
	private Integer attendancePrice;
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
	 * 進件完工日
	 */
	private Timestamp createdFinishDate;
	/**
	 * 應回應時間
	 */
	private Timestamp acceptableResponseDate;
	/**
	 * 應到場時間
	 */
	private Timestamp acceptableArriveDate;
	/**
	 * 應完修時間
	 */
	private Timestamp acceptableFinishDate;
	/**
	 * 結案時間
	 */
	private Timestamp closeDate;
	
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
	 * 是否vip
	 */
	private String isVip;
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
	 * 專案名稱
	 */
	private String projectName;
	/**
	 * 專案代碼
	 */
	private String projectCode;
	/**
	 * 是否專案
	 */
	private String isProject = "N";
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
	 * 最新一筆案件交易參數
	 */
	private List<SrmCaseNewTransactionParameterDTO> caseNewTransactionParameterDTOs;
	/**
	 * 交易參數的json字符串
	 */
	private String caseTransactionParameterStr;
	/**
	 * 複製案件筆數
	 */
	private String caseNumber;
	
	/**
	 * 是否選擇單筆
	 */
	private Boolean isSingle;
	
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
	 * 接收人地址
	 */
	private String toMail;
	/**
	 * 抄送人員地址
	 */
	private String ccMail;
	/**
	 * 接收人名称
	 */
	private String toName;
	
	/**
	 * 发件人地址
	 */
	private String fromMail;
	
	/**
	 * 发件人名称
	 */
	private String fromName;
	
	/**
	 * 邮件主题
	 */
	private String mailSubject;
	
	/**
	 * 邮件内容
	 */
	private String mailContext;
	
	/**
	 * 邮件内容
	 */
	private String mailContext1;
	
	/**
	 * 邮件内容
	 */
	private String mailContext2;
	/**
	 * 有交易參數
	 */
	private String haveTransParameter;
	/**
	 * 案件處理記錄列表
	 */
	private List<SrmCaseTransactionDTO> caseTransactionDTOs;
	/**
	 * 案件處理記錄json字符串
	 */
	private String caseTransactionStr;
	/**
	 * 有案件處理記錄
	 */
	private String haveCaseTransaction;
	/**
	 * 進件數量
	 */
	private Integer caseCount;
	/**
	 * 結案數量
	 */
	private Integer closeCount;
	/**
	 * 回應逾時數量
	 */
	private Integer responseCount;
	/**
	 * 回應逾時比率
	 */
	private String responsePer;
	/**
	 * 完修逾時
	 */
	private Integer finishCount;
	/**
	 * 完修逾時比率
	 */
	private String finishPer;
	/**
	 * 廠商工程部門代碼
	 */
	private String dispatchDeptId;
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
	 * 派工人員代碼
	 */
	private String dispatchProcessUser;
	/**
	 * 派工人員姓名
	 */
	private String dispatchProcessUsername;
	/**
	 * 執行派工人員
	 */
	private String dispatchUser;
	/**
	 * 執行派工人員姓名
	 */
	private String dispatchUserName;
	/**
	 * 派件動作日期
	 */
	private Timestamp dispatchDate;
	/**
	 * 回應動作人員 ID
	 */
	private String responseUser;
	/**
	 * 回應動作人員姓名
	 */
	private String responseUserName;
	/**
	 * 回應動作日期
	 */
	private Timestamp responseDate;
	/**
	 * 到場動作人員 ID
	 */
	private String arriveUser;
	/**
	 * 到場動作人員姓名
	 */
	private String arriveUserName;
	/**
	 * 到場動作日期
	 */
	private Timestamp arriveDate;
	/**
	 * 完修動作人員 ID
	 */
	private String completeUser;
	/**
	 * 完修動作人員姓名
	 */
	private String completeUserName;
	/**
	 * 完修動作日期
	 */
	private Timestamp completeDate;
	/**
	 * 簽收人員ID
	 */
	private String analyzeUser;
	/**
	 * 簽收人員姓名
	 */
	private String analyzeUserName;
	/**
	 * 簽收動作日期
	 */
	private Timestamp analyzeDate;
	/**
	 * 結案者代號
	 */
	private String closeUser;
	/**
	 * 結案者姓名
	 */
	private String closeUserName;
	/**
	 * 裝機部門
	 */
	private String installedDeptId;
	/**
	 * 裝機人員
	 */
	private String installedUser;
	/**
	 * 派送到TMS
	 */
	private String isTms = "N";
	/**
	 * 報修問題原因
	 */
	private String problemReason;
	/**
	 * 報修問題原因名稱
	 */
	private String problemReasonName;
	/**
	 * 報修問題原因分類名稱
	 */
	private String problemReasonCategoryName;
	/**
	 * 報修解決方式
	 */
	private String problemSolution;
	/**
	 * 報修解決方式名稱
	 */
	private String problemSolutionName;
	/**
	 * 報修解決方式分類名稱
	 */
	private String problemSolutionCategoryName;
	/**
	 * 責任歸屬
	 */
	private String responsibity;
	/**
	 * 責任歸屬名稱
	 */
	private String responsibityName;
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
	 * 回應狀態
	 */
	private String responseStatus;
	/**
	 * 到場狀態
	 */
	private String arriveStatus;
	/**
	 * 完修狀態
	 */
	private String completeStatus;
	/**
	 * 案件處理狀態
	 */
	private String caseDealStatus;
	/**
	 * 廠商名稱
	 */
	private String vendorName;
	/**
	 * 倉別
	 */
	private String wareHouseId;
	/**
	 * 倉別名稱
	 */
	private String wareHouseName;
	/**
	 * 報修地址
	 */
	private String repairAdress;
	/**
	 * 刷卡機合約編號
	 */
	private String edcTypeContract;
	/**
	 * 週邊設備1合約編號
	 */
	private String peripheralsContract;
	/**
	 * 週邊設備2合約編號
	 */
	private String peripherals2Contract;
	/**
	 * 週邊設備3合約編號
	 */
	private String peripherals3Contract;
	/**
	 * 設備開啟的功能
	 */
	private String openFunctionName;
	/**
	 * CUP交易類別
	 */
	private String cupTransType;
	/**
	 * DCC交易類別
	 */
	private String dccTransType;
	/**
	 * 實際處理日期
	 */
	private Timestamp dealDate;
	/**
	 * 	處理說明
	 */
	private String transactionDescription;
	/**
	 * 回應警示時效
	 */
	private Double responseWarnning;
	/**
	 * 到場警示時效
	 */
	private Double arriveWarnning;
	/**
	 * 完修警示時效
	 */
	private Double completeWarnning;
	/**
	 * 回應警示開始時效
	 */
	private Timestamp responseWarnDate;
	/**
	 * 到場警示開始時效
	 */
	private Timestamp arriveWarnDate;
	/**
	 * 完修警示開始時效
	 */
	private Timestamp completeWarnDate;
	/**
	 * 網絡線數量
	 */
	private String networkLineNumber;
	/**
	 * 催修
	 */
	private String rushRepair;
	/**
	 * 回應情況
	 */
	private String responseCondition;
	/**
	 * 到場情況
	 */
	private String arriveCondition;
	/**
	 * 完修情況
	 */
	private String completeCondition;
	
	/**
	 *  案件處理中設備支援功能檔DTO集合
	 */
	private List<SrmCaseAssetFunctionDTO> srmCaseAssetFunctionDTOs;
	
	/**
	 * 案件處理中設備链接档DTO集合
	 */
	private List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOs;
	
	/**
	 * 案件處理案件記錄DTO資料
	 */
	private SrmCaseTransactionDTO srmCaseTransactionDTO;
	
	/**
	 * 案件附加檔案
	 */
	private List<SrmCaseAttFileDTO> caseAttFileDTOs;
	
	/**
	 * 案件交易參數
	 */
	private List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs;
	
	/**
	 * 案件建案時循環的參數
	 */
	private Integer caseLoopPrams;
	
	/**
	 * 計算dtid得到的集合
	 */
	private Map<Integer, String> dtidMap;
	
	/**
	 * 客戶碼
	 */
	private String customerCode;
	/**
	 * 
	 */
	private ContractSlaDTO contractSlaDTO;
	/**
	 * 裝機日期
	 */
	private Timestamp installedDate;
	/**
	 * 拆機日期
	 */
	private Timestamp uninstalledDate;
	/**
	 * CUP啟用日
	 */
	private Timestamp cupEnableDate;
	/**
	 * CUP移除日
	 */
	private Timestamp cupDisableDate;
	/**
	 * 異動項目
	 */
	private String updateItem;
	/**
	 * 裝機人員姓名
	 */
	private String installedUserName;
	/**
	 * 開放交易集合
	 */
	private String openTransactionList;
	/**
	 * 交叉報表集合
	 */
	private List<CrossTabReportDTO> crossTabReportDTOs;
	/**
	 * 基本參數配置集合
	 */
	private List<SrmTransactionParameterItemDTO> srmTransactionParameterItemDTOs;
	/**
	 * 是否查歷史
	 */
	private String isHistory;
	/**
	 * 頁面所輸特店代號
	 */
	private String merMid;
	/**
	 * 問題原因code
	 */
	private String problemReasonCode;
	/**
	 * 問題解決方式code
	 */
	private String problemSolutionCode;
	
	/**
	 * sla時效保存集合
	 */
	private Map<String, Date> dateMap;
	
	/**
	 * 電子發票載具
	 */
	private String electronicInvoice;
	
	/**
	 * 銀聯閃付
	 */
	private String cupQuickPass;
	
	/**
	 * 派工單位名稱
	 */
	private String dispatchDeptName;
	/**
	 * 
	 */
	private List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs;
	/**
	 * 財產編號
	 */
	private String propertyId;
	/**
	 * 是否同裝機作業
	 */
	private String sameInstalled;
	/**
	 * TMS參數說明
	 */
	private String tmsParamDesc;
	/**
	 * 刷卡機設備序號
	 */
	private String edcSerialNumber;
	/**
	 * 設備開啟功能清單
	 */
	private String functionTypeList;
	/**
	 * 到場說明
	 */
	private String attendanceDesc;
	/**
	 * 延期說明
	 */
	private String rushRepairDesc;
	/**
	 * ECR線
	 */
	private String ecrLine;
	/**
	 * ECR線 維護費設備
	 */
	private Integer ecrLineInFee;
	/**
	 * ECR線(未稅)
	 */
	private Integer ecrLineNoTax;
	/**
	 * 網路線
	 */
	private String netLine;
	/**
	 * 網路線 維護費設備
	 */
	private Integer netLineInFee;
	/**
	 * 網路線(未稅)
	 */
	private Integer netLineNoTax;
	/**
	 * 耗材品項、個數、費用
	 */
	private String otherLine;
	/**
	 * 是否首裝
	 */
	private String isFirstInstalled = "N";
	/**
	 * 非首裝費
	 */
	private Integer notFirstInstalledPrice;
	/**
	 * 非首裝費(未稅)
	 */
	private Integer notFirstInstalledPriceNoTax;
	/**
	 * 週邊異動
	 */
	private String peripheralsIsUpdate;
	/**
	 * POS機成本
	 */
	private String posPrice;
	/**
	 * POS機成本
	 */
	private Integer posPriceInFee;
	/**
	 * 開始日期
	 */
	private String startDate;
	/**
	 * 結束日期
	 */
	private String endDate;
	/**
	 * 特店地址
	 */
	private String bussionAddress;
	/**
	 * 裝機地址
	 */
	private String installedAddress;
	/**
	 * 拆機地址
	 */
	private String unInstalledAddress;
	/**
	 * 拆機維護廠商
	 */
	private String unInstalledCompanyName;
	/**
	 * 完修逾期時間
	 */
	private BigDecimal delayTime;
	/**
	 * 裝機-拆機是否未滿三個月
	 */
	private String userdDays90;
	/**
	 * 裝機-拆機是否未滿三個月
	 */
	private Integer userdDays90Price;
	/**
	 *  裝機-拆機是否未滿四個月
	 */
	private String userdDays120;
	/**
	 *  拆機未滿四個月費用
	 */
	private Integer userdDays120Price;
	/**
	 * 是否開啟加密
	 */
	private String isOpenEncrypt;
	/**
	 * 電子化繳費平台
	 */
	private String electronicPayPlatform;
	/**
	 * LOGO
	 */
	private String logoStyle;
	/**
	 * 是否修改案件關聯的設備
	 */
	private String isUpdateAsset = "N";
	/**
	 * 設備ID
	 */
	private String assetTypeId;
	/**
	 * 舊資料銀聯交易標記
	 */
	private String unionPayFlag;
	/**
	 * 舊資料smartpay交易標記
	 */
	private String smartPayFlag;
	/**
	 * 舊資料特店代號
	 */
	private String smid;
	/**
	 * 舊資料tid
	 */
	private String stid;
	/**
	 * 求償作業-將依據DTID查詢的設備信息放入SrmPaymentItemDTO
	 */
	private List<SrmPaymentItemDTO> srmPaymentItemDTOs;
	/**
	 * 分期特店代號
	 */
	private String merchantCodeOther;
	/**
	 * foms案件
	 */
	private String fomsCase = "N";
	/**
	 * 裝機部門名稱
	 */
	private String installDeptName;
	/**
	 * 感應交易
	 */
	private String enableClss;
	/**
	 * 外接PINPAD
	 */
	private String pinpadExtern;
	/**
	 * 驗收日期
	 */
	private String checkedDate;
	/**
	 * 設備狀態
	 */
	private String assetStatus;
	/**
	 * 異動時間
	 */
	private Timestamp updateDate;
	/**
	 * 维护费(含税)
	 */
	private String fee;
	/**
	 * 维护费(含税)
	 */
	private Integer feePrice;
	/**
	 * sim門租月會費
	 */
	private Integer simCardPrice;
	/**
	 * 匯出報表的rowId
	 */
	private String rowId;
	/**
	 * 急件/特急件費用
	 */
	private Integer dispatchPrice;
	/**
	 * 急件/特急件費用(未稅)
	 */
	private Integer dispatchPriceNoTax;
	/**
	 * 裝機作業費
	 */
	private Integer installPrice;
	/**
	 * 拆機作業費
	 */
	private Integer unInstallPrice;
	/**
	 * 拆機作業費未稅
	 */
	private Integer unInstallPriceNoTax;
	/**
	 * 設定費
	 */
	private Integer settingPrice;
	/**
	 * 設定費未稅
	 */
	private Integer settingPriceNoTax;
	/**
	 * 裝 並 移費用總計
	 */
	private Integer installedPriceSum;
	/**
	 * 非首裝費用總計
	 */
	private Integer notFirstInstalledPriceSum;
	/**
	 * 非首裝費用總計(未稅)
	 */
	private Integer notFirstInstalledNoTaxPriceSum;
	/**
	 * 拆機作業費 總計
	 */
	private Integer unInstallPriceSum;
	/**
	 * 拆機作業費 總計(未稅)
	 */
	private Integer unInstallPriceNoTaxSum;
	/**
	 * 設定費 總計
	 */
	private Integer settingPriceSum;
	/**
	 * 設定費 總計(未稅)
	 */
	private Integer settingPriceNoTaxSum;
	/**
	 * 急件 特急件 總計
	 */
	private Integer fastSum;
	/**
	 * 急件 特急件 總計(未稅)
	 */
	private Integer fastNoTaxSum;
	/**
	 * Pos費用總計
	 */
	private Integer posSum0;
	/**
	 * ECR線材總費用
	 */
	private Integer ecrlineSumInCase;
	/**
	 * ECR線材總費用(未稅)
	 */
	private Integer ecrlineNoTaxSumInCase;
	/**
	 * 網路線費用總和
	 */
	private Integer networkRouteSumInCase;
	/**
	 * 網路線費用總和(未稅)
	 */
	private Integer networkRouteNoTaxSumInCase;
	/**
	 * 備註
	 */
	private String remark;
	/**
	 * 急件/特急件費用
	 */
	private Integer fastFee;
	/**
	 * 周邊異動
	 */
	private String peripheralsUpdate;
	/**
	 * 是否同一地安裝
	 */
	private String isSamePlaceInstall;
	/**
	 * 首裝
	 */
	private String firstInstall;
	/**
	 * 首裝
	 */
	private String firstUnInstall;
	/**
	 * 客戶的code
	 */
	private String companyCode;
	/**
	 * 序號
	 */
	private Integer rowIndex;
	/**
	 * 子報表1list
	 */
	private List<WorkFeeSetting> workFeeSettingList;
	/**
	 * 執行完修部門
	 */
	private String completeDepartmentId;
	/**
	 * 執行完修部門名稱
	 */
	private String completeDepartmentName;
	/**
	 * AE-MID
	 */
	private String aeMid;
	/**
	 * AE-TID
	 */
	private String aeTid;
	/**
	 * 是工單範本
	 */
	private String isCaseTemplate;
	/**
	 * 案件動作類型
	 */
	private String hasOnlineExclusion = "N";
	/**
	 * 是否為最新案件
	 */
	private String isNewCase;
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
	 * 建案廠商客服公司
	 */
	private String vendorServiceCustomer;
	/**
	 * 退回标志位--Task #3110
	 */
	private String hasRetreat = "N";
	/**
	 * 案件區域
	 */
	private String caseArea;
	
	/**
	 * 是否執行過延期
	 */
	private String hasDelay = "N";
	/**
	 * 是否同特店聯絡人行動電話
	 */
	private String isBussinessContactMobilePhone = "E";
	/**
	 * 是否同特店聯絡人Email
	 */
	private String isBussinessContactEmail = "E";
	/**
	 * 裝機連絡人手機
	 */
	private String installedContactMobilePhone;
	/**
	 * 裝機連絡人EMAIL
	 */
	private String installedContactEmail;
	/**
	 * 是否同特店聯絡人行動電話
	 */
	private String contactIsBussinessContactMobilePhone = "E";
	/**
	 * 是否同特店聯絡人Email
	 */
	private String contactIsBussinessContactEmail = "E";
	/**
	 * 聯繫連絡人手機
	 */
	private String contactMobilePhone;
	/**
	 * 聯繫連絡人EMAIL
	 */
	private String contactUserEmail;
	/**
	 * 特店聯系人Email
	 */
	private String contactEmail;
	/**
	 * 是否爲cms案件
	 */
	private String cmsCase = "N";
	/**
	 * 統一編碼（CMS使用）
	 */
	private String unityNumber;
	/**
	 * 確認授權
	 */
	private String confirmAuthorizes = "N";
	/**
	 * 裝機案件編號
	 */
	private String installCaseId;
	/**
	 * 裝機完修日
	 */
	private Timestamp installCompleteDate;
	/**
	 * 是否爲IAtoms建立的cms案件
	 */
	private String isIatomsCreateCms = "N";
	/**
	 * 租賃預載輸入的設備序號
	 */
	private String preloadSerialNumber;
	/**
	 * 設備序號(SIM卡)
	 */
	private String simSerialNumber;
	/**
	 * 裝機郵遞區號
	 */
	private String installedPostCode;
	/**
	 * 聯系郵遞區號
	 */
	private String contactPostCode;
	/**
	 * 裝機郵遞區號
	 */
	private String installedPostCodeName;
	/**
	 * 聯系郵遞區號
	 */
	private String contactPostCodeName;
	/**
	 * 
	 */
	private String postCodeId;
	/**
	 * contactPostArea
	 */
	private String contactPostArea;
	/**
	 * 
	 */
	private String contactAreaCode;
	
	/**
	 * 記錄cms返回成功信息
	 */
	private String cmsResultMsg;
	/**
	 * api案件歷程id
	 */
	private String apiTransactionId;
	/**
	 * 到場註記--Task #3548
	 */
	private String hasArrive = "N";
	/**
	 * 物流廠商名稱
	 */
	private String logisticsVendorName;
	/**
	 * 物流廠商
	 */
	private String logisticsVendor;
	/**
	 * 物流編號
	 */
	private String logisticsNumber;
	/**
	 * Receipt_type
	 */
	private String receiptType;
	
	/**
	 * Receipt_type中文
	 */
	private String receiptTypeName;
	
	/**
	 * 軟體版本
	 */
	private List<Parameter> softwareVersions; 
	
	/**
	 * Constructor:無參構造函數
	 */
	public SrmCaseHandleInfoDTO() {
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
	 * @return the processType
	 */
	public String getProcessType() {
		return processType;
	}

	/**
	 * @param processType the processType to set
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
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
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
	 * @return the expectedCompletionDate
	 */
	public Timestamp getExpectedCompletionDate() {
		return expectedCompletionDate;
	}

	/**
	 * @param expectedCompletionDate the expectedCompletionDate to set
	 */
	public void setExpectedCompletionDate(Timestamp expectedCompletionDate) {
		this.expectedCompletionDate = expectedCompletionDate;
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
	 * @return the edcType
	 */
	public String getEdcType() {
		return edcType;
	}

	/**
	 * @param edcType the edcType to set
	 */
	public void setEdcType(String edcType) {
		this.edcType = edcType;
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
	 * @return the builtInFeature
	 */
	public String getBuiltInFeature() {
		return builtInFeature;
	}

	/**
	 * @param builtInFeature the builtInFeature to set
	 */
	public void setBuiltInFeature(String builtInFeature) {
		this.builtInFeature = builtInFeature;
	}

	/**
	 * @return the multiModule
	 */
	public String getMultiModule() {
		return multiModule;
	}

	/**
	 * @param multiModule the multiModule to set
	 */
	public void setMultiModule(String multiModule) {
		this.multiModule = multiModule;
	}

	/**
	 * @return the peripherals
	 */
	public String getPeripherals() {
		return peripherals;
	}

	/**
	 * @param peripherals the peripherals to set
	 */
	public void setPeripherals(String peripherals) {
		this.peripherals = peripherals;
	}

	/**
	 * @return the peripheralsFunction
	 */
	public String getPeripheralsFunction() {
		return peripheralsFunction;
	}

	/**
	 * @param peripheralsFunction the peripheralsFunction to set
	 */
	public void setPeripheralsFunction(String peripheralsFunction) {
		this.peripheralsFunction = peripheralsFunction;
	}

	/**
	 * @return the ecrConnection
	 */
	public String getEcrConnection() {
		return ecrConnection;
	}

	/**
	 * @param ecrConnection the ecrConnection to set
	 */
	public void setEcrConnection(String ecrConnection) {
		this.ecrConnection = ecrConnection;
	}

	/**
	 * @return the peripherals2
	 */
	public String getPeripherals2() {
		return peripherals2;
	}

	/**
	 * @param peripherals2 the peripherals2 to set
	 */
	public void setPeripherals2(String peripherals2) {
		this.peripherals2 = peripherals2;
	}

	/**
	 * @return the peripheralsFunction2
	 */
	public String getPeripheralsFunction2() {
		return peripheralsFunction2;
	}

	/**
	 * @param peripheralsFunction2 the peripheralsFunction2 to set
	 */
	public void setPeripheralsFunction2(String peripheralsFunction2) {
		this.peripheralsFunction2 = peripheralsFunction2;
	}

	/**
	 * @return the connectionType
	 */
	public String getConnectionType() {
		return connectionType;
	}

	/**
	 * @param connectionType the connectionType to set
	 */
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	/**
	 * @return the peripherals3
	 */
	public String getPeripherals3() {
		return peripherals3;
	}

	/**
	 * @param peripherals3 the peripherals3 to set
	 */
	public void setPeripherals3(String peripherals3) {
		this.peripherals3 = peripherals3;
	}

	/**
	 * @return the peripheralsFunction3
	 */
	public String getPeripheralsFunction3() {
		return peripheralsFunction3;
	}

	/**
	 * @param peripheralsFunction3 the peripheralsFunction3 to set
	 */
	public void setPeripheralsFunction3(String peripheralsFunction3) {
		this.peripheralsFunction3 = peripheralsFunction3;
	}

	/**
	 * @return the localhostIp
	 */
	public String getLocalhostIp() {
		return localhostIp;
	}

	/**
	 * @param localhostIp the localhostIp to set
	 */
	public void setLocalhostIp(String localhostIp) {
		this.localhostIp = localhostIp;
	}

	/**
	 * @return the netVendorId
	 */
	public String getNetVendorId() {
		return netVendorId;
	}

	/**
	 * @param netVendorId the netVendorId to set
	 */
	public void setNetVendorId(String netVendorId) {
		this.netVendorId = netVendorId;
	}

	/**
	 * @return the gateway
	 */
	public String getGateway() {
		return gateway;
	}

	/**
	 * @param gateway the gateway to set
	 */
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	/**
	 * @return the netmask
	 */
	public String getNetmask() {
		return netmask;
	}

	/**
	 * @param netmask the netmask to set
	 */
	public void setNetmask(String netmask) {
		this.netmask = netmask;
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
	 * @return the attendanceTimes
	 */
	public Integer getAttendanceTimes() {
		return attendanceTimes;
	}

	/**
	 * @param attendanceTimes the attendanceTimes to set
	 */
	public void setAttendanceTimes(Integer attendanceTimes) {
		this.attendanceTimes = attendanceTimes;
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
	 * @return the uninstalledAdress
	 */
/*	public String getUninstalledAdress() {
		return uninstalledAdress;
	}*/

	/**
	 * @param uninstalledAdress the uninstalledAdress to set
	 */
	/*public void setUninstalledAdress(String uninstalledAdress) {
		this.uninstalledAdress = uninstalledAdress;
	}
*/
	/**
	 * @return the uninstalledIsBussinessAddress
	 */
	/*public String getUninstalledIsBussinessAddress() {
		return uninstalledIsBussinessAddress;
	}*/

	/**
	 * @param uninstalledIsBussinessAddress the uninstalledIsBussinessAddress to set
	 */
	/*public void setUninstalledIsBussinessAddress(
			String uninstalledIsBussinessAddress) {
		this.uninstalledIsBussinessAddress = uninstalledIsBussinessAddress;
	}*/

	/**
	 * @return the uninstalledContact
	 */
	/*public String getUninstalledContact() {
		return uninstalledContact;
	}*/

	/**
	 * @param uninstalledContact the uninstalledContact to set
	 */
	/*public void setUninstalledContact(String uninstalledContact) {
		this.uninstalledContact = uninstalledContact;
	}*/

	/**
	 * @return the uninstalledIsBussinessContact
	 */
	/*public String getUninstalledIsBussinessContact() {
		return uninstalledIsBussinessContact;
	}*/

	/**
	 * @param uninstalledIsBussinessContact the uninstalledIsBussinessContact to set
	 */
	/*public void setUninstalledIsBussinessContact(
			String uninstalledIsBussinessContact) {
		this.uninstalledIsBussinessContact = uninstalledIsBussinessContact;
	}*/

	/**
	 * @return the uninstalledContactPhone
	 */
	/*public String getUninstalledContactPhone() {
		return uninstalledContactPhone;
	}*/

	/**
	 * @param uninstalledContactPhone the uninstalledContactPhone to set
	 */
	/*public void setUninstalledContactPhone(String uninstalledContactPhone) {
		this.uninstalledContactPhone = uninstalledContactPhone;
	}*/

	/**
	 * @return the uninstalledIsBussinessPhone
	 */
	/*public String getUninstalledIsBussinessPhone() {
		return uninstalledIsBussinessPhone;
	}*/

	/**
	 * @param uninstalledIsBussinessPhone the uninstalledIsBussinessPhone to set
	 */
	/*public void setUninstalledIsBussinessPhone(String uninstalledIsBussinessPhone) {
		this.uninstalledIsBussinessPhone = uninstalledIsBussinessPhone;
	}*/

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
	 * @return the createdFinishDate
	 */
	public Timestamp getCreatedFinishDate() {
		return createdFinishDate;
	}

	/**
	 * @param createdFinishDate the createdFinishDate to set
	 */
	public void setCreatedFinishDate(Timestamp createdFinishDate) {
		this.createdFinishDate = createdFinishDate;
	}

	/**
	 * @return the acceptableResponseDate
	 */
	public Timestamp getAcceptableResponseDate() {
		return acceptableResponseDate;
	}

	/**
	 * @param acceptableResponseDate the acceptableResponseDate to set
	 */
	public void setAcceptableResponseDate(Timestamp acceptableResponseDate) {
		this.acceptableResponseDate = acceptableResponseDate;
	}

	/**
	 * @return the acceptableArriveDate
	 */
	public Timestamp getAcceptableArriveDate() {
		return acceptableArriveDate;
	}

	/**
	 * @param acceptableArriveDate the acceptableArriveDate to set
	 */
	public void setAcceptableArriveDate(Timestamp acceptableArriveDate) {
		this.acceptableArriveDate = acceptableArriveDate;
	}

	/**
	 * @return the acceptableFinishDate
	 */
	public Timestamp getAcceptableFinishDate() {
		return acceptableFinishDate;
	}

	/**
	 * @param acceptableFinishDate the acceptableFinishDate to set
	 */
	public void setAcceptableFinishDate(Timestamp acceptableFinishDate) {
		this.acceptableFinishDate = acceptableFinishDate;
	}

	/**
	 * @return the closeDate
	 */
	public Timestamp getCloseDate() {
		return closeDate;
	}

	/**
	 * @param closeDate the closeDate to set
	 */
	public void setCloseDate(Timestamp closeDate) {
		this.closeDate = closeDate;
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
	 * @return the caseAttFileDTOs
	 */
	public List<SrmCaseAttFileDTO> getCaseAttFileDTOs() {
		return caseAttFileDTOs;
	}

	/**
	 * @param caseAttFileDTOs the caseAttFileDTOs to set
	 */
	public void setCaseAttFileDTOs(List<SrmCaseAttFileDTO> caseAttFileDTOs) {
		this.caseAttFileDTOs = caseAttFileDTOs;
	}

	/**
	 * @return the caseNewTransactionParameterDTOs
	 */
	public List<SrmCaseNewTransactionParameterDTO> getCaseNewTransactionParameterDTOs() {
		return caseNewTransactionParameterDTOs;
	}

	/**
	 * @param caseNewTransactionParameterDTOs the caseNewTransactionParameterDTOs to set
	 */
	public void setCaseNewTransactionParameterDTOs(
			List<SrmCaseNewTransactionParameterDTO> caseNewTransactionParameterDTOs) {
		this.caseNewTransactionParameterDTOs = caseNewTransactionParameterDTOs;
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
	 * @return the isVip
	 */
	public String getIsVip() {
		return isVip;
	}

	/**
	 * @param isVip the isVip to set
	 */
	public void setIsVip(String isVip) {
		this.isVip = isVip;
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
	 * @return the edcTypeName
	 */
	public String getEdcTypeName() {
		return edcTypeName;
	}

	/**
	 * @param edcTypeName the edcTypeName to set
	 */
	public void setEdcTypeName(String edcTypeName) {
		this.edcTypeName = edcTypeName;
	}

	/**
	 * @return the softwareVersionName
	 */
	public String getSoftwareVersionName() {
		return softwareVersionName;
	}

	/**
	 * @param softwareVersionName the softwareVersionName to set
	 */
	public void setSoftwareVersionName(String softwareVersionName) {
		this.softwareVersionName = softwareVersionName;
	}

	/**
	 * @return the builtInFeatureName
	 */
	public String getBuiltInFeatureName() {
		return builtInFeatureName;
	}

	/**
	 * @param builtInFeatureName the builtInFeatureName to set
	 */
	public void setBuiltInFeatureName(String builtInFeatureName) {
		this.builtInFeatureName = builtInFeatureName;
	}

	/**
	 * @return the multiModuleName
	 */
	public String getMultiModuleName() {
		return multiModuleName;
	}

	/**
	 * @param multiModuleName the multiModuleName to set
	 */
	public void setMultiModuleName(String multiModuleName) {
		this.multiModuleName = multiModuleName;
	}

	/**
	 * @return the peripheralsName
	 */
	public String getPeripheralsName() {
		return peripheralsName;
	}

	/**
	 * @param peripheralsName the peripheralsName to set
	 */
	public void setPeripheralsName(String peripheralsName) {
		this.peripheralsName = peripheralsName;
	}

	/**
	 * @return the peripheralsFunctionName
	 */
	public String getPeripheralsFunctionName() {
		return peripheralsFunctionName;
	}

	/**
	 * @param peripheralsFunctionName the peripheralsFunctionName to set
	 */
	public void setPeripheralsFunctionName(String peripheralsFunctionName) {
		this.peripheralsFunctionName = peripheralsFunctionName;
	}

	/**
	 * @return the ecrConnectionName
	 */
	public String getEcrConnectionName() {
		return ecrConnectionName;
	}

	/**
	 * @param ecrConnectionName the ecrConnectionName to set
	 */
	public void setEcrConnectionName(String ecrConnectionName) {
		this.ecrConnectionName = ecrConnectionName;
	}

	/**
	 * @return the peripherals2Name
	 */
	public String getPeripherals2Name() {
		return peripherals2Name;
	}

	/**
	 * @param peripherals2Name the peripherals2Name to set
	 */
	public void setPeripherals2Name(String peripherals2Name) {
		this.peripherals2Name = peripherals2Name;
	}

	/**
	 * @return the peripheralsFunction2Name
	 */
	public String getPeripheralsFunction2Name() {
		return peripheralsFunction2Name;
	}

	/**
	 * @param peripheralsFunction2Name the peripheralsFunction2Name to set
	 */
	public void setPeripheralsFunction2Name(String peripheralsFunction2Name) {
		this.peripheralsFunction2Name = peripheralsFunction2Name;
	}

	/**
	 * @return the connectionTypeName
	 */
	public String getConnectionTypeName() {
		return connectionTypeName;
	}

	/**
	 * @param connectionTypeName the connectionTypeName to set
	 */
	public void setConnectionTypeName(String connectionTypeName) {
		this.connectionTypeName = connectionTypeName;
	}

	/**
	 * @return the peripherals3Name
	 */
	public String getPeripherals3Name() {
		return peripherals3Name;
	}

	/**
	 * @param peripherals3Name the peripherals3Name to set
	 */
	public void setPeripherals3Name(String peripherals3Name) {
		this.peripherals3Name = peripherals3Name;
	}

	/**
	 * @return the peripheralsFunction3Name
	 */
	public String getPeripheralsFunction3Name() {
		return peripheralsFunction3Name;
	}

	/**
	 * @param peripheralsFunction3Name the peripheralsFunction3Name to set
	 */
	public void setPeripheralsFunction3Name(String peripheralsFunction3Name) {
		this.peripheralsFunction3Name = peripheralsFunction3Name;
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
	 * @return the uninstalledAdressLocation
	 */
	/*public String getUninstalledAddressLocation() {
		return uninstalledAddressLocation;
	}*/

	/**
	 * @param uninstalledAdressLocation the uninstalledAdressLocation to set
	 */
	/*public void setUninstalledAddressLocation(String uninstalledAddressLocation) {
		this.uninstalledAddressLocation = uninstalledAddressLocation;
	}*/

	/**
	 * @return the netVendorName
	 */
	public String getNetVendorName() {
		return netVendorName;
	}

	/**
	 * @param netVendorName the netVendorName to set
	 */
	public void setNetVendorName(String netVendorName) {
		this.netVendorName = netVendorName;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the projectCode
	 */
	public String getProjectCode() {
		return projectCode;
	}

	/**
	 * @param projectCode the projectCode to set
	 */
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
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
	 * @return the isProject
	 */
	public String getIsProject() {
		return isProject;
	}

	/**
	 * @param isProject the isProject to set
	 */
	public void setIsProject(String isProject) {
		this.isProject = isProject;
	}

	/**
	 * @return the caseNumber
	 */
	public String getCaseNumber() {
		return caseNumber;
	}

	/**
	 * @param caseNumber the caseNumber to set
	 */
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	/**
	 * @return the isSingle
	 */
	public Boolean getIsSingle() {
		return isSingle;
	}

	/**
	 * @param isSingle the isSingle to set
	 */
	public void setIsSingle(Boolean isSingle) {
		this.isSingle = isSingle;
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
	 * @return the installTypeName
	 */
	public String getInstallTypeName() {
		return installTypeName;
	}

	/**
	 * @param installTypeName the installTypeName to set
	 */
	public void setInstallTypeName(String installTypeName) {
		this.installTypeName = installTypeName;
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
	 * @return the caseCategoryName
	 */
	public String getCaseCategoryName() {
		return caseCategoryName;
	}

	/**
	 * @param caseCategoryName the caseCategoryName to set
	 */
	public void setCaseCategoryName(String caseCategoryName) {
		this.caseCategoryName = caseCategoryName;
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
	 * @return the peripheralsSerialNumber
	 */
	public String getPeripheralsSerialNumber() {
		return peripheralsSerialNumber;
	}

	/**
	 * @param peripheralsSerialNumber the peripheralsSerialNumber to set
	 */
	public void setPeripheralsSerialNumber(String peripheralsSerialNumber) {
		this.peripheralsSerialNumber = peripheralsSerialNumber;
	}

	/**
	 * @return the peripherals2SerialNumber
	 */
	public String getPeripherals2SerialNumber() {
		return peripherals2SerialNumber;
	}

	/**
	 * @param peripherals2SerialNumber the peripherals2SerialNumber to set
	 */
	public void setPeripherals2SerialNumber(String peripherals2SerialNumber) {
		this.peripherals2SerialNumber = peripherals2SerialNumber;
	}

	/**
	 * @return the peripherals3SerialNumber
	 */
	public String getPeripherals3SerialNumber() {
		return peripherals3SerialNumber;
	}

	/**
	 * @param peripherals3SerialNumber the peripherals3SerialNumber to set
	 */
	public void setPeripherals3SerialNumber(String peripherals3SerialNumber) {
		this.peripherals3SerialNumber = peripherals3SerialNumber;
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
	 * @return the toMail
	 */
	public String getToMail() {
		return toMail;
	}

	/**
	 * @param toMail the toMail to set
	 */
	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	/**
	 * @return the toName
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * @param toName the toName to set
	 */
	public void setToName(String toName) {
		this.toName = toName;
	}

	/**
	 * @return the fromMail
	 */
	public String getFromMail() {
		return fromMail;
	}

	/**
	 * @param fromMail the fromMail to set
	 */
	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @param fromName the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * @return the mailSubject
	 */
	public String getMailSubject() {
		return mailSubject;
	}

	/**
	 * @param mailSubject the mailSubject to set
	 */
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	/**
	 * @return the mailContext
	 */
	public String getMailContext() {
		return mailContext;
	}

	/**
	 * @param mailContext the mailContext to set
	 */
	public void setMailContext(String mailContext) {
		this.mailContext = mailContext;
	}

	/**
	 * @return the mailContext1
	 */
	public String getMailContext1() {
		return mailContext1;
	}

	/**
	 * @param mailContext1 the mailContext1 to set
	 */
	public void setMailContext1(String mailContext1) {
		this.mailContext1 = mailContext1;
	}

	/**
	 * @return the mailContext2
	 */
	public String getMailContext2() {
		return mailContext2;
	}

	/**
	 * @param mailContext2 the mailContext2 to set
	 */
	public void setMailContext2(String mailContext2) {
		this.mailContext2 = mailContext2;
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
	 * @return the caseTransactionParameterStr
	 */
	public String getCaseTransactionParameterStr() {
		return caseTransactionParameterStr;
	}

	/**
	 * @param caseTransactionParameterStr the caseTransactionParameterStr to set
	 */
	public void setCaseTransactionParameterStr(String caseTransactionParameterStr) {
		this.caseTransactionParameterStr = caseTransactionParameterStr;
	}

	/**
	 * @return the caseTransactionParameterDTOs
	 */
	public List<SrmCaseTransactionParameterDTO> getCaseTransactionParameterDTOs() {
		return caseTransactionParameterDTOs;
	}

	/**
	 * @param caseTransactionParameterDTOs the caseTransactionParameterDTOs to set
	 */
	public void setCaseTransactionParameterDTOs(
			List<SrmCaseTransactionParameterDTO> caseTransactionParameterDTOs) {
		this.caseTransactionParameterDTOs = caseTransactionParameterDTOs;
	}

	/**
	 * @return the haveTransParameter
	 */
	public String getHaveTransParameter() {
		return haveTransParameter;
	}

	/**
	 * @param haveTransParameter the haveTransParameter to set
	 */
	public void setHaveTransParameter(String haveTransParameter) {
		this.haveTransParameter = haveTransParameter;
	}

	/**
	 * @return the caseTransactionDTOs
	 */
	public List<SrmCaseTransactionDTO> getCaseTransactionDTOs() {
		return caseTransactionDTOs;
	}

	/**
	 * @param caseTransactionDTOs the caseTransactionDTOs to set
	 */
	public void setCaseTransactionDTOs(
			List<SrmCaseTransactionDTO> caseTransactionDTOs) {
		this.caseTransactionDTOs = caseTransactionDTOs;
	}

	/**
	 * @return the caseTransactionStr
	 */
	public String getCaseTransactionStr() {
		return caseTransactionStr;
	}

	/**
	 * @param caseTransactionStr the caseTransactionStr to set
	 */
	public void setCaseTransactionStr(String caseTransactionStr) {
		this.caseTransactionStr = caseTransactionStr;
	}

	/**
	 * @return the haveCaseTransaction
	 */
	public String getHaveCaseTransaction() {
		return haveCaseTransaction;
	}

	/**
	 * @param haveCaseTransaction the haveCaseTransaction to set
	 */
	public void setHaveCaseTransaction(String haveCaseTransaction) {
		this.haveCaseTransaction = haveCaseTransaction;
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
	 * @return the dispatchDeptId
	 */
	public String getDispatchDeptId() {
		return dispatchDeptId;
	}

	/**
	 * @param dispatchDeptId the dispatchDeptId to set
	 */
	public void setDispatchDeptId(String dispatchDeptId) {
		this.dispatchDeptId = dispatchDeptId;
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
	 * @return the dispatchProcessUser
	 */
	public String getDispatchProcessUser() {
		return dispatchProcessUser;
	}

	/**
	 * @param dispatchProcessUser the dispatchProcessUser to set
	 */
	public void setDispatchProcessUser(String dispatchProcessUser) {
		this.dispatchProcessUser = dispatchProcessUser;
	}

	/**
	 * @return the dispatchProcessUsername
	 */
	public String getDispatchProcessUsername() {
		return dispatchProcessUsername;
	}

	/**
	 * @param dispatchProcessUsername the dispatchProcessUsername to set
	 */
	public void setDispatchProcessUsername(String dispatchProcessUsername) {
		this.dispatchProcessUsername = dispatchProcessUsername;
	}

	/**
	 * @return the dispatchUser
	 */
	public String getDispatchUser() {
		return dispatchUser;
	}

	/**
	 * @param dispatchUser the dispatchUser to set
	 */
	public void setDispatchUser(String dispatchUser) {
		this.dispatchUser = dispatchUser;
	}

	/**
	 * @return the dispatchUserName
	 */
	public String getDispatchUserName() {
		return dispatchUserName;
	}

	/**
	 * @param dispatchUserName the dispatchUserName to set
	 */
	public void setDispatchUserName(String dispatchUserName) {
		this.dispatchUserName = dispatchUserName;
	}

	/**
	 * @return the dispatchDate
	 */
	public Timestamp getDispatchDate() {
		return dispatchDate;
	}

	/**
	 * @param dispatchDate the dispatchDate to set
	 */
	public void setDispatchDate(Timestamp dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	/**
	 * @return the responseUser
	 */
	public String getResponseUser() {
		return responseUser;
	}

	/**
	 * @param responseUser the responseUser to set
	 */
	public void setResponseUser(String responseUser) {
		this.responseUser = responseUser;
	}

	/**
	 * @return the caseCount
	 */
	public Integer getCaseCount() {
		return caseCount;
	}

	/**
	 * @param caseCount the caseCount to set
	 */
	public void setCaseCount(Integer caseCount) {
		this.caseCount = caseCount;
	}

	/**
	 * @return the closeCount
	 */
	public Integer getCloseCount() {
		return closeCount;
	}

	/**
	 * @param closeCount the closeCount to set
	 */
	public void setCloseCount(Integer closeCount) {
		this.closeCount = closeCount;
	}

	/**
	 * @return the responseCount
	 */
	public Integer getResponseCount() {
		return responseCount;
	}

	/**
	 * @param responseCount the responseCount to set
	 */
	public void setResponseCount(Integer responseCount) {
		this.responseCount = responseCount;
	}

	/**
	 * @return the responsePer
	 */
	public String getResponsePer() {
		return responsePer;
	}

	/**
	 * @param responsePer the responsePer to set
	 */
	public void setResponsePer(String responsePer) {
		this.responsePer = responsePer;
	}

	/**
	 * @return the finishCount
	 */
	public Integer getFinishCount() {
		return finishCount;
	}

	/**
	 * @param finishCount the finishCount to set
	 */
	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}

	/**
	 * @return the finishPer
	 */
	public String getFinishPer() {
		return finishPer;
	}

	/**
	 * @param finishPer the finishPer to set
	 */
	public void setFinishPer(String finishPer) {
		this.finishPer = finishPer;
	}

	/**
	 * @return the responseUserName
	 */
	public String getResponseUserName() {
		return responseUserName;
	}

	/**
	 * @param responseUserName the responseUserName to set
	 */
	public void setResponseUserName(String responseUserName) {
		this.responseUserName = responseUserName;
	}

	/**
	 * @return the responseDate
	 */
	public Timestamp getResponseDate() {
		return responseDate;
	}

	/**
	 * @param responseDate the responseDate to set
	 */
	public void setResponseDate(Timestamp responseDate) {
		this.responseDate = responseDate;
	}

	/**
	 * @return the arriveUser
	 */
	public String getArriveUser() {
		return arriveUser;
	}

	/**
	 * @param arriveUser the arriveUser to set
	 */
	public void setArriveUser(String arriveUser) {
		this.arriveUser = arriveUser;
	}

	/**
	 * @return the arriveUserName
	 */
	public String getArriveUserName() {
		return arriveUserName;
	}

	/**
	 * @param arriveUserName the arriveUserName to set
	 */
	public void setArriveUserName(String arriveUserName) {
		this.arriveUserName = arriveUserName;
	}

	/**
	 * @return the arriveDate
	 */
	public Timestamp getArriveDate() {
		return arriveDate;
	}

	/**
	 * @param arriveDate the arriveDate to set
	 */
	public void setArriveDate(Timestamp arriveDate) {
		this.arriveDate = arriveDate;
	}

	/**
	 * @return the completeUser
	 */
	public String getCompleteUser() {
		return completeUser;
	}

	/**
	 * @param completeUser the completeUser to set
	 */
	public void setCompleteUser(String completeUser) {
		this.completeUser = completeUser;
	}

	/**
	 * @return the completeUserName
	 */
	public String getCompleteUserName() {
		return completeUserName;
	}

	/**
	 * @param completeUserName the completeUserName to set
	 */
	public void setCompleteUserName(String completeUserName) {
		this.completeUserName = completeUserName;
	}

	/**
	 * @return the completeDate
	 */
	public Timestamp getCompleteDate() {
		return completeDate;
	}

	/**
	 * @param completeDate the completeDate to set
	 */
	public void setCompleteDate(Timestamp completeDate) {
		this.completeDate = completeDate;
	}

	/**
	 * @return the analyzeUser
	 */
	public String getAnalyzeUser() {
		return analyzeUser;
	}

	/**
	 * @param analyzeUser the analyzeUser to set
	 */
	public void setAnalyzeUser(String analyzeUser) {
		this.analyzeUser = analyzeUser;
	}

	/**
	 * @return the analyzeUserName
	 */
	public String getAnalyzeUserName() {
		return analyzeUserName;
	}

	/**
	 * @param analyzeUserName the analyzeUserName to set
	 */
	public void setAnalyzeUserName(String analyzeUserName) {
		this.analyzeUserName = analyzeUserName;
	}

	/**
	 * @return the analyzeDate
	 */
	public Timestamp getAnalyzeDate() {
		return analyzeDate;
	}

	/**
	 * @param analyzeDate the analyzeDate to set
	 */
	public void setAnalyzeDate(Timestamp analyzeDate) {
		this.analyzeDate = analyzeDate;
	}

	/**
	 * @return the closeUser
	 */
	public String getCloseUser() {
		return closeUser;
	}

	/**
	 * @param closeUser the closeUser to set
	 */
	public void setCloseUser(String closeUser) {
		this.closeUser = closeUser;
	}

	/**
	 * @return the closeUserName
	 */
	public String getCloseUserName() {
		return closeUserName;
	}

	/**
	 * @param closeUserName the closeUserName to set
	 */
	public void setCloseUserName(String closeUserName) {
		this.closeUserName = closeUserName;
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
	 * @return the installedUser
	 */
	public String getInstalledUser() {
		return installedUser;
	}

	/**
	 * @param installedUser the installedUser to set
	 */
	public void setInstalledUser(String installedUser) {
		this.installedUser = installedUser;
	}

	/**
	 * @return the isTms
	 */
	public String getIsTms() {
		return isTms;
	}

	/**
	 * @param isTms the isTms to set
	 */
	public void setIsTms(String isTms) {
		this.isTms = isTms;
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
	 * @return the problemSolution
	 */
	public String getProblemSolution() {
		return problemSolution;
	}

	/**
	 * @param problemSolution the problemSolution to set
	 */
	public void setProblemSolution(String problemSolution) {
		this.problemSolution = problemSolution;
	}

	/**
	 * @return the responsibity
	 */
	public String getResponsibity() {
		return responsibity;
	}

	/**
	 * @param responsibity the responsibity to set
	 */
	public void setResponsibity(String responsibity) {
		this.responsibity = responsibity;
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
	 * @return the responseStatus
	 */
	public String getResponseStatus() {
		return responseStatus;
	}

	/**
	 * @param responseStatus the responseStatus to set
	 */
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * @return the arriveStatus
	 */
	public String getArriveStatus() {
		return arriveStatus;
	}

	/**
	 * @param arriveStatus the arriveStatus to set
	 */
	public void setArriveStatus(String arriveStatus) {
		this.arriveStatus = arriveStatus;
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

	/**
	 * @return the caseDealStatus
	 */
	public String getCaseDealStatus() {
		return caseDealStatus;
	}

	/**
	 * @param caseDealStatus the caseDealStatus to set
	 */
	public void setCaseDealStatus(String caseDealStatus) {
		this.caseDealStatus = caseDealStatus;
	}

	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * @return the wareHouseId
	 */
	public String getWareHouseId() {
		return wareHouseId;
	}

	/**
	 * @param wareHouseId the wareHouseId to set
	 */
	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
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
	 * @return the repairAdress
	 */
	public String getRepairAdress() {
		return repairAdress;
	}

	/**
	 * @param repairAdress the repairAdress to set
	 */
	public void setRepairAdress(String repairAdress) {
		this.repairAdress = repairAdress;
	}

	/**
	 * @return the peripheralsContract
	 */
	public String getPeripheralsContract() {
		return peripheralsContract;
	}

	/**
	 * @param peripheralsContract the peripheralsContract to set
	 */
	public void setPeripheralsContract(String peripheralsContract) {
		this.peripheralsContract = peripheralsContract;
	}

	/**
	 * @return the peripherals2Contract
	 */
	public String getPeripherals2Contract() {
		return peripherals2Contract;
	}

	/**
	 * @param peripherals2Contract the peripherals2Contract to set
	 */
	public void setPeripherals2Contract(String peripherals2Contract) {
		this.peripherals2Contract = peripherals2Contract;
	}

	/**
	 * @return the peripherals3Contract
	 */
	public String getPeripherals3Contract() {
		return peripherals3Contract;
	}

	/**
	 * @param peripherals3Contract the peripherals3Contract to set
	 */
	public void setPeripherals3Contract(String peripherals3Contract) {
		this.peripherals3Contract = peripherals3Contract;
	}

	/**
	 * @return the openFunctionName
	 */
	public String getOpenFunctionName() {
		return openFunctionName;
	}

	/**
	 * @param openFunctionName the openFunctionName to set
	 */
	public void setOpenFunctionName(String openFunctionName) {
		this.openFunctionName = openFunctionName;
	}

	/**
	 * @return the cupTransType
	 */
	public String getCupTransType() {
		return cupTransType;
	}

	/**
	 * @param cupTransType the cupTransType to set
	 */
	public void setCupTransType(String cupTransType) {
		this.cupTransType = cupTransType;
	}

	/**
	 * @return the dccTransType
	 */
	public String getDccTransType() {
		return dccTransType;
	}
	
	/**
	 * @param dccTransType the dccTransType to set
	 */
	public void setDccTransType(String dccTransType) {
		this.dccTransType = dccTransType;
	}

	/**
	 * @return the dealDate
	 */
	public Timestamp getDealDate() {
		return dealDate;
	}

	/**
	 * @param dealDate the dealDate to set
	 */
	public void setDealDate(Timestamp dealDate) {
		this.dealDate = dealDate;
	}

	/**
	 * @return the transactionDescription
	 */
	public String getTransactionDescription() {
		return transactionDescription;
	}

	/**
	 * @param transactionDescription the transactionDescription to set
	 */
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	/**
	 * @return the processTypeName
	 */
	public String getProcessTypeName() {
		return processTypeName;
	}

	/**
	 * @param processTypeName the processTypeName to set
	 */
	public void setProcessTypeName(String processTypeName) {
		this.processTypeName = processTypeName;
	}

	/**
	 * @return the problemReasonName
	 */
	public String getProblemReasonName() {
		return problemReasonName;
	}

	/**
	 * @param problemReasonName the problemReasonName to set
	 */
	public void setProblemReasonName(String problemReasonName) {
		this.problemReasonName = problemReasonName;
	}

	/**
	 * @return the problemReasonCategoryName
	 */
	public String getProblemReasonCategoryName() {
		return problemReasonCategoryName;
	}

	/**
	 * @param problemReasonCategoryName the problemReasonCategoryName to set
	 */
	public void setProblemReasonCategoryName(String problemReasonCategoryName) {
		this.problemReasonCategoryName = problemReasonCategoryName;
	}

	/**
	 * @return the problemSolutionName
	 */
	public String getProblemSolutionName() {
		return problemSolutionName;
	}

	/**
	 * @param problemSolutionName the problemSolutionName to set
	 */
	public void setProblemSolutionName(String problemSolutionName) {
		this.problemSolutionName = problemSolutionName;
	}

	/**
	 * @return the problemSolutionCategoryName
	 */
	public String getProblemSolutionCategoryName() {
		return problemSolutionCategoryName;
	}

	/**
	 * @param problemSolutionCategoryName the problemSolutionCategoryName to set
	 */
	public void setProblemSolutionCategoryName(String problemSolutionCategoryName) {
		this.problemSolutionCategoryName = problemSolutionCategoryName;
	}

	/**
	 * @return the responseWarnning
	 */
	public Double getResponseWarnning() {
		return responseWarnning;
	}

	/**
	 * @param responseWarnning the responseWarnning to set
	 */
	public void setResponseWarnning(Double responseWarnning) {
		this.responseWarnning = responseWarnning;
	}

	/**
	 * @return the arriveWarnning
	 */
	public Double getArriveWarnning() {
		return arriveWarnning;
	}

	/**
	 * @param arriveWarnning the arriveWarnning to set
	 */
	public void setArriveWarnning(Double arriveWarnning) {
		this.arriveWarnning = arriveWarnning;
	}

	/**
	 * @return the completeWarnning
	 */
	public Double getCompleteWarnning() {
		return completeWarnning;
	}

	/**
	 * @param completeWarnning the completeWarnning to set
	 */
	public void setCompleteWarnning(Double completeWarnning) {
		this.completeWarnning = completeWarnning;
	}

	/**
	 * @return the responseWarnDate
	 */
	public Timestamp getResponseWarnDate() {
		return responseWarnDate;
	}

	/**
	 * @param responseWarnDate the responseWarnDate to set
	 */
	public void setResponseWarnDate(Timestamp responseWarnDate) {
		this.responseWarnDate = responseWarnDate;
	}

	/**
	 * @return the arriveWarnDate
	 */
	public Timestamp getArriveWarnDate() {
		return arriveWarnDate;
	}

	/**
	 * @param arriveWarnDate the arriveWarnDate to set
	 */
	public void setArriveWarnDate(Timestamp arriveWarnDate) {
		this.arriveWarnDate = arriveWarnDate;
	}

	/**
	 * @return the completeWarnDate
	 */
	public Timestamp getCompleteWarnDate() {
		return completeWarnDate;
	}

	/**
	 * @param completeWarnDate the completeWarnDate to set
	 */
	public void setCompleteWarnDate(Timestamp completeWarnDate) {
		this.completeWarnDate = completeWarnDate;
	}

	/**
	 * @return the networkLineNumber
	 */
	public String getNetworkLineNumber() {
		return networkLineNumber;
	}

	/**
	 * @param networkLineNumber the networkLineNumber to set
	 */
	public void setNetworkLineNumber(String networkLineNumber) {
		this.networkLineNumber = networkLineNumber;
	}

	/**
	 * @return the rushRepair
	 */
	public String getRushRepair() {
		return rushRepair;
	}

	/**
	 * @param rushRepair the rushRepair to set
	 */
	public void setRushRepair(String rushRepair) {
		this.rushRepair = rushRepair;
	}

	/**
	 * @return the responseCondition
	 */
	public String getResponseCondition() {
		return responseCondition;
	}

	/**
	 * @param responseCondition the responseCondition to set
	 */
	public void setResponseCondition(String responseCondition) {
		this.responseCondition = responseCondition;
	}

	/**
	 * @return the arriveCondition
	 */
	public String getArriveCondition() {
		return arriveCondition;
	}

	/**
	 * @param arriveCondition the arriveCondition to set
	 */
	public void setArriveCondition(String arriveCondition) {
		this.arriveCondition = arriveCondition;
	}

	/**
	 * @return the completeCondition
	 */
	public String getCompleteCondition() {
		return completeCondition;
	}

	/**
	 * @param completeCondition the completeCondition to set
	 */
	public void setCompleteCondition(String completeCondition) {
		this.completeCondition = completeCondition;
	}

	/**
	 * @return the srmCaseAssetFunctionDTOs
	 */
	public List<SrmCaseAssetFunctionDTO> getSrmCaseAssetFunctionDTOs() {
		return srmCaseAssetFunctionDTOs;
	}

	/**
	 * @param srmCaseAssetFunctionDTOs the srmCaseAssetFunctionDTOs to set
	 */
	public void setSrmCaseAssetFunctionDTOs(
			List<SrmCaseAssetFunctionDTO> srmCaseAssetFunctionDTOs) {
		this.srmCaseAssetFunctionDTOs = srmCaseAssetFunctionDTOs;
	}

	/**
	 * @return the srmCaseAssetLinkDTOs
	 */
	public List<SrmCaseAssetLinkDTO> getSrmCaseAssetLinkDTOs() {
		return srmCaseAssetLinkDTOs;
	}

	/**
	 * @param srmCaseAssetLinkDTOs the srmCaseAssetLinkDTOs to set
	 */
	public void setSrmCaseAssetLinkDTOs(
			List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOs) {
		this.srmCaseAssetLinkDTOs = srmCaseAssetLinkDTOs;
	}

	/**
	 * @return the srmCaseTransactionDTO
	 */
	public SrmCaseTransactionDTO getSrmCaseTransactionDTO() {
		return srmCaseTransactionDTO;
	}

	/**
	 * @param srmCaseTransactionDTO the srmCaseTransactionDTO to set
	 */
	public void setSrmCaseTransactionDTO(SrmCaseTransactionDTO srmCaseTransactionDTO) {
		this.srmCaseTransactionDTO = srmCaseTransactionDTO;
	}

	/**
	 * @return the caseLoopPrams
	 */
	public Integer getCaseLoopPrams() {
		return caseLoopPrams;
	}

	/**
	 * @param caseLoopPrams the caseLoopPrams to set
	 */
	public void setCaseLoopPrams(Integer caseLoopPrams) {
		this.caseLoopPrams = caseLoopPrams;
	}

	/**
	 * @return the dtidMap
	 */
	public Map<Integer, String> getDtidMap() {
		return dtidMap;
	}

	/**
	 * @param dtidMap the dtidMap to set
	 */
	public void setDtidMap(Map<Integer, String> dtidMap) {
		this.dtidMap = dtidMap;
	}

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	/**
	 * @return the contractSlaDTO
	 */
	public ContractSlaDTO getContractSlaDTO() {
		return contractSlaDTO;
	}

	/**
	 * @param contractSlaDTO the contractSlaDTO to set
	 */
	public void setContractSlaDTO(ContractSlaDTO contractSlaDTO) {
		this.contractSlaDTO = contractSlaDTO;
	}
	

	/**
	 * @return the repeatCaseId
	 */
	public String getRepeatCaseId() {
		return repeatCaseId;
	}

	/**
	 * @param repeatCaseId the repeatCaseId to set
	 */
	public void setRepeatCaseId(String repeatCaseId) {
		this.repeatCaseId = repeatCaseId;
	}

	/**
	 * @return the caseNo
	 */
	public String getCaseNo() {
		return caseNo;
	}

	/**
	 * @param caseNo the caseNo to set
	 */
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	/**
	 * @return the installedDate
	 */
	public Timestamp getInstalledDate() {
		return installedDate;
	}

	/**
	 * @param installedDate the installedDate to set
	 */
	public void setInstalledDate(Timestamp installedDate) {
		this.installedDate = installedDate;
	}

	/**
	 * @return the uninstalledDate
	 */
	public Timestamp getUninstalledDate() {
		return uninstalledDate;
	}

	/**
	 * @param uninstalledDate the uninstalledDate to set
	 */
	public void setUninstalledDate(Timestamp uninstalledDate) {
		this.uninstalledDate = uninstalledDate;
	}

	/**
	 * @return the cupEnableDate
	 */
	public Timestamp getCupEnableDate() {
		return cupEnableDate;
	}

	/**
	 * @param cupEnableDate the cupEnableDate to set
	 */
	public void setCupEnableDate(Timestamp cupEnableDate) {
		this.cupEnableDate = cupEnableDate;
	}

	/**
	 * @return the cupDisableDate
	 */
	public Timestamp getCupDisableDate() {
		return cupDisableDate;
	}

	/**
	 * @param cupDisableDate the cupDisableDate to set
	 */
	public void setCupDisableDate(Timestamp cupDisableDate) {
		this.cupDisableDate = cupDisableDate;
	}

	/**
	 * @return the updateItem
	 */
	public String getUpdateItem() {
		return updateItem;
	}

	/**
	 * @param updateItem the updateItem to set
	 */
	public void setUpdateItem(String updateItem) {
		this.updateItem = updateItem;
	}
	/**
	 * @return the oldInstalledDeptAndUser
	 */
	public String getOldInstalledDeptAndUser() {
		return oldInstalledDeptAndUser;
	}

	/**
	 * @param oldInstalledDeptAndUser the oldInstalledDeptAndUser to set
	 */
	public void setOldInstalledDeptAndUser(String oldInstalledDeptAndUser) {
		this.oldInstalledDeptAndUser = oldInstalledDeptAndUser;
	}

	/**
	 * @return the installedUserName
	 */
	public String getInstalledUserName() {
		return installedUserName;
	}

	/**
	 * @param installedUserName the installedUserName to set
	 */
	public void setInstalledUserName(String installedUserName) {
		this.installedUserName = installedUserName;
	}
	/**
	 * @return the openTransactionList
	 */
	public String getOpenTransactionList() {
		return openTransactionList;
	}

	/**
	 * @param openTransactionList the openTransactionList to set
	 */
	public void setOpenTransactionList(String openTransactionList) {
		this.openTransactionList = openTransactionList;
	}

	/**
	 * @return the crossTabReportDTOs
	 */
	public List<CrossTabReportDTO> getCrossTabReportDTOs() {
		return crossTabReportDTOs;
	}

	/**
	 * @param crossTabReportDTOs the crossTabReportDTOs to set
	 */
	public void setCrossTabReportDTOs(List<CrossTabReportDTO> crossTabReportDTOs) {
		this.crossTabReportDTOs = crossTabReportDTOs;
	}

	/**
	 * @return the srmTransactionParameterItemDTOs
	 */
	public List<SrmTransactionParameterItemDTO> getSrmTransactionParameterItemDTOs() {
		return srmTransactionParameterItemDTOs;
	}

	/**
	 * @param srmTransactionParameterItemDTOs the srmTransactionParameterItemDTOs to set
	 */
	public void setSrmTransactionParameterItemDTOs(
			List<SrmTransactionParameterItemDTO> srmTransactionParameterItemDTOs) {
		this.srmTransactionParameterItemDTOs = srmTransactionParameterItemDTOs;
	}

	/**
	 * @return the isHistory
	 */
	public String getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory the isHistory to set
	 */
	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}

	/**
	 * @return the merMid
	 */
	public String getMerMid() {
		return merMid;
	}

	/**
	 * @param merMid the merMid to set
	 */
	public void setMerMid(String merMid) {
		this.merMid = merMid;
	}

	/**
	 * @return the problemReasonCode
	 */
	public String getProblemReasonCode() {
		return problemReasonCode;
	}

	/**
	 * @return the problemSolutionCode
	 */
	public String getProblemSolutionCode() {
		return problemSolutionCode;
	}

	/**
	 * @param problemReasonCode the problemReasonCode to set
	 */
	public void setProblemReasonCode(String problemReasonCode) {
		this.problemReasonCode = problemReasonCode;
	}

	/**
	 * @param problemSolutionCode the problemSolutionCode to set
	 */
	public void setProblemSolutionCode(String problemSolutionCode) {
		this.problemSolutionCode = problemSolutionCode;
	}

	/**
	 * @return the responsibityName
	 */
	public String getResponsibityName() {
		return responsibityName;
	}

	/**
	 * @param responsibityName the responsibityName to set
	 */
	public void setResponsibityName(String responsibityName) {
		this.responsibityName = responsibityName;
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
	 * @return the dateMap
	 */
	public Map<String, Date> getDateMap() {
		return dateMap;
	}

	/**
	 * @param dateMap the dateMap to set
	 */
	public void setDateMap(Map<String, Date> dateMap) {
		this.dateMap = dateMap;
	}

	/**
	 * @return the electronicInvoice
	 */
	public String getElectronicInvoice() {
		return electronicInvoice;
	}

	/**
	 * @return the cupQuickPass
	 */
	public String getCupQuickPass() {
		return cupQuickPass;
	}

	/**
	 * @param electronicInvoice the electronicInvoice to set
	 */
	public void setElectronicInvoice(String electronicInvoice) {
		this.electronicInvoice = electronicInvoice;
	}

	/**
	 * @param cupQuickPass the cupQuickPass to set
	 */
	public void setCupQuickPass(String cupQuickPass) {
		this.cupQuickPass = cupQuickPass;
	}

	/**
	 * @return the dispatchDeptName
	 */
	public String getDispatchDeptName() {
		return dispatchDeptName;
	}

	/**
	 * @param dispatchDeptName the dispatchDeptName to set
	 */
	public void setDispatchDeptName(String dispatchDeptName) {
		this.dispatchDeptName = dispatchDeptName;
	}

	/**
	 * @return the caseHandleInfoDTOs
	 */
	public List<SrmCaseHandleInfoDTO> getCaseHandleInfoDTOs() {
		return caseHandleInfoDTOs;
	}

	/**
	 * @param caseHandleInfoDTOs the caseHandleInfoDTOs to set
	 */
	public void setCaseHandleInfoDTOs(List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs) {
		this.caseHandleInfoDTOs = caseHandleInfoDTOs;
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
	 * @return the sameInstalled
	 */
	public String getSameInstalled() {
		return sameInstalled;
	}

	/**
	 * @param sameInstalled the sameInstalled to set
	 */
	public void setSameInstalled(String sameInstalled) {
		this.sameInstalled = sameInstalled;
	}

	/**
	 * @return the tmsParamDesc
	 */
	public String getTmsParamDesc() {
		return tmsParamDesc;
	}

	/**
	 * @param tmsParamDesc the tmsParamDesc to set
	 */
	public void setTmsParamDesc(String tmsParamDesc) {
		this.tmsParamDesc = tmsParamDesc;
	}

	/**
	 * @return the peripheralsContractCode
	 */
	public String getPeripheralsContractCode() {
		return peripheralsContractCode;
	}

	/**
	 * @param peripheralsContractCode the peripheralsContractCode to set
	 */
	public void setPeripheralsContractCode(String peripheralsContractCode) {
		this.peripheralsContractCode = peripheralsContractCode;
	}

	/**
	 * @return the peripherals2ContractCode
	 */
	public String getPeripherals2ContractCode() {
		return peripherals2ContractCode;
	}

	/**
	 * @param peripherals2ContractCode the peripherals2ContractCode to set
	 */
	public void setPeripherals2ContractCode(String peripherals2ContractCode) {
		this.peripherals2ContractCode = peripherals2ContractCode;
	}

	/**
	 * @return the peripherals3ContractCode
	 */
	public String getPeripherals3ContractCode() {
		return peripherals3ContractCode;
	}

	/**
	 * @param peripherals3ContractCode the peripherals3ContractCode to set
	 */
	public void setPeripherals3ContractCode(String peripherals3ContractCode) {
		this.peripherals3ContractCode = peripherals3ContractCode;
	}

	/**
	 * @return the functionTypeList
	 */
	public String getFunctionTypeList() {
		return functionTypeList;
	}

	/**
	 * @param functionTypeList the functionTypeList to set
	 */
	public void setFunctionTypeList(String functionTypeList) {
		this.functionTypeList = functionTypeList;
	}

	/**
	 * @return the edcSerialNumber
	 */
	public String getEdcSerialNumber() {
		return edcSerialNumber;
	}

	/**
	 * @param edcSerialNumber the edcSerialNumber to set
	 */
	public void setEdcSerialNumber(String edcSerialNumber) {
		this.edcSerialNumber = edcSerialNumber;
	}

	/**
	 * @return the attendanceDesc
	 */
	public String getAttendanceDesc() {
		return attendanceDesc;
	}

	/**
	 * @param attendanceDesc the attendanceDesc to set
	 */
	public void setAttendanceDesc(String attendanceDesc) {
		this.attendanceDesc = attendanceDesc;
	}

	/**
	 * @return the rushRepairDesc
	 */
	public String getRushRepairDesc() {
		return rushRepairDesc;
	}

	/**
	 * @param rushRepairDesc the rushRepairDesc to set
	 */
	public void setRushRepairDesc(String rushRepairDesc) {
		this.rushRepairDesc = rushRepairDesc;
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
	 * @return the edcTypeContract
	 */
	public String getEdcTypeContract() {
		return edcTypeContract;
	}

	/**
	 * @param edcTypeContract the edcTypeContract to set
	 */
	public void setEdcTypeContract(String edcTypeContract) {
		this.edcTypeContract = edcTypeContract;
	}

	

	/**
	 * @return the ecrLine
	 */
	public String getEcrLine() {
		return ecrLine;
	}

	/**
	 * @param ecrLine the ecrLine to set
	 */
	public void setEcrLine(String ecrLine) {
		this.ecrLine = ecrLine;
	}

	/**
	 * @return the netLine
	 */
	public String getNetLine() {
		return netLine;
	}

	/**
	 * @param netLine the netLine to set
	 */
	public void setNetLine(String netLine) {
		this.netLine = netLine;
	}

	/**
	 * @return the otherLine
	 */
	public String getOtherLine() {
		return otherLine;
	}

	/**
	 * @param otherLine the otherLine to set
	 */
	public void setOtherLine(String otherLine) {
		this.otherLine = otherLine;
	}

	/**
	 * @return the isFirstInstalled
	 */
	public String getIsFirstInstalled() {
		return isFirstInstalled;
	}

	/**
	 * @param isFirstInstalled the isFirstInstalled to set
	 */
	public void setIsFirstInstalled(String isFirstInstalled) {
		this.isFirstInstalled = isFirstInstalled;
	}
	
	/**
	 * @return the posPrice
	 */
	public String getPosPrice() {
		return posPrice;
	}

	/**
	 * @param posPrice the posPrice to set
	 */
	public void setPosPrice(String posPrice) {
		this.posPrice = posPrice;
	}

	/**
	 * @return the posPriceInFee
	 */
	public Integer getPosPriceInFee() {
		return posPriceInFee;
	}

	/**
	 * @param posPriceInFee the posPriceInFee to set
	 */
	public void setPosPriceInFee(Integer posPriceInFee) {
		this.posPriceInFee = posPriceInFee;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the bussionAddress
	 */
	public String getBussionAddress() {
		return bussionAddress;
	}

	/**
	 * @param bussionAddress the bussionAddress to set
	 */
	public void setBussionAddress(String bussionAddress) {
		this.bussionAddress = bussionAddress;
	}

	/**
	 * @return the installedAddress
	 */
	public String getInstalledAddress() {
		return installedAddress;
	}

	/**
	 * @param installedAddress the installedAddress to set
	 */
	public void setInstalledAddress(String installedAddress) {
		this.installedAddress = installedAddress;
	}

	/**
	 * @return the delayTime
	 */
	public BigDecimal getDelayTime() {
		return delayTime;
	}

	/**
	 * @param delayTime the delayTime to set
	 */
	public void setDelayTime(BigDecimal delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * @return the unInstalledAddress
	 */
	public String getUnInstalledAddress() {
		return unInstalledAddress;
	}

	/**
	 * @param unInstalledAddress the unInstalledAddress to set
	 */
	public void setUnInstalledAddress(String unInstalledAddress) {
		this.unInstalledAddress = unInstalledAddress;
	}

	/**
	 * @return the unInstalledCompanyName
	 */
	public String getUnInstalledCompanyName() {
		return unInstalledCompanyName;
	}

	/**
	 * @param unInstalledCompanyName the unInstalledCompanyName to set
	 */
	public void setUnInstalledCompanyName(String unInstalledCompanyName) {
		this.unInstalledCompanyName = unInstalledCompanyName;
	}

	/**
	 * @return the userdDays90
	 */
	public String getUserdDays90() {
		return userdDays90;
	}

	/**
	 * @param userdDays90 the userdDays90 to set
	 */
	public void setUserdDays90(String userdDays90) {
		this.userdDays90 = userdDays90;
	}

	/**
	 * @return the userdDays120
	 */
	public String getUserdDays120() {
		return userdDays120;
	}

	/**
	 * @param userdDays120 the userdDays120 to set
	 */
	public void setUserdDays120(String userdDays120) {
		this.userdDays120 = userdDays120;
	}

	/**
	 * @return the isOpenEncrypt
	 */
	public String getIsOpenEncrypt() {
		return isOpenEncrypt;
	}

	/**
	 * @param isOpenEncrypt the isOpenEncrypt to set
	 */
	public void setIsOpenEncrypt(String isOpenEncrypt) {
		this.isOpenEncrypt = isOpenEncrypt;
	}

	/**
	 * @return the electronicPayPlatform
	 */
	public String getElectronicPayPlatform() {
		return electronicPayPlatform;
	}

	/**
	 * @param electronicPayPlatform the electronicPayPlatform to set
	 */
	public void setElectronicPayPlatform(String electronicPayPlatform) {
		this.electronicPayPlatform = electronicPayPlatform;
	}

	/**
	 * @return the logoStyle
	 */
	public String getLogoStyle() {
		return logoStyle;
	}

	/**
	 * @param logoStyle the logoStyle to set
	 */
	public void setLogoStyle(String logoStyle) {
		this.logoStyle = logoStyle;
	}

	/**
	 * @return the isUpdateAsset
	 */
	public String getIsUpdateAsset() {
		return isUpdateAsset;
	}

	/**
	 * @param isUpdateAsset the isUpdateAsset to set
	 */
	public void setIsUpdateAsset(String isUpdateAsset) {
		this.isUpdateAsset = isUpdateAsset;
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
	 * @return the srmPaymentItemDTOs
	 */
	public List<SrmPaymentItemDTO> getSrmPaymentItemDTOs() {
		return srmPaymentItemDTOs;
	}

	/**
	 * @param srmPaymentItemDTOs the srmPaymentItemDTOs to set
	 */
	public void setSrmPaymentItemDTOs(List<SrmPaymentItemDTO> srmPaymentItemDTOs) {
		this.srmPaymentItemDTOs = srmPaymentItemDTOs;
	}

	/**
	 * @return the merchantCodeOther
	 */
	public String getMerchantCodeOther() {
		return merchantCodeOther;
	}

	/**
	 * @param merchantCodeOther the merchantCodeOther to set
	 */
	public void setMerchantCodeOther(String merchantCodeOther) {
		this.merchantCodeOther = merchantCodeOther;
	}

	/**
	 * @return the installDeptName
	 */
	public String getInstallDeptName() {
		return installDeptName;
	}

	/**
	 * @param installDeptName the installDeptName to set
	 */
	public void setInstallDeptName(String installDeptName) {
		this.installDeptName = installDeptName;
	}

	/**
	 * @return the fomsCase
	 */
	public String getFomsCase() {
		return fomsCase;
	}

	/**
	 * @param fomsCase the fomsCase to set
	 */
	public void setFomsCase(String fomsCase) {
		this.fomsCase = fomsCase;
	}

	/**
	 * @return the smartPayFlag
	 */
	public String getSmartPayFlag() {
		return smartPayFlag;
	}

	/**
	 * @return the smid
	 */
	public String getSmid() {
		return smid;
	}

	/**
	 * @return the stid
	 */
	public String getStid() {
		return stid;
	}

	/**
	 * @param smartPayFlag the smartPayFlag to set
	 */
	public void setSmartPayFlag(String smartPayFlag) {
		this.smartPayFlag = smartPayFlag;
	}

	/**
	 * @param smid the smid to set
	 */
	public void setSmid(String smid) {
		this.smid = smid;
	}

	/**
	 * @param stid the stid to set
	 */
	public void setStid(String stid) {
		this.stid = stid;
	}

	/**
	 * @return the unionPayFlag
	 */
	public String getUnionPayFlag() {
		return unionPayFlag;
	}

	/**
	 * @param unionPayFlag the unionPayFlag to set
	 */
	public void setUnionPayFlag(String unionPayFlag) {
		this.unionPayFlag = unionPayFlag;
	}

	/**
	 * @return the enableClss
	 */
	public String getEnableClss() {
		return enableClss;
	}

	/**
	 * @return the pinpadExtern
	 */
	public String getPinpadExtern() {
		return pinpadExtern;
	}

	/**
	 * @param enableClss the enableClss to set
	 */
	public void setEnableClss(String enableClss) {
		this.enableClss = enableClss;
	}

	/**
	 * @param pinpadExtern the pinpadExtern to set
	 */
	public void setPinpadExtern(String pinpadExtern) {
		this.pinpadExtern = pinpadExtern;
	}

	/**
	 * @return the checkedDate
	 */
	public String getCheckedDate() {
		return checkedDate;
	}

	/**
	 * @param checkedDate the checkedDate to set
	 */
	public void setCheckedDate(String checkedDate) {
		this.checkedDate = checkedDate;
	}

	/**
	 * @return the assetStatus
	 */
	public String getAssetStatus() {
		return assetStatus;
	}

	/**
	 * @param assetStatus the assetStatus to set
	 */
	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
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
	 * @return the fee
	 */
	public String getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(String fee) {
		this.fee = fee;
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
	 * @return the ecrLineNoTax
	 */
	public Integer getEcrLineNoTax() {
		return ecrLineNoTax;
	}

	/**
	 * @param ecrLineNoTax the ecrLineNoTax to set
	 */
	public void setEcrLineNoTax(Integer ecrLineNoTax) {
		this.ecrLineNoTax = ecrLineNoTax;
	}

	/**
	 * @return the netLineNoTax
	 */
	public Integer getNetLineNoTax() {
		return netLineNoTax;
	}

	/**
	 * @param netLineNoTax the netLineNoTax to set
	 */
	public void setNetLineNoTax(Integer netLineNoTax) {
		this.netLineNoTax = netLineNoTax;
	}

	/**
	 * @return the notFirstInstalledPrice
	 */
	public Integer getNotFirstInstalledPrice() {
		return notFirstInstalledPrice;
	}

	/**
	 * @param notFirstInstalledPrice the notFirstInstalledPrice to set
	 */
	public void setNotFirstInstalledPrice(Integer notFirstInstalledPrice) {
		this.notFirstInstalledPrice = notFirstInstalledPrice;
	}

	/**
	 * @return the notFirstInstalledPriceNoTax
	 */
	public Integer getNotFirstInstalledPriceNoTax() {
		return notFirstInstalledPriceNoTax;
	}

	/**
	 * @param notFirstInstalledPriceNoTax the notFirstInstalledPriceNoTax to set
	 */
	public void setNotFirstInstalledPriceNoTax(Integer notFirstInstalledPriceNoTax) {
		this.notFirstInstalledPriceNoTax = notFirstInstalledPriceNoTax;
	}

	/**
	 * @return the peripheralsIsUpdate
	 */
	public String getPeripheralsIsUpdate() {
		return peripheralsIsUpdate;
	}

	/**
	 * @param peripheralsIsUpdate the peripheralsIsUpdate to set
	 */
	public void setPeripheralsIsUpdate(String peripheralsIsUpdate) {
		this.peripheralsIsUpdate = peripheralsIsUpdate;
	}

	/**
	 * @return the dispatchPrice
	 */
	public Integer getDispatchPrice() {
		return dispatchPrice;
	}

	/**
	 * @param dispatchPrice the dispatchPrice to set
	 */
	public void setDispatchPrice(Integer dispatchPrice) {
		this.dispatchPrice = dispatchPrice;
	}

	/**
	 * @return the dispatchPriceNoTax
	 */
	public Integer getDispatchPriceNoTax() {
		return dispatchPriceNoTax;
	}

	/**
	 * @param dispatchPriceNoTax the dispatchPriceNoTax to set
	 */
	public void setDispatchPriceNoTax(Integer dispatchPriceNoTax) {
		this.dispatchPriceNoTax = dispatchPriceNoTax;
	}

	/**
	 * @return the unInstallPrice
	 */
	public Integer getUnInstallPrice() {
		return unInstallPrice;
	}

	/**
	 * @param unInstallPrice the unInstallPrice to set
	 */
	public void setUnInstallPrice(Integer unInstallPrice) {
		this.unInstallPrice = unInstallPrice;
	}

	/**
	 * @return the unInstallPriceNoTax
	 */
	public Integer getUnInstallPriceNoTax() {
		return unInstallPriceNoTax;
	}

	/**
	 * @param unInstallPriceNoTax the unInstallPriceNoTax to set
	 */
	public void setUnInstallPriceNoTax(Integer unInstallPriceNoTax) {
		this.unInstallPriceNoTax = unInstallPriceNoTax;
	}

	/**
	 * @return the settingPrice
	 */
	public Integer getSettingPrice() {
		return settingPrice;
	}

	/**
	 * @param settingPrice the settingPrice to set
	 */
	public void setSettingPrice(Integer settingPrice) {
		this.settingPrice = settingPrice;
	}

	/**
	 * @return the settingPriceNoTax
	 */
	public Integer getSettingPriceNoTax() {
		return settingPriceNoTax;
	}

	/**
	 * @param settingPriceNoTax the settingPriceNoTax to set
	 */
	public void setSettingPriceNoTax(Integer settingPriceNoTax) {
		this.settingPriceNoTax = settingPriceNoTax;
	}
	
	/**
	 * @return the notFirstInstalledPriceSum
	 */
	public Integer getNotFirstInstalledPriceSum() {
		return notFirstInstalledPriceSum;
	}

	/**
	 * @param notFirstInstalledPriceSum the notFirstInstalledPriceSum to set
	 */
	public void setNotFirstInstalledPriceSum(Integer notFirstInstalledPriceSum) {
		this.notFirstInstalledPriceSum = notFirstInstalledPriceSum;
	}

	/**
	 * @return the notFirstInstalledNoTaxPriceSum
	 */
	public Integer getNotFirstInstalledNoTaxPriceSum() {
		return notFirstInstalledNoTaxPriceSum;
	}

	/**
	 * @param notFirstInstalledNoTaxPriceSum the notFirstInstalledNoTaxPriceSum to set
	 */
	public void setNotFirstInstalledNoTaxPriceSum(
			Integer notFirstInstalledNoTaxPriceSum) {
		this.notFirstInstalledNoTaxPriceSum = notFirstInstalledNoTaxPriceSum;
	}

	/**
	 * @return the unInstallPriceSum
	 */
	public Integer getUnInstallPriceSum() {
		return unInstallPriceSum;
	}

	/**
	 * @param unInstallPriceSum the unInstallPriceSum to set
	 */
	public void setUnInstallPriceSum(Integer unInstallPriceSum) {
		this.unInstallPriceSum = unInstallPriceSum;
	}

	/**
	 * @return the unInstallPriceNoTaxSum
	 */
	public Integer getUnInstallPriceNoTaxSum() {
		return unInstallPriceNoTaxSum;
	}

	/**
	 * @param unInstallPriceNoTaxSum the unInstallPriceNoTaxSum to set
	 */
	public void setUnInstallPriceNoTaxSum(Integer unInstallPriceNoTaxSum) {
		this.unInstallPriceNoTaxSum = unInstallPriceNoTaxSum;
	}

	/**
	 * @return the settingPriceSum
	 */
	public Integer getSettingPriceSum() {
		return settingPriceSum;
	}

	/**
	 * @param settingPriceSum the settingPriceSum to set
	 */
	public void setSettingPriceSum(Integer settingPriceSum) {
		this.settingPriceSum = settingPriceSum;
	}

	/**
	 * @return the settingPriceNoTaxSum
	 */
	public Integer getSettingPriceNoTaxSum() {
		return settingPriceNoTaxSum;
	}

	/**
	 * @param settingPriceNoTaxSum the settingPriceNoTaxSum to set
	 */
	public void setSettingPriceNoTaxSum(Integer settingPriceNoTaxSum) {
		this.settingPriceNoTaxSum = settingPriceNoTaxSum;
	}

	/**
	 * @return the fastSum
	 */
	public Integer getFastSum() {
		return fastSum;
	}

	/**
	 * @param fastSum the fastSum to set
	 */
	public void setFastSum(Integer fastSum) {
		this.fastSum = fastSum;
	}

	/**
	 * @return the fastNoTaxSum
	 */
	public Integer getFastNoTaxSum() {
		return fastNoTaxSum;
	}

	/**
	 * @param fastNoTaxSum the fastNoTaxSum to set
	 */
	public void setFastNoTaxSum(Integer fastNoTaxSum) {
		this.fastNoTaxSum = fastNoTaxSum;
	}

	/**
	 * @return the posSum0
	 */
	public Integer getPosSum0() {
		return posSum0;
	}

	/**
	 * @param posSum0 the posSum0 to set
	 */
	public void setPosSum0(Integer posSum0) {
		this.posSum0 = posSum0;
	}

	/**
	 * @return the ecrlineSumInCase
	 */
	public Integer getEcrlineSumInCase() {
		return ecrlineSumInCase;
	}

	/**
	 * @param ecrlineSumInCase the ecrlineSumInCase to set
	 */
	public void setEcrlineSumInCase(Integer ecrlineSumInCase) {
		this.ecrlineSumInCase = ecrlineSumInCase;
	}

	/**
	 * @return the ecrlineNoTaxSumInCase
	 */
	public Integer getEcrlineNoTaxSumInCase() {
		return ecrlineNoTaxSumInCase;
	}

	/**
	 * @param ecrlineNoTaxSumInCase the ecrlineNoTaxSumInCase to set
	 */
	public void setEcrlineNoTaxSumInCase(Integer ecrlineNoTaxSumInCase) {
		this.ecrlineNoTaxSumInCase = ecrlineNoTaxSumInCase;
	}

	/**
	 * @return the networkRouteSumInCase
	 */
	public Integer getNetworkRouteSumInCase() {
		return networkRouteSumInCase;
	}

	/**
	 * @param networkRouteSumInCase the networkRouteSumInCase to set
	 */
	public void setNetworkRouteSumInCase(Integer networkRouteSumInCase) {
		this.networkRouteSumInCase = networkRouteSumInCase;
	}

	/**
	 * @return the networkRouteNoTaxSumInCase
	 */
	public Integer getNetworkRouteNoTaxSumInCase() {
		return networkRouteNoTaxSumInCase;
	}

	/**
	 * @param networkRouteNoTaxSumInCase the networkRouteNoTaxSumInCase to set
	 */
	public void setNetworkRouteNoTaxSumInCase(Integer networkRouteNoTaxSumInCase) {
		this.networkRouteNoTaxSumInCase = networkRouteNoTaxSumInCase;
	}

	/**
	 * @return the attendancePrice
	 */
	public Integer getAttendancePrice() {
		return attendancePrice;
	}

	/**
	 * @param attendancePrice the attendancePrice to set
	 */
	public void setAttendancePrice(Integer attendancePrice) {
		this.attendancePrice = attendancePrice;
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
	 * @return the fastFee
	 */
	public Integer getFastFee() {
		return fastFee;
	}

	/**
	 * @param fastFee the fastFee to set
	 */
	public void setFastFee(Integer fastFee) {
		this.fastFee = fastFee;
	}

	/**
	 * @return the peripheralsUpdate
	 */
	public String getPeripheralsUpdate() {
		return peripheralsUpdate;
	}

	/**
	 * @param peripheralsUpdate the peripheralsUpdate to set
	 */
	public void setPeripheralsUpdate(String peripheralsUpdate) {
		this.peripheralsUpdate = peripheralsUpdate;
	}

	/**
	 * @return the isSamePlaceInstall
	 */
	public String getIsSamePlaceInstall() {
		return isSamePlaceInstall;
	}

	/**
	 * @param isSamePlaceInstall the isSamePlaceInstall to set
	 */
	public void setIsSamePlaceInstall(String isSamePlaceInstall) {
		this.isSamePlaceInstall = isSamePlaceInstall;
	}

	/**
	 * @return the installPrice
	 */
	public Integer getInstallPrice() {
		return installPrice;
	}

	/**
	 * @param installPrice the installPrice to set
	 */
	public void setInstallPrice(Integer installPrice) {
		this.installPrice = installPrice;
	}

	/**
	 * @return the installedPriceSum
	 */
	public Integer getInstalledPriceSum() {
		return installedPriceSum;
	}

	/**
	 * @param installedPriceSum the installedPriceSum to set
	 */
	public void setInstalledPriceSum(Integer installedPriceSum) {
		this.installedPriceSum = installedPriceSum;
	}

	/**
	 * @return the firstInstall
	 */
	public String getFirstInstall() {
		return firstInstall;
	}

	/**
	 * @param firstInstall the firstInstall to set
	 */
	public void setFirstInstall(String firstInstall) {
		this.firstInstall = firstInstall;
	}

	/**
	 * @return the firstUnInstall
	 */
	public String getFirstUnInstall() {
		return firstUnInstall;
	}

	/**
	 * @param firstUnInstall the firstUnInstall to set
	 */
	public void setFirstUnInstall(String firstUnInstall) {
		this.firstUnInstall = firstUnInstall;
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
	 * @return the index
	 */
	public Integer getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param index the index to set
	 */
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * @return the ecrLineInFee
	 */
	public Integer getEcrLineInFee() {
		return ecrLineInFee;
	}

	/**
	 * @param ecrLineInFee the ecrLineInFee to set
	 */
	public void setEcrLineInFee(Integer ecrLineInFee) {
		this.ecrLineInFee = ecrLineInFee;
	}

	/**
	 * @return the netLineInFee
	 */
	public Integer getNetLineInFee() {
		return netLineInFee;
	}

	/**
	 * @param netLineInFee the netLineInFee to set
	 */
	public void setNetLineInFee(Integer netLineInFee) {
		this.netLineInFee = netLineInFee;
	}

	/**
	 * @return the userdDays90Price
	 */
	public Integer getUserdDays90Price() {
		return userdDays90Price;
	}

	/**
	 * @param userdDays90Price the userdDays90Price to set
	 */
	public void setUserdDays90Price(Integer userdDays90Price) {
		this.userdDays90Price = userdDays90Price;
	}

	/**
	 * @return the feePrice
	 */
	public Integer getFeePrice() {
		return feePrice;
	}

	/**
	 * @param feePrice the feePrice to set
	 */
	public void setFeePrice(Integer feePrice) {
		this.feePrice = feePrice;
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
	 * @return the ccMail
	 */
	public String getCcMail() {
		return ccMail;
	}

	/**
	 * @param ccMail the ccMail to set
	 */
	public void setCcMail(String ccMail) {
		this.ccMail = ccMail;
	}
	
	/**
	 * @return the userdDays120Price
	 */
	public Integer getUserdDays120Price() {
		return userdDays120Price;
	}

	/**
	 * @param userdDays120Price the userdDays120Price to set
	 */
	public void setUserdDays120Price(Integer userdDays120Price) {
		this.userdDays120Price = userdDays120Price;
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
	 * @return the completeDepartmentId
	 */
	public String getCompleteDepartmentId() {
		return completeDepartmentId;
	}

	/**
	 * @param completeDepartmentId the completeDepartmentId to set
	 */
	public void setCompleteDepartmentId(String completeDepartmentId) {
		this.completeDepartmentId = completeDepartmentId;
	}

	/**
	 * @return the completeDepartmentName
	 */
	public String getCompleteDepartmentName() {
		return completeDepartmentName;
	}

	/**
	 * @param completeDepartmentName the completeDepartmentName to set
	 */
	public void setCompleteDepartmentName(String completeDepartmentName) {
		this.completeDepartmentName = completeDepartmentName;
	}

	/**
	 * @return the aeMid
	 */
	public String getAeMid() {
		return aeMid;
	}

	/**
	 * @param aeMid the aeMid to set
	 */
	public void setAeMid(String aeMid) {
		this.aeMid = aeMid;
	}

	/**
	 * @return the aeTid
	 */
	public String getAeTid() {
		return aeTid;
	}

	/**
	 * @param aeTid the aeTid to set
	 */
	public void setAeTid(String aeTid) {
		this.aeTid = aeTid;
	}

	/**
	 * @return the isCaseTemplate
	 */
	public String getIsCaseTemplate() {
		return isCaseTemplate;
	}

	/**
	 * @param isCaseTemplate the isCaseTemplate to set
	 */
	public void setIsCaseTemplate(String isCaseTemplate) {
		this.isCaseTemplate = isCaseTemplate;
	}

	/**
	 * @return the hasOnlineExclusion
	 */
	public String getHasOnlineExclusion() {
		return hasOnlineExclusion;
	}

	/**
	 * @param hasOnlineExclusion the hasOnlineExclusion to set
	 */
	public void setHasOnlineExclusion(String hasOnlineExclusion) {
		this.hasOnlineExclusion = hasOnlineExclusion;
	}

	/**
	 * @return the isNewCase
	 */
	public String getIsNewCase() {
		return isNewCase;
	}

	/**
	 * @param isNewCase the isNewCase to set
	 */
	public void setIsNewCase(String isNewCase) {
		this.isNewCase = isNewCase;
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
	 * @return the vendorServiceCustomer
	 */
	public String getVendorServiceCustomer() {
		return vendorServiceCustomer;
	}

	/**
	 * @param vendorServiceCustomer the vendorServiceCustomer to set
	 */
	public void setVendorServiceCustomer(String vendorServiceCustomer) {
		this.vendorServiceCustomer = vendorServiceCustomer;
	}

	/**
	 * @return the hasRetreat
	 */
	public String getHasRetreat() {
		return hasRetreat;
	}

	/**
	 * @param hasRetreat the hasRetreat to set
	 */
	public void setHasRetreat(String hasRetreat) {
		this.hasRetreat = hasRetreat;
	}

	/**
	 * @return the caseArea
	 */
	public String getCaseArea() {
		return caseArea;
	}

	/**
	 * @param caseArea the caseArea to set
	 */
	public void setCaseArea(String caseArea) {
		this.caseArea = caseArea;
	}

	/**
	 * @return the hasDelay
	 */
	public String getHasDelay() {
		return hasDelay;
	}

	/**
	 * @param hasDelay the hasDelay to set
	 */
	public void setHasDelay(String hasDelay) {
		this.hasDelay = hasDelay;
	}

	/**
	 * @return the isBussinessContactMobilePhone
	 */
	public String getIsBussinessContactMobilePhone() {
		return isBussinessContactMobilePhone;
	}

	/**
	 * @param isBussinessContactMobilePhone the isBussinessContactMobilePhone to set
	 */
	public void setIsBussinessContactMobilePhone(
			String isBussinessContactMobilePhone) {
		this.isBussinessContactMobilePhone = isBussinessContactMobilePhone;
	}

	/**
	 * @return the isBussinessContactEmail
	 */
	public String getIsBussinessContactEmail() {
		return isBussinessContactEmail;
	}

	/**
	 * @param isBussinessContactEmail the isBussinessContactEmail to set
	 */
	public void setIsBussinessContactEmail(String isBussinessContactEmail) {
		this.isBussinessContactEmail = isBussinessContactEmail;
	}

	/**
	 * @return the installedContactMobilePhone
	 */
	public String getInstalledContactMobilePhone() {
		return installedContactMobilePhone;
	}

	/**
	 * @param installedContactMobilePhone the installedContactMobilePhone to set
	 */
	public void setInstalledContactMobilePhone(String installedContactMobilePhone) {
		this.installedContactMobilePhone = installedContactMobilePhone;
	}

	/**
	 * @return the installedContactEmail
	 */
	public String getInstalledContactEmail() {
		return installedContactEmail;
	}

	/**
	 * @param installedContactEmail the installedContactEmail to set
	 */
	public void setInstalledContactEmail(String installedContactEmail) {
		this.installedContactEmail = installedContactEmail;
	}

	/**
	 * @return the contactIsBussinessContactMobilePhone
	 */
	public String getContactIsBussinessContactMobilePhone() {
		return contactIsBussinessContactMobilePhone;
	}

	/**
	 * @param contactIsBussinessContactMobilePhone the contactIsBussinessContactMobilePhone to set
	 */
	public void setContactIsBussinessContactMobilePhone(
			String contactIsBussinessContactMobilePhone) {
		this.contactIsBussinessContactMobilePhone = contactIsBussinessContactMobilePhone;
	}

	/**
	 * @return the contactIsBussinessContactEmail
	 */
	public String getContactIsBussinessContactEmail() {
		return contactIsBussinessContactEmail;
	}

	/**
	 * @param contactIsBussinessContactEmail the contactIsBussinessContactEmail to set
	 */
	public void setContactIsBussinessContactEmail(
			String contactIsBussinessContactEmail) {
		this.contactIsBussinessContactEmail = contactIsBussinessContactEmail;
	}

	/**
	 * @return the contactMobilePhone
	 */
	public String getContactMobilePhone() {
		return contactMobilePhone;
	}

	/**
	 * @param contactMobilePhone the contactMobilePhone to set
	 */
	public void setContactMobilePhone(String contactMobilePhone) {
		this.contactMobilePhone = contactMobilePhone;
	}

	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	/**
	 * @return the contactUserEmail
	 */
	public String getContactUserEmail() {
		return contactUserEmail;
	}

	/**
	 * @param contactUserEmail the contactUserEmail to set
	 */
	public void setContactUserEmail(String contactUserEmail) {
		this.contactUserEmail = contactUserEmail;
	}

	/**
	 * @return the cmsCase
	 */
	public String getCmsCase() {
		return cmsCase;
	}

	/**
	 * @param cmsCase the cmsCase to set
	 */
	public void setCmsCase(String cmsCase) {
		this.cmsCase = cmsCase;
	}

	/**
	 * @return the unityNumber
	 */
	public String getUnityNumber() {
		return unityNumber;
	}

	/**
	 * @param unityNumber the unityNumber to set
	 */
	public void setUnityNumber(String unityNumber) {
		this.unityNumber = unityNumber;
	}

	/**
	 * @return the confirmAuthorizes
	 */
	public String getConfirmAuthorizes() {
		return confirmAuthorizes;
	}

	/**
	 * @param confirmAuthorizes the confirmAuthorizes to set
	 */
	public void setConfirmAuthorizes(String confirmAuthorizes) {
		this.confirmAuthorizes = confirmAuthorizes;
	}
	/**
	 * @return the installCaseId
	 */
	public String getInstallCaseId() {
		return installCaseId;
	}

	/**
	 * @param installCaseId the installCaseId to set
	 */
	public void setInstallCaseId(String installCaseId) {
		this.installCaseId = installCaseId;
	}

	/**
	 * @return the installCompleteDate
	 */
	public Timestamp getInstallCompleteDate() {
		return installCompleteDate;
	}

	/**
	 * @param installCompleteDate the installCompleteDate to set
	 */
	public void setInstallCompleteDate(Timestamp installCompleteDate) {
		this.installCompleteDate = installCompleteDate;
	}

	/**
	 * @return the isIatomsCreateCms
	 */
	public String getIsIatomsCreateCms() {
		return isIatomsCreateCms;
	}

	/**
	 * @param isIatomsCreateCms the isIatomsCreateCms to set
	 */
	public void setIsIatomsCreateCms(String isIatomsCreateCms) {
		this.isIatomsCreateCms = isIatomsCreateCms;
	}

	/**
	 * @return the preloadSerialNumber
	 */
	public String getPreloadSerialNumber() {
		return preloadSerialNumber;
	}

	/**
	 * @param preloadSerialNumber the preloadSerialNumber to set
	 */
	public void setPreloadSerialNumber(String preloadSerialNumber) {
		this.preloadSerialNumber = preloadSerialNumber;
	}

	/**
	 * @return the simSerialNumber
	 */
	public String getSimSerialNumber() {
		return simSerialNumber;
	}

	/**
	 * @param simSerialNumber the simSerialNumber to set
	 */
	public void setSimSerialNumber(String simSerialNumber) {
		this.simSerialNumber = simSerialNumber;
	}

	/**
	 * @return the installedPostCode
	 */
	public String getInstalledPostCode() {
		return installedPostCode;
	}

	/**
	 * @param installedPostCode the installedPostCode to set
	 */
	public void setInstalledPostCode(String installedPostCode) {
		this.installedPostCode = installedPostCode;
	}

	/**
	 * @return the contactPostCode
	 */
	public String getContactPostCode() {
		return contactPostCode;
	}

	/**
	 * @param contactPostCode the contactPostCode to set
	 */
	public void setContactPostCode(String contactPostCode) {
		this.contactPostCode = contactPostCode;
	}

	

	/**
	 * @return the postCodeId
	 */
	public String getPostCodeId() {
		return postCodeId;
	}

	/**
	 * @param postCodeId the postCodeId to set
	 */
	public void setPostCodeId(String postCodeId) {
		this.postCodeId = postCodeId;
	}

	/**
	 * @return the contactPostArea
	 */
	public String getContactPostArea() {
		return contactPostArea;
	}

	/**
	 * @param contactPostArea the contactPostArea to set
	 */
	public void setContactPostArea(String contactPostArea) {
		this.contactPostArea = contactPostArea;
	}

	/**
	 * @return the contactAreaCode
	 */
	public String getContactAreaCode() {
		return contactAreaCode;
	}

	/**
	 * @param contactAreaCode the contactAreaCode to set
	 */
	public void setContactAreaCode(String contactAreaCode) {
		this.contactAreaCode = contactAreaCode;
	}

	/**
	 * @return the installedPostCodeName
	 */
	public String getInstalledPostCodeName() {
		return installedPostCodeName;
	}

	/**
	 * @param installedPostCodeName the installedPostCodeName to set
	 */
	public void setInstalledPostCodeName(String installedPostCodeName) {
		this.installedPostCodeName = installedPostCodeName;
	}

	/**
	 * @return the contactPostCodeName
	 */
	public String getContactPostCodeName() {
		return contactPostCodeName;
	}

	/**
	 * @param contactPostCodeName the contactPostCodeName to set
	 */
	public void setContactPostCodeName(String contactPostCodeName) {
		this.contactPostCodeName = contactPostCodeName;
	}

	/**
	 * @return the apiTransactionId
	 */
	public String getApiTransactionId() {
		return apiTransactionId;
	}

	/**
	 * @param apiTransactionId the apiTransactionId to set
	 */
	public void setApiTransactionId(String apiTransactionId) {
		this.apiTransactionId = apiTransactionId;
	}

	/**
	 * @return the cmsResultMsg
	 */
	public String getCmsResultMsg() {
		return cmsResultMsg;
	}

	/**
	 * @param cmsResultMsg the cmsResultMsg to set
	 */
	public void setCmsResultMsg(String cmsResultMsg) {
		this.cmsResultMsg = cmsResultMsg;
	}

	/**
	 * @return the installContactAreaCode
	 */
	public String getInstallContactAreaCode() {
		return installContactAreaCode;
	}

	/**
	 * @param installContactAreaCode the installContactAreaCode to set
	 */
	public void setInstallContactAreaCode(String installContactAreaCode) {
		this.installContactAreaCode = installContactAreaCode;
	}

	/**
	 * @return the installContactAreaName
	 */
	public String getInstallContactAreaName() {
		return installContactAreaName;
	}

	/**
	 * @param installContactAreaName the installContactAreaName to set
	 */
	public void setInstallContactAreaName(String installContactAreaName) {
		this.installContactAreaName = installContactAreaName;
	}

	/**
	 * @return the hasArrive
	 */
	public String getHasArrive() {
		return hasArrive;
	}

	/**
	 * @param hasArrive the hasArrive to set
	 */
	public void setHasArrive(String hasArrive) {
		this.hasArrive = hasArrive;
	}

	/**
	 * @return the logisticsVendorName
	 */
	public String getLogisticsVendorName() {
		return logisticsVendorName;
	}

	/**
	 * @param logisticsVendorName the logisticsVendorName to set
	 */
	public void setLogisticsVendorName(String logisticsVendorName) {
		this.logisticsVendorName = logisticsVendorName;
	}

	/**
	 * @return the logisticsVendor
	 */
	public String getLogisticsVendor() {
		return logisticsVendor;
	}

	/**
	 * @param logisticsVendor the logisticsVendor to set
	 */
	public void setLogisticsVendor(String logisticsVendor) {
		this.logisticsVendor = logisticsVendor;
	}

	/**
	 * @return the logisticsNumber
	 */
	public String getLogisticsNumber() {
		return logisticsNumber;
	}

	/**
	 * @param logisticsNumber the logisticsNumber to set
	 */
	public void setLogisticsNumber(String logisticsNumber) {
		this.logisticsNumber = logisticsNumber;
	}

	/**
	 * @return the receiptType
	 */
	public String getReceiptType() {
		return receiptType;
	}

	/**
	 * @param receiptType the receiptType to set
	 */
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	/**
	 * @return the receiptTypeName
	 */
	public String getReceiptTypeName() {
		return receiptTypeName;
	}

	/**
	 * @param receiptTypeName the receiptTypeName to set
	 */
	public void setReceiptTypeName(String receiptTypeName) {
		this.receiptTypeName = receiptTypeName;
	}

	/**
	 * @return the softwareVersions
	 */
	public List<Parameter> getSoftwareVersions() {
		return softwareVersions;
	}

	/**
	 * @param softwareVersions the softwareVersions to set
	 */
	public void setSoftwareVersions(List<Parameter> softwareVersions) {
		this.softwareVersions = softwareVersions;
	}
	
}
