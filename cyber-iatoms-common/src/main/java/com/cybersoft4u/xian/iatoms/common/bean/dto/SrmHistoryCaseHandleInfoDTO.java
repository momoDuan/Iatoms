package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;
/**
 * Purpose: 案件歷史處理資料檔DTO
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2017/2/9
 * @MaintenancePersonnel CrissZhang
 */
public class SrmHistoryCaseHandleInfoDTO extends DataTransferObject<String> {
	
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 6140494835740300674L;
	
	/**
	 * Purpose: enum
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/2/23
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		HISTORY_ID("historyId"),
		CASE_ID("caseId"),
		CASE_CATEGORY("caseCategory"),
		CUSTOMER_ID("customerId"),
		CONTRACT_ID("contractId"),
		REQUIREMENT_NO("requirementNo"),
		HANDLED_TYPE("handledType"),
		INSTALL_TYPE("installType"),
		DTID("dtid"),
		COMPANY_ID("companyId"),
		CASE_TYPE("caseType"),
		EXPECTED_COMPLETION_DATE("expectedCompletionDate"),
		MERCHANT_CODE("merchantCode"),
		MERCHANT_ID("merchantId"),
		MERCHANT_HEADER_ID("merchantHeaderId"),
		INSTALLED_ADRESS("installedAdress"),
		IS_BUSSINESS_ADDRESS("isBussinessAddress"),
		INSTALLED_CONTACT("installedContact"),
		IS_BUSSINESS_CONTACT("isBussinessContact"),
		INSTALLED_CONTACT_PHONE("installedContactPhone"),
		IS_BUSSINESS_CONTACT_PHONE("isBussinessContactPhone"),
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
		GATEWAY("gateway"),
		NETMASK("netmask"),
		DESCRIPTION("description"),
		ATTENDANCE_TIMES("attendanceTimes"),
		REPAIR_TIMES("repairTimes"),
		REPAIR_REASON("repairReason"),
		UNINSTALL_TYPE("uninstallType"),
		UNINSTALLED_ADRESS("uninstalledAdress"),
		UNINSTALLED_IS_BUSSINESS_ADDRESS("uninstalledIsBussinessAddress"),
		UNINSTALLED_CONTACT("uninstalledContact"),
		UNINSTALLED_IS_BUSSINESS_CONTACT("uninstalledIsBussinessContact"),
		UNINSTALLED_CONTACT_PHONE("uninstalledContactPhone"),
		UNINSTALLED_IS_BUSSINESS_PHONE("uninstalledIsBussinessPhone"),
		CREATED_BY_ID("createdById"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_DATE("updatedDate"),
		CREATED_FINISH_DATE("createdFinishDate"),
		ACCEPTABLE_RESPONSE_DATE("acceptableResponseDate"),
		ACCEPTABLE_ARRIVE_DATE("acceptableArriveDate"),
		ACCEPTABLE_FINISH_DATE("acceptableFinishDate"),
		REAL_RESPONSE_DATE("realResponseDate"),
		REAL_ARRIVE_DATE("realArriveDate"),
		REAL_FINISH_DATE("realFinishDate"),
		CLOSE_DATE("closeDate"),
		NAME("name"),
		TID("tid"),
		SERIAL_NUMBER("serialNumber"),
		ASSET_NAME("assetName"),
		ASSET_ID("assetId"),
		TRANSACTION_TYPE("transactionType"),
		CONTRACT_CODE("contractCode"),
		AO_NAME("aoName"),
		FIRST_DESCRIPTION("firstDescription"),
		SECOND_DESCRIPTION("secondDescription"),
		THIRD_DESCRIPTION("thirdDescription");;

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
	
	public static void main(String[] args) {
		BindPageDataUtils.generateAttributeEnum(SrmHistoryCaseHandleInfoDTO.class);
	}
	/**
	 * ID
	 */
	private String historyId;
	/**
	 * 案件單號
	 */
	private String caseId;
	/**
	 * 案件種類
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
	 * 處理方式
	 */
	private String handledType;
	/**
	 * 裝機類型
	 */
	private String installType;
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 維護廠商
	 */
	private String companyId;
	/**
	 * 案件類型
	 */
	private String caseType;
	/**
	 * 預計完成日
	 */
	private Date expectedCompletionDate;
	/**
	 * 特店代號
	 */
	private String merchantCode;
	/**
	 * 特店id
	 */
	private String merchantId;
	/**
	 * 特店標頭主鍵
	 */
	private String merchantHeaderId;
	/**
	 * 裝機地址
	 */
	private String installedAdress;
	/**
	 * 是否同營業地址
	 */
	private String isBussinessAddress;
	/**
	 * 裝機聯絡人
	 */
	private String installedContact;
	/**
	 * 是否同特店聯絡人
	 */
	private String isBussinessContact;
	/**
	 * 裝機人聯絡電話
	 */
	private String installedContactPhone;
	/**
	 * 是否同特店聯絡人
	 */
	private String isBussinessContactPhone;
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
	 * 報修次數
	 */
	private Integer repairTimes;
	/**
	 * 保修原因
	 */
	private String repairReason;
	/**
	 * 拆機類型
	 */
	private String uninstallType;
	/**
	 * 拆機地址
	 */
	private String uninstalledAdress;
	/**
	 * 拆機是否同營業地址
	 */
	private String uninstalledIsBussinessAddress;
	/**
	 * 拆機聯絡人
	 */
	private String uninstalledContact;
	/**
	 * 拆機是否同特店聯絡人
	 */
	private String uninstalledIsBussinessContact;
	/**
	 * 拆機人聯絡電話
	 */
	private String uninstalledContactPhone;
	/**
	 * 拆機是否同特店聯絡人電話
	 */
	private String uninstalledIsBussinessPhone;
	/**
	 * 新增人員編號
	 */
	private String createdById;
	/**
	 * 新增日期
	 */
	private Timestamp createdDate;
	/**
	 * 異動人員編號
	 */
	private String updatedById;
	/**
	 * 異動日期
	 */
	private Date updatedDate;
	/**
	 * 進件完工日
	 */
	private Date createdFinishDate;
	/**
	 * 應回應時間
	 */
	private Date acceptableResponseDate;
	/**
	 * 應到場時間
	 */
	private Date acceptableArriveDate;
	/**
	 * 應完修時間
	 */
	private Date acceptableFinishDate;
	/**
	 * 實際回應時間
	 */
	private Date realResponseDate;
	/**
	 * 實際到場時間
	 */
	private Date realArriveDate;
	/**
	 * 實際完修時間
	 */
	private Date realFinishDate;
	/**
	 * 結案時間
	 */
	private Date closeDate;
	/**
	 * 特點名稱
	 */
	private String name;
	/**
	 * TID
	 */
	private String tid;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 設備名稱
	 */
	private String assetName;
	/**
	 * 設備ID
	*/
	private String assetId;
	/**
	 * 合約編號
	 */
	private String contractCode;
	/**
	 * 交易參數類別
	 */
	private String transactionType;
	/**
	 * AO_NAME
	 */
	private String aoName;
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
	 * 求償作業-將依據DTID查詢的設備信息放入SrmPaymentItemDTO
	 */
	private List<SrmPaymentItemDTO> srmPaymentItemDTOs;
	/**
	 * Constructor:無參構造函數
	 */
	public SrmHistoryCaseHandleInfoDTO() {
		super();
	}

	

	/**
	 * Constructor: 有參構造函數
	 */
	public SrmHistoryCaseHandleInfoDTO(String historyId, String caseId,
			String caseCategory, String customerId, String contractId,
			String requirementNo, String handledType, String installType,
			String dtid, String companyId, String caseType,
			Date expectedCompletionDate, String merchantCode,
			String merchantHeaderId, String installedAdress,
			String isBussinessAddress, String installedContact,
			String isBussinessContact, String installedContactPhone,
			String isBussinessContactPhone, String edcType,
			String softwareVersion, String builtInFeature, String multiModule,
			String peripherals, String peripheralsFunction,
			String ecrConnection, String peripherals2,
			String peripheralsFunction2, String connectionType,
			String peripherals3, String peripheralsFunction3,
			String localhostIp, String netVendorId, String gateway,
			String netmask, String description, Integer attendanceTimes,
			Integer repairTimes, String repairReason, String uninstallType,
			String uninstalledAdress, String uninstalledIsBussinessAddress,
			String uninstalledContact, String uninstalledIsBussinessContact,
			String uninstalledContactPhone, String uninstalledIsBussinessPhone,
			String createdById, Timestamp createdDate, String updatedById,
			Date updatedDate, Date createdFinishDate,
			Date acceptableResponseDate, Date acceptableArriveDate,
			Date acceptableFinishDate, Date realResponseDate,
			Date realArriveDate, Date realFinishDate, Date closeDate,
			String name, String tid, String serialNumber, String assetName,
			String contractCode) {
		super();
		this.historyId = historyId;
		this.caseId = caseId;
		this.caseCategory = caseCategory;
		this.customerId = customerId;
		this.contractId = contractId;
		this.requirementNo = requirementNo;
		this.handledType = handledType;
		this.installType = installType;
		this.dtid = dtid;
		this.companyId = companyId;
		this.caseType = caseType;
		this.expectedCompletionDate = expectedCompletionDate;
		this.merchantCode = merchantCode;
		this.merchantHeaderId = merchantHeaderId;
		this.installedAdress = installedAdress;
		this.isBussinessAddress = isBussinessAddress;
		this.installedContact = installedContact;
		this.isBussinessContact = isBussinessContact;
		this.installedContactPhone = installedContactPhone;
		this.isBussinessContactPhone = isBussinessContactPhone;
		this.edcType = edcType;
		this.softwareVersion = softwareVersion;
		this.builtInFeature = builtInFeature;
		this.multiModule = multiModule;
		this.peripherals = peripherals;
		this.peripheralsFunction = peripheralsFunction;
		this.ecrConnection = ecrConnection;
		this.peripherals2 = peripherals2;
		this.peripheralsFunction2 = peripheralsFunction2;
		this.connectionType = connectionType;
		this.peripherals3 = peripherals3;
		this.peripheralsFunction3 = peripheralsFunction3;
		this.localhostIp = localhostIp;
		this.netVendorId = netVendorId;
		this.gateway = gateway;
		this.netmask = netmask;
		this.description = description;
		this.attendanceTimes = attendanceTimes;
		this.repairTimes = repairTimes;
		this.repairReason = repairReason;
		this.uninstallType = uninstallType;
		this.uninstalledAdress = uninstalledAdress;
		this.uninstalledIsBussinessAddress = uninstalledIsBussinessAddress;
		this.uninstalledContact = uninstalledContact;
		this.uninstalledIsBussinessContact = uninstalledIsBussinessContact;
		this.uninstalledContactPhone = uninstalledContactPhone;
		this.uninstalledIsBussinessPhone = uninstalledIsBussinessPhone;
		this.createdById = createdById;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedDate = updatedDate;
		this.createdFinishDate = createdFinishDate;
		this.acceptableResponseDate = acceptableResponseDate;
		this.acceptableArriveDate = acceptableArriveDate;
		this.acceptableFinishDate = acceptableFinishDate;
		this.realResponseDate = realResponseDate;
		this.realArriveDate = realArriveDate;
		this.realFinishDate = realFinishDate;
		this.closeDate = closeDate;
		this.name = name;
		this.tid = tid;
		this.serialNumber = serialNumber;
		this.assetName = assetName;
		this.contractCode = contractCode;
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
	 * @return the handledType
	 */
	public String getHandledType() {
		return handledType;
	}

	/**
	 * @param handledType the handledType to set
	 */
	public void setHandledType(String handledType) {
		this.handledType = handledType;
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
	public Date getExpectedCompletionDate() {
		return expectedCompletionDate;
	}

	/**
	 * @param expectedCompletionDate the expectedCompletionDate to set
	 */
	public void setExpectedCompletionDate(Date expectedCompletionDate) {
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
	public String getUninstalledAdress() {
		return uninstalledAdress;
	}

	/**
	 * @param uninstalledAdress the uninstalledAdress to set
	 */
	public void setUninstalledAdress(String uninstalledAdress) {
		this.uninstalledAdress = uninstalledAdress;
	}

	/**
	 * @return the uninstalledIsBussinessAddress
	 */
	public String getUninstalledIsBussinessAddress() {
		return uninstalledIsBussinessAddress;
	}

	/**
	 * @param uninstalledIsBussinessAddress the uninstalledIsBussinessAddress to set
	 */
	public void setUninstalledIsBussinessAddress(
			String uninstalledIsBussinessAddress) {
		this.uninstalledIsBussinessAddress = uninstalledIsBussinessAddress;
	}

	/**
	 * @return the uninstalledContact
	 */
	public String getUninstalledContact() {
		return uninstalledContact;
	}

	/**
	 * @param uninstalledContact the uninstalledContact to set
	 */
	public void setUninstalledContact(String uninstalledContact) {
		this.uninstalledContact = uninstalledContact;
	}

	/**
	 * @return the uninstalledIsBussinessContact
	 */
	public String getUninstalledIsBussinessContact() {
		return uninstalledIsBussinessContact;
	}

	/**
	 * @param uninstalledIsBussinessContact the uninstalledIsBussinessContact to set
	 */
	public void setUninstalledIsBussinessContact(
			String uninstalledIsBussinessContact) {
		this.uninstalledIsBussinessContact = uninstalledIsBussinessContact;
	}

	/**
	 * @return the uninstalledContactPhone
	 */
	public String getUninstalledContactPhone() {
		return uninstalledContactPhone;
	}

	/**
	 * @param uninstalledContactPhone the uninstalledContactPhone to set
	 */
	public void setUninstalledContactPhone(String uninstalledContactPhone) {
		this.uninstalledContactPhone = uninstalledContactPhone;
	}

	/**
	 * @return the uninstalledIsBussinessPhone
	 */
	public String getUninstalledIsBussinessPhone() {
		return uninstalledIsBussinessPhone;
	}

	/**
	 * @param uninstalledIsBussinessPhone the uninstalledIsBussinessPhone to set
	 */
	public void setUninstalledIsBussinessPhone(String uninstalledIsBussinessPhone) {
		this.uninstalledIsBussinessPhone = uninstalledIsBussinessPhone;
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
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the createdFinishDate
	 */
	public Date getCreatedFinishDate() {
		return createdFinishDate;
	}

	/**
	 * @param createdFinishDate the createdFinishDate to set
	 */
	public void setCreatedFinishDate(Date createdFinishDate) {
		this.createdFinishDate = createdFinishDate;
	}

	/**
	 * @return the acceptableResponseDate
	 */
	public Date getAcceptableResponseDate() {
		return acceptableResponseDate;
	}

	/**
	 * @param acceptableResponseDate the acceptableResponseDate to set
	 */
	public void setAcceptableResponseDate(Date acceptableResponseDate) {
		this.acceptableResponseDate = acceptableResponseDate;
	}

	/**
	 * @return the acceptableArriveDate
	 */
	public Date getAcceptableArriveDate() {
		return acceptableArriveDate;
	}

	/**
	 * @param acceptableArriveDate the acceptableArriveDate to set
	 */
	public void setAcceptableArriveDate(Date acceptableArriveDate) {
		this.acceptableArriveDate = acceptableArriveDate;
	}

	/**
	 * @return the acceptableFinishDate
	 */
	public Date getAcceptableFinishDate() {
		return acceptableFinishDate;
	}

	/**
	 * @param acceptableFinishDate the acceptableFinishDate to set
	 */
	public void setAcceptableFinishDate(Date acceptableFinishDate) {
		this.acceptableFinishDate = acceptableFinishDate;
	}

	/**
	 * @return the realResponseDate
	 */
	public Date getRealResponseDate() {
		return realResponseDate;
	}

	/**
	 * @param realResponseDate the realResponseDate to set
	 */
	public void setRealResponseDate(Date realResponseDate) {
		this.realResponseDate = realResponseDate;
	}

	/**
	 * @return the realArriveDate
	 */
	public Date getRealArriveDate() {
		return realArriveDate;
	}

	/**
	 * @param realArriveDate the realArriveDate to set
	 */
	public void setRealArriveDate(Date realArriveDate) {
		this.realArriveDate = realArriveDate;
	}

	/**
	 * @return the realFinishDate
	 */
	public Date getRealFinishDate() {
		return realFinishDate;
	}

	/**
	 * @param realFinishDate the realFinishDate to set
	 */
	public void setRealFinishDate(Date realFinishDate) {
		this.realFinishDate = realFinishDate;
	}

	/**
	 * @return the closeDate
	 */
	public Date getCloseDate() {
		return closeDate;
	}

	/**
	 * @param closeDate the closeDate to set
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
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
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
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
	
}
