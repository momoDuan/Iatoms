package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;
import java.util.List;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: EDC交易參數查詢DTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/9/22
 * @MaintenancePersonnel CrissZhang
 */
public class EdcParameterDTO extends DataTransferObject<String>{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 2912628930628287890L;

	/**
	 * Purpose: EDC交易參數ATTRIBUTE
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2016/9/22
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		DTID("dtid"),
		COMPANY_ID("companyId"),
		COMPANY_NAME("companyName"),
		CASE_ID("caseId"),
		ASSET_STATUS("assetStatus"),
		MERCHANT_NAME("merchantName"),
		MER_ANNOUNCED_NAME("merAnnouncedName"),
		MERCHANT_CODE("merchantCode"),
		MERCHANT_STAGES_CODE("merchantStagesCode"),
		SERIAL_NUMBER("serialNumber"),
		INSTALLED_DATE("installedDate"),
		DISASSEMBLE_DATE("disassembleDate"),
		CUP_ENABLE_DATE("cupEnableDate"),
		CUP_DISABLE_DATE("cupDisableDate"),
		OPEN_TRANSACTION_LIST("openTransactionList"),
		INSTALL_ADDRESS("installAddress"),
		BUSINESS_ADDRESS("businessAddress"),
		MER_CONTACT("merContact"),
		MER_TEL1("merTel1"),
		MER_TEL2("merTel2"),
		MER_MOBILE_PHONE("merMobilePhone"),
		MER_AO_ID("merAoId"),
		BUSINESS_HOURS("businessHours"),
		MAINTAIN_COMPANY("maintainCompany"),
		EDC_TYPE("edcType"),
		SOFTWARE_VERSION("softwareVersion"),
		BUILT_IN_FEATURE("builtInFeature"),
		MULTI_MODULE("multiModule"),
		ECR_CONNECTION("ecrConnection"),
		COMM_MODE("commMode"),
		NET_VENDOR_ID("netVendorId"),
		LINE_NUMBER("lineNumber"),
		LOCALHOST_IP("localhostIp"),
		GATE_WAY("gateWay"),
		NETMASK("netmask"),
		CHANGE_PROJECT("changeProject"),
		INSTALLED_TYPE("installedType"),
		PERIPHERALS1("peripherals1"),
		PERIPHERALS_FUNCTION1("peripheralsFunction1"),
		PERIPHERALS2("peripherals2"),
		PERIPHERALS_FUNCTION2("peripheralsFunction2"),
		PERIPHERALS3("peripherals3"),
		PERIPHERALS_FUNCTION3("peripheralsFunction3");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * Constructor:無參構造函數
	 */
	public EdcParameterDTO(){
		
	}
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 客戶編號
	 */
	private String companyId;
	/**
	 * 客戶名稱
	 */
	private String companyName;
	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * 特點名稱
	 */
	private String merchantName;
	/**
	 * 表頭（同對外名稱）
	 */
	private String merAnnouncedName;
	/**
	 * 特店代號
	 */
	private String merchantCode;
	/**
	 * 特店分期代號
	 */
	private String merchantStagesCode;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 裝機日期
	 */
	private Date installedDate;
	/**
	 * 拆機日期
	 */
	private Date disassembleDate;
	/**
	 * CUP 啟用日期
	 */
	private Date cupEnableDate;
	/**
	 * CUP 移除日期
	 */
	private Date cupDisableDate;
	/**
	 * 已開放交易清單
	 */
	private String openTransactionList;
	/**
	 * 裝機地址
	 */
	private String installAddress;
	/**
	 * 營業地址
	 */
	private String businessAddress;
	/**
	 * 特店聯絡人
	 */
	private String merContact;
	/**
	 * 特店聯絡人電話1
	 */
	private String merTel1;
	/**
	 * 特店聯絡人電話2
	 */
	private String merTel2;
	/**
	 * 特店聯絡人行動電話
	 */
	private String merMobilePhone;
	/**
	 * AO 人員
	 */
	private String merAoId;
	/**
	 * 營業時間
	 */
	private String businessHours;
	/**
	 * 維護廠商
	 */
	private String maintainCompany;
	/**
	 * 刷卡機型
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
	 * ECR 連線
	 */
	private String ecrConnection;
	/**
	 * 通訊模式
	 */
	private String commMode;
	/**
	 * 寬頻連線
	 */
	private String netVendorId;
	/**
	 * 刷卡機 IP 位址
	 */
	private String localhostIp;
	/**
	 * 刷卡機 GateWay
	 */
	private String gateWay;
	/**
	 * 刷卡機 Netmask
	 */
	private String netmask;
	/**
	 * 週邊設備1
	 */
	private String peripherals1;
	/**
	 * 週邊設備功能1
	 */
	private String peripheralsFunction1;
	/**
	 * 週邊設備2
	 */
	private String peripherals2;
	/**
	 * 週邊設備功能2
	 */
	private String peripheralsFunction2;
	/**
	 * 週邊設備3
	 */
	private String peripherals3;
	/**
	 * 週邊設備功能3
	 */
	private String peripheralsFunction3;
	/**
	 * 異動項目
	 */
	private String changeProject;
	/**
	 * 裝機類型
	 */
	private String installedType;
	
	/**
	 * 交易參數DTO對象
	 */
	private SrmCaseNewTransactionParameterDTO transactionParameterDTO;
	/**
	 * 交易參數DTO對象列表
	 */
	private List<SrmCaseNewTransactionParameterDTO> transactionParameterDTOs;
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
	 * @return the merchantStagesCode
	 */
	public String getMerchantStagesCode() {
		return merchantStagesCode;
	}
	/**
	 * @param merchantStagesCode the merchantStagesCode to set
	 */
	public void setMerchantStagesCode(String merchantStagesCode) {
		this.merchantStagesCode = merchantStagesCode;
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
	 * @return the installedDate
	 */
	public Date getInstalledDate() {
		return installedDate;
	}
	/**
	 * @param installedDate the installedDate to set
	 */
	public void setInstalledDate(Date installedDate) {
		this.installedDate = installedDate;
	}
	/**
	 * @return the disassembleDate
	 */
	public Date getDisassembleDate() {
		return disassembleDate;
	}
	/**
	 * @param disassembleDate the disassembleDate to set
	 */
	public void setDisassembleDate(Date disassembleDate) {
		this.disassembleDate = disassembleDate;
	}
	/**
	 * @return the cupEnableDate
	 */
	public Date getCupEnableDate() {
		return cupEnableDate;
	}
	/**
	 * @param cupEnableDate the cupEnableDate to set
	 */
	public void setCupEnableDate(Date cupEnableDate) {
		this.cupEnableDate = cupEnableDate;
	}
	/**
	 * @return the cupDisableDate
	 */
	public Date getCupDisableDate() {
		return cupDisableDate;
	}
	/**
	 * @param cupDisableDate the cupDisableDate to set
	 */
	public void setCupDisableDate(Date cupDisableDate) {
		this.cupDisableDate = cupDisableDate;
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
	 * @return the merContact
	 */
	public String getMerContact() {
		return merContact;
	}
	/**
	 * @param merContact the merContact to set
	 */
	public void setMerContact(String merContact) {
		this.merContact = merContact;
	}
	/**
	 * @return the merTel1
	 */
	public String getMerTel1() {
		return merTel1;
	}
	/**
	 * @param merTel1 the merTel1 to set
	 */
	public void setMerTel1(String merTel1) {
		this.merTel1 = merTel1;
	}
	/**
	 * @return the merTel2
	 */
	public String getMerTel2() {
		return merTel2;
	}
	/**
	 * @param merTel2 the merTel2 to set
	 */
	public void setMerTel2(String merTel2) {
		this.merTel2 = merTel2;
	}
	/**
	 * @return the merMobilePhone
	 */
	public String getMerMobilePhone() {
		return merMobilePhone;
	}
	/**
	 * @param merMobilePhone the merMobilePhone to set
	 */
	public void setMerMobilePhone(String merMobilePhone) {
		this.merMobilePhone = merMobilePhone;
	}
	/**
	 * @return the merAoId
	 */
	public String getMerAoId() {
		return merAoId;
	}
	/**
	 * @param merAoId the merAoId to set
	 */
	public void setMerAoId(String merAoId) {
		this.merAoId = merAoId;
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
	 * @return the commMode
	 */
	public String getCommMode() {
		return commMode;
	}
	/**
	 * @param commMode the commMode to set
	 */
	public void setCommMode(String commMode) {
		this.commMode = commMode;
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
	 * @return the gateWay
	 */
	public String getGateWay() {
		return gateWay;
	}
	/**
	 * @param gateWay the gateWay to set
	 */
	public void setGateWay(String gateWay) {
		this.gateWay = gateWay;
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
	 * @return the peripherals1
	 */
	public String getPeripherals1() {
		return peripherals1;
	}
	/**
	 * @param peripherals1 the peripherals1 to set
	 */
	public void setPeripherals1(String peripherals1) {
		this.peripherals1 = peripherals1;
	}
	/**
	 * @return the peripheralsFunction1
	 */
	public String getPeripheralsFunction1() {
		return peripheralsFunction1;
	}
	/**
	 * @param peripheralsFunction1 the peripheralsFunction1 to set
	 */
	public void setPeripheralsFunction1(String peripheralsFunction1) {
		this.peripheralsFunction1 = peripheralsFunction1;
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
	 * @return the changeProject
	 */
	public String getChangeProject() {
		return changeProject;
	}
	/**
	 * @param changeProject the changeProject to set
	 */
	public void setChangeProject(String changeProject) {
		this.changeProject = changeProject;
	}
	/**
	 * @return the installedType
	 */
	public String getInstalledType() {
		return installedType;
	}
	/**
	 * @param installedType the installedType to set
	 */
	public void setInstalledType(String installedType) {
		this.installedType = installedType;
	}
	/**
	 * @return the transactionParameterDTO
	 */
	public SrmCaseNewTransactionParameterDTO getTransactionParameterDTO() {
		return transactionParameterDTO;
	}
	/**
	 * @param transactionParameterDTO the transactionParameterDTO to set
	 */
	public void setTransactionParameterDTO(
			SrmCaseNewTransactionParameterDTO transactionParameterDTO) {
		this.transactionParameterDTO = transactionParameterDTO;
	}
	/**
	 * @return the transactionParameterDTOs
	 */
	public List<SrmCaseNewTransactionParameterDTO> getTransactionParameterDTOs() {
		return transactionParameterDTOs;
	}
	/**
	 * @param transactionParameterDTOs the transactionParameterDTOs to set
	 */
	public void setTransactionParameterDTOs(
			List<SrmCaseNewTransactionParameterDTO> transactionParameterDTOs) {
		this.transactionParameterDTOs = transactionParameterDTOs;
	}
	
}
