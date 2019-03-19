package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 客訴管理DTO
 * @author nicklin
 * @since  JDK 1.7
 * @date   2018/02/27
 * @MaintenancePersonnel cybersoft
 */
public class ComplaintDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2176978892338905560L;
	/**
	 * Purpose:枚舉類型參數
	 * @author nicklin
	 * @since  JDK 1.7
	 * @date   2018/02/27
	 * @MaintenancePersonnel cybersoft
	 */
	public static enum ATTRIBUTE {
		CASE_ID("caseId"),
		CUSTOMER_ID("customerId"),
		CUSTOMER_NAME("customerName"),
		MERCHANT_ID("merchantId"),
		MERCHANT_CODE("merchantCode"),
		MERCHANT_NAME("merchantName"),
		COMPANY_ID("companyId"),
		COMPANY_NAME("companyName"),
		USER_NAME("userName"),
		COMPLAINT_STAFF("complaintStaff"),
		COMPLAINT_CONTENT("complaintContent"),
		HANDLE_CONTENT("handleContent"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		FILE_MAP("fileMap"),
		FILE_NAME("fileName"),
		COMPLAINT_DATE("complaintDate"),
		CONTACT_WAY("contactWay"),
		IS_VIP("isVip"),
		TID("tid"),
		QUESTION_TYPE("questionType"),
		QUESTION_TYPE_NAME("questionTypeName"),
		IS_CUSTOMER("isCustomer"),
		CUSTOMER_AMOUNT("customerAmount"),
		IS_VENDOR("isVendor"),
		VENDOR_AMOUNT("vendorAmount"),
		DELETED("deleted");
		
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 客訴編號
	 */
	private String caseId;
	/**
	 * 客戶
	 */
	private String customerId;
	/**
	 * 客戶名稱
	 */
	private String customerName;
	/**
	 * 特店主鍵
	 */
	private String merchantId;
	/**
	 * 特店代號
	 */
	private String merchantCode;
	/**
	 * 特店名稱
	 */
	private String merchantName;
	/**
	 * 歸責廠商
	 */
	private String companyId;
	/**
	 * 歸責廠商名稱
	 */
	private String companyName;
	/**
	 * 歸責名稱
	 */
	private String userName;
	/**
	 * 申訴人員
	 */
	private String complaintStaff;
	/**
	 * 客訴內容
	 */
	private String complaintContent;
	/**
	 * 處理說明
	 */
	private String handleContent;
	/**
	 * 新增人員帳號
	 */
	private String createdById;
	/**
	 * 新增人員姓名
	 */
	private String createdByName;
	/**
	 * 新增日期
	 */
	private Date createdDate;
	/**
	 * 異動人員帳號
	 */
	private String updatedById;
	/**
	 * 異動人員姓名
	 */
	private String updatedByName;
	/**
	 * 異動日期
	 */
	private Date updatedDate;
	/**
	 * 附加文件
	 */
	private Map<String, MultipartFile> fileMap;
	/**
	 * 文件名稱
	 */
	private String fileName;
	/**
	 * 發生日期
	 */
	private Date complaintDate;
	/**
	 * 聯繫方式
	 */
	private String contactWay;
	/**
	 * VIP註記
	 */
	private String isVip;
	/**
	 * TID/DTID
	 */
	private String tid;
	/**
	 * 問題分類
	 */
	private String questionType;
	/**
	 * 問題分類名稱
	 */
	private String questionTypeName;
	/**
	 * 賠償客戶
	 */
	private String isCustomer;
	/**
	 * 賠償金額
	 */
	private Integer customerAmount;
	/**
	 * 廠商罰款
	 */
	private String isVendor;
	/**
	 * 罰款金額
	 */
	private Integer vendorAmount;
	/**
	 * 作廢註記
	 */
	private String deleted;
	
	/**
	 * Constructor:無參數建構子
	 */
	public ComplaintDTO() {
		super();
	}
	/**
	 * Constructor:有參數建構子
	 */
	public ComplaintDTO(String caseId, String customerId, String customerName, String merchantId,
			String companyId, String companyName, String complaintStaff, String complaintContent,
			String handleContent, String createdById, String createdByName, Date createdDate, String updatedById, 
			String updatedByName, Date updatedDate, Date complaintDate, String contactWay, String isVip,
			String tid, String questionType, String questionTypeName, String isCustomer, Integer customerAmount,
			String isVendor, Integer vendorAmount, String deleted) {
		this.caseId = caseId;
		this.customerId = customerId;
		this.customerName = customerName;
		this.merchantId = merchantId;
		this.companyId = companyId;
		this.companyName = companyName;
		this.complaintStaff = complaintStaff;
		this.complaintContent = complaintContent;
		this.handleContent = handleContent;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.complaintDate = complaintDate;
		this.contactWay = contactWay;
		this.isVip = isVip;
		this.tid = tid;
		this.questionType = questionType;
		this.questionTypeName = questionTypeName;
		this.isCustomer = isCustomer;
		this.customerAmount = customerAmount;
		this.isVendor = isVendor;
		this.vendorAmount = vendorAmount;
		this.deleted = deleted;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the complaintStaff
	 */
	public String getComplaintStaff() {
		return complaintStaff;
	}
	/**
	 * @param complaintStaff the complaintStaff to set
	 */
	public void setComplaintStaff(String complaintStaff) {
		this.complaintStaff = complaintStaff;
	}
	/**
	 * @return the complaintContent
	 */
	public String getComplaintContent() {
		return complaintContent;
	}
	/**
	 * @param complaintContent the complaintContent to set
	 */
	public void setComplaintContent(String complaintContent) {
		this.complaintContent = complaintContent;
	}
	/**
	 * @return the handleContent
	 */
	public String getHandleContent() {
		return handleContent;
	}
	/**
	 * @param handleContent the handleContent to set
	 */
	public void setHandleContent(String handleContent) {
		this.handleContent = handleContent;
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
	 * @return the complaintDate
	 */
	public Date getComplaintDate() {
		return complaintDate;
	}
	/**
	 * @param complaintDate the complaintDate to set
	 */
	public void setComplaintDate(Date complaintDate) {
		this.complaintDate = complaintDate;
	}
	/**
	 * @return the contactWay
	 */
	public String getContactWay() {
		return contactWay;
	}
	/**
	 * @param contactWay the contactWay to set
	 */
	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
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
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}
	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	/**
	 * @return the questionTypeName
	 */
	public String getQuestionTypeName() {
		return questionTypeName;
	}
	/**
	 * @param questionTypeName the questionTypeName to set
	 */
	public void setQuestionTypeName(String questionTypeName) {
		this.questionTypeName = questionTypeName;
	}
	/**
	 * @return the isCustomer
	 */
	public String getIsCustomer() {
		return isCustomer;
	}
	/**
	 * @param isCustomer the isCustomer to set
	 */
	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}
	/**
	 * @return the customerAmount
	 */
	public Integer getCustomerAmount() {
		return customerAmount;
	}
	/**
	 * @param customerAmount the customerAmount to set
	 */
	public void setCustomerAmount(Integer customerAmount) {
		this.customerAmount = customerAmount;
	}
	/**
	 * @return the isVendor
	 */
	public String getIsVendor() {
		return isVendor;
	}
	/**
	 * @param isVendor the isVendor to set
	 */
	public void setIsVendor(String isVendor) {
		this.isVendor = isVendor;
	}
	/**
	 * @return the vendorAmount
	 */
	public Integer getVendorAmount() {
		return vendorAmount;
	}
	/**
	 * @param vendorAmount the vendorAmount to set
	 */
	public void setVendorAmount(Integer vendorAmount) {
		this.vendorAmount = vendorAmount;
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
}
