package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 合約維護DTO 
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/5/17
 * @MaintenancePersonnel CarrieDuan
 */
public class BimContractDTO extends DataTransferObject<String>{

	/**
	 *  系统日志记录物件  
	 */
	private static final long serialVersionUID = -753399749641595848L;
	
	/**
	 * 
	 * Purpose: 枚举类型参数
	 * @author allenchen
	 * @since  JDK 1.7
	 * @date   2016/5/18
	 * @MaintenancePersonnel allenchen
	 */
	public static enum ATTRIBUTE {
		CONTRACT_ID("contractId"),
		CONTRACT_CODE("contractCode"),
		CUSTOMER_ID("customerId"),
		CUSTOMER_NAME("customerName"),
		VENDOR_ID("vendorId"),		//已棄用，改用company
		VENDOR_NAME("vendorName"),	//已棄用
		COMPANY_ID("companyId"),
		COMPANY_NAME("companyName"),
		COMPANY_IDS("companyIds"),
		COMPANY_NAMES("companyNames"),
		UNITY_NUMBER("unityNumber"),
		CONTRACT_TYPE_ID("contractTypeId"),
		CONTRACT_TYPE_NAME("contractTypeName"),
		CONTRACT_PRICE("contractPrice"),
		CONTRACT_DATE("contractDate"),
		START_DATE("startDate"),
		END_DATE("endDate"),
		END_DATE_STRING("endDateString"),
		CANCEL_DATE("cancelDate"),
		COMMENT("comment"),
		CREATE_USER("createUser"),
		CREATE_USER_NAME("createUserName"),
		CREATE_DATE("createDate"),
		UPDATE_USER("updateUser"),
		UPDATE_USER_NAME("updateUserName"),
		UPDATE_DATE("updateDate"),
		CONTRACT_STATUS("contractStatus"),
		PAY_MODE("payMode"),
		CONTRACT_STATUS_NAME("contractStatusName"),
		PAY_MODE_NAME("payModeName"),
		PAY_REQUIRE("payRequire"),
		FACTORY_WARRANTY("factoryWarranty"),
		CUSTOMER_WARRANTY("customerWarranty"),
		WINDOW_ONE("window1"),
		WINDOW_ONE_CONN("window1Connection"),
		WINDOW_TWO("window2"),
		WINDOW_TWO_CONN("window2Connection"),
		LIST_CONTRACT_MANAGE_D_T_OS("listContractManageDTOs"),
		WORK_HOUR_START_1("workHourStart1"),
		WORK_HOUR_END_1("workHourEnd1"),
		WORK_HOUR_START_2("workHourStart2"),
		WORK_HOUR_END_2("workHourEnd2"),
		ATTACHED_FILE("attachedFile"),
		ATTACHED_FILES("attachedFiles"),
		WORK_HOUR_1("workHour1"),
		WORK_HOUR_2("workHour2"),
		FILE_MAP("fileMap"),
		FILE_NAME("fileName"),
		DELETED("deleted"),
		TO_MAIL("toMail"),
		CC_MAIL("ccMail"),
		MAIL_SUBJECT("mailSubject"),
		TEXT_TEMPLATE("textTemplate"),
		MAIL_CONTEXT("mailContext"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		PRICE("price");
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	
	
	/**
	 * 合約Id
	 */
	private String contractId;
	
	/**
	 * 合約編號
	 */
	private String contractCode;
	/**
	 * 合約ID
	 */
	private String hiddenContractId;
	/**
	 * 客戶ID
	 */
	private String customerId;
	/**
	 * 客戶Name
	 */
	private String customerName;
	/**
	 * 廠商ID
	 */
	private String companyId;
	/**
	 * 廠商名稱
	 */
	private String companyName;
	/**
	 * 廠商ID集合
	 */
	private String companyIds;
	/**
	 * 廠商名稱集合
	 */
	private String companyNames;
	/**
	 * 統一編號
	 */
	private String unityNumber;
	/**
	 * 合約類型名稱
	 */
	private String contractTypeId;
	/**
	 * 合約類型名稱
	 */
	private String contractTypeName;
	/**
	 * 和月價格
	 */
	private Long contractPrice;
	/**
	 * 合約時間起迄
	 */
	private String contractDate;
	
	/**
	 * 約定上班時間1
	 */
	private String workHour1;
	
	/**
	 * 約定上班時間2
	 */
	private String workHour2;
	
	/**
	 * 合約時間起
	 */
	private Date startDate;
	/**
	 * 合約時間迄
	 */
	private Date endDate;
	/**
	 * 取消日期
	 */
	private Date cancelDate;
	/**
	 * 說明
	 */
	private String comment;
	/**
	 * 創建人編號
	 */
	private String createUser;
	/**
	 * 創建人名稱
	 */
	private String createUserName;
	/**
	 * 創建時間
	 */
	private String createDate;
	/**
	 * 更新人account
	 */
	private String updateUser;
	/**
	 * 更新人員名稱
	 */
	private String updateUserName;
	/**
	 * 更新時間
	 */
	private String updateDate;
	/**
	 * 合約狀態
	 */
	private String contractStatus;
	/**
	 * 付款方式
	 */
	private String payMode;
	/**
	 * 合約狀態名稱
	 */
	private String contractStatusName;
	/**
	 * 付款模式名稱
	 */
	private String payModeName;
	/**
	 * 付款條件
	 */
	private String payRequire;
	/**
	 * 原廠保固期限
	 */
	private Integer factoryWarranty;
	/**
	 * 客戶保固期限
	 */
	private Integer customerWarranty;
	/**
	 * 窗口1
	 */
	private String window1;
	/**
	 * 窗口1联系方式
	 */
	private String window1Connection;
	/**
	 * 窗口2
	 */
	private String window2;
	/**
	 * 窗口2联系方式
	 */
	private String window2Connection;
	/**
	 * 約定上班時間1start
	 */
	private String workHourStart1;
	/**
	 * 約定上班時間1end	
	 */
	private String workHourEnd1;
	/**
	 * 約定上班時間2
	 */
	private String workHourStart2;
	/**
	 * 約定上班時間2end
	 */
	private String workHourEnd2;
	/**
	 * 附件路徑
	 */
	private String attachedFile;
	/**
	 * 刪除標記
	 */
	private String deleted = "N";
	
	/**
	 * 合約設備集合
	 */
	private List<BimContractAssetDTO> assetTypeDTOs;//设备DTO
	/**
	 * 合約附加文件
	 */
	private Map<String, MultipartFile> fileMap;
	/**
	 * 問價名稱
	 */
	private String fileName;
	
	/**
	 * 附件問題列表
	 */
	private List<String> attachedFiles;
	/**
	 * 廠商集合
	 */
	private List<String> vendors;
	/**
	 * 合約類型集合
	 */
	private List<String> contractTypes;
	/**
	 * 廠商名稱
	 */
	private String vendorName;
	/**
	 * 收件地址
	 */
	private String toMail;
	/**
	 * CC地址
	 */
	private String ccMail;
	/**
	 * 郵件主題
	 */
	private String mailSubject;
	/**
	 * 郵件文本
	 */
	private String textTemplate;
	/**
	 * 
	 */
	private String mailContext;
	/**
	 * 
	 */
	private String endDateString;
	/**
	 * 售價
	 */
	private String price;
	/**
	 * 創建者
	 */
	private String createdById;
	/**
	 * 創建者姓名
	 */
	private String createdByName;
	/**
	 * 創建時間
	 */
	private Timestamp createdDate;
	/**
	 * 修改者
	 */
	private String updatedById;
	/**
	 * 修改者姓名
	 */
	private String updatedByName;
	/**
	 * 修改時間
	 */
	private Timestamp updatedDate;
	
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
	 * @return the contractType
	 */
	public String getContractTypeId() {
		return contractTypeId;
	}
	/**
	 * @param contractType the contractType to set
	 */
	public void setContractTypeId(String contractTypeId) {
		this.contractTypeId = contractTypeId;
	}
	/**
	 * @return the contractPrice
	 */
	public Long getContractPrice() {
		return contractPrice;
	}
	/**
	 * @param contractPrice the contractPrice to set
	 */
	public void setContractPrice(Long contractPrice) {
		this.contractPrice = contractPrice;
	}

	/**
	 * @return the contractDate
	 */
	public String getContractDate() {
		return contractDate;
	}
	/**
	 * @param contractDate the contractDate to set
	 */
	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}
	

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @param cancelDate the cancelDate to set
	 */
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
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
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
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
	public String getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * @return the contractStatus
	 */
	public String getContractStatus() {
		return contractStatus;
	}
	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
	/**
	 * @return the payMode
	 */
	public String getPayMode() {
		return payMode;
	}
	/**
	 * @param payMode the payMode to set
	 */
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	/**
	 * @return the payRequire
	 */
	public String getPayRequire() {
		return payRequire;
	}
	/**
	 * @param payRequire the payRequire to set
	 */
	public void setPayRequire(String payRequire) {
		this.payRequire = payRequire;
	}
	
	/**
	 * @return the window1
	 */
	public String getWindow1() {
		return window1;
	}
	/**
	 * @param window1 the window1 to set
	 */
	public void setWindow1(String window1) {
		this.window1 = window1;
	}
	/**
	 * @return the window1Connection
	 */
	public String getWindow1Connection() {
		return window1Connection;
	}
	/**
	 * @param window1Connection the window1Connection to set
	 */
	public void setWindow1Connection(String window1Connection) {
		this.window1Connection = window1Connection;
	}
	/**
	 * @return the window2
	 */
	public String getWindow2() {
		return window2;
	}
	/**
	 * @param window2 the window2 to set
	 */
	public void setWindow2(String window2) {
		this.window2 = window2;
	}
	/**
	 * @return the window2Connection
	 */
	public String getWindow2Connection() {
		return window2Connection;
	}
	/**
	 * @param window2Connection the window2Connection to set
	 */
	public void setWindow2Connection(String window2Connection) {
		this.window2Connection = window2Connection;
	}
	/**
	 * @return the assetTypeDTOs
	 */
	public List<BimContractAssetDTO> getAssetTypeDTOs() {
		return assetTypeDTOs;
	}
	/**
	 * @param assetTypeDTOs the assetTypeDTOs to set
	 */
	public void setAssetTypeDTOs(List<BimContractAssetDTO> assetTypeDTOs) {
		this.assetTypeDTOs = assetTypeDTOs;
	}
	/**
	 * @return the contractTypeName
	 */
	public String getContractTypeName() {
		return contractTypeName;
	}
	/**
	 * @param contractTypeName the contractTypeName to set
	 */
	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}
	/**
	 * @return the contractStatusName
	 */
	public String getContractStatusName() {
		return contractStatusName;
	}
	/**
	 * @param contractStatusName the contractStatusName to set
	 */
	public void setContractStatusName(String contractStatusName) {
		this.contractStatusName = contractStatusName;
	}
	/**
	 * @return the payModeName
	 */
	public String getPayModeName() {
		return payModeName;
	}
	/**
	 * @param payModeName the payModeName to set
	 */
	public void setPayModeName(String payModeName) {
		this.payModeName = payModeName;
	}
	/**
	 * @return the hiddenContractId
	 */
	public String getHiddenContractId() {
		return hiddenContractId;
	}
	/**
	 * @param hiddenContractId the hiddenContractId to set
	 */
	public void setHiddenContractId(String hiddenContractId) {
		this.hiddenContractId = hiddenContractId;
	}
	/**
	 * @return the cancelDate
	 */
	public Date getCancelDate() {
		return cancelDate;
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
	 * @return the factoryWarranty
	 */
	public Integer getFactoryWarranty() {
		return factoryWarranty;
	}
	/**
	 * @param factoryWarranty the factoryWarranty to set
	 */
	public void setFactoryWarranty(Integer factoryWarranty) {
		this.factoryWarranty = factoryWarranty;
	}
	/**
	 * @return the customerWarranty
	 */
	public Integer getCustomerWarranty() {
		return customerWarranty;
	}
	/**
	 * @param customerWarranty the customerWarranty to set
	 */
	public void setCustomerWarranty(Integer customerWarranty) {
		this.customerWarranty = customerWarranty;
	}
	/**
	 * @return the workHourStart1
	 */
	public String getWorkHourStart1() {
		return workHourStart1;
	}
	/**
	 * @param workHourStart1 the workHourStart1 to set
	 */
	public void setWorkHourStart1(String workHourStart1) {
		this.workHourStart1 = workHourStart1;
	}
	/**
	 * @return the workHourEnd1
	 */
	public String getWorkHourEnd1() {
		return workHourEnd1;
	}
	/**
	 * @param workHourEnd1 the workHourEnd1 to set
	 */
	public void setWorkHourEnd1(String workHourEnd1) {
		this.workHourEnd1 = workHourEnd1;
	}
	/**
	 * @return the workHourStart2
	 */
	public String getWorkHourStart2() {
		return workHourStart2;
	}
	/**
	 * @param workHourStart2 the workHourStart2 to set
	 */
	public void setWorkHourStart2(String workHourStart2) {
		this.workHourStart2 = workHourStart2;
	}
	/**
	 * @return the workHourEnd2
	 */
	public String getWorkHourEnd2() {
		return workHourEnd2;
	}
	/**
	 * @param workHourEnd2 the workHourEnd2 to set
	 */
	public void setWorkHourEnd2(String workHourEnd2) {
		this.workHourEnd2 = workHourEnd2;
	}
	/**
	 * @return the attachedFile
	 */
	public String getAttachedFile() {
		return attachedFile;
	}
	/**
	 * @param attachedFile the attachedFile to set
	 */
	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
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
	 * @return the companyIds
	 */
	public String getCompanyIds() {
		return companyIds;
	}
	/**
	 * @param companyIds the companyIds to set
	 */
	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}
	/**
	 * @return the companyNames
	 */
	public String getCompanyNames() {
		return companyNames;
	}
	/**
	 * @param companyNames the companyNames to set
	 */
	public void setCompanyNames(String companyNames) {
		this.companyNames = companyNames;
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
	 * @return the attachedFiles
	 */
	public List<String> getAttachedFiles() {
		return attachedFiles;
	}
	/**
	 * @param attachedFiles the attachedFiles to set
	 */
	public void setAttachedFiles(List<String> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}
	/**
	 * @return the workHour1
	 */
	public String getWorkHour1() {
		return workHour1;
	}
	/**
	 * @param workHour1 the workHour1 to set
	 */
	public void setWorkHour1(String workHour1) {
		this.workHour1 = workHour1;
	}
	/**
	 * @return the workHour2
	 */
	public String getWorkHour2() {
		return workHour2;
	}
	/**
	 * @param workHour2 the workHour2 to set
	 */
	public void setWorkHour2(String workHour2) {
		this.workHour2 = workHour2;
	}
	/**
	 * @return the fileMap
	 */
	public Map<String, MultipartFile> getFileMap() {
		return fileMap;
	}
	/**
	 * @param fileMap the fileMap to set
	 */
	public void setFileMap(Map<String, MultipartFile> fileMap) {
		this.fileMap = fileMap;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the vendors
	 */
	public List<String> getVendors() {
		return vendors;
	}
	/**
	 * @param vendors the vendors to set
	 */
	public void setVendors(List<String> vendors) {
		this.vendors = vendors;
	}
	/**
	 * @return the contractTypes
	 */
	public List<String> getContractTypes() {
		return contractTypes;
	}
	/**
	 * @param contractTypes the contractTypes to set
	 */
	public void setContractTypes(List<String> contractTypes) {
		this.contractTypes = contractTypes;
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
	 * @return the textTemplate
	 */
	public String getTextTemplate() {
		return textTemplate;
	}
	/**
	 * @param textTemplate the textTemplate to set
	 */
	public void setTextTemplate(String textTemplate) {
		this.textTemplate = textTemplate;
	}
	/**
	 * @return the endDateString
	 */
	public String getEndDateString() {
		return endDateString;
	}
	/**
	 * @param endDateString the endDateString to set
	 */
	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the createdById
	 */
	public String getCreatedById() {
		return createdById;
	}
	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}
	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	/**
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}
	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}
	/**
	 * @return the updatedDate
	 */
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
