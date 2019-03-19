package com.cybersoft4u.xian.iatoms.services.dmo;

import java.util.Date;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO;

import cafe.core.dmo.DomainModelObject;

/**
 * Purpose: 客訴管理
 * @author  nicklin
 * @since	JDK 1.7
 * @date	2018/03/02
 * @MaintenancePersonnel cybersoft
 */
public class SrmComplaint extends DomainModelObject<String, ComplaintDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6470983851226629454L;

	private String caseId;
	private String customerId;
	private String customerName;
	private String merchantId;
	private String merchantCode;
	private String merchantName;
	private String companyId;
	private String companyName;
	private String userName;
	private String complaintStaff;
	private String complaintContent;
	private String handleContent;
	private String createdById;
	private String createdByName;
	private Date createdDate;
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;
	private String tid;
	private Date complaintDate;
	private String contactWay;
	private String isVip;
	private String questionType;
	private String isCustomer;
	private Integer customerAmount;
	private String isVendor;
	private Integer vendorAmount;
	private String deleted;

	/**
	 * Constructor:無參數建構子
	 */
	public SrmComplaint() {
	}
	
	/**
	 * Constructor:有參數建構子
	 */
	public SrmComplaint(String caseId, String customerId, String merchantId,
			String companyId, String complaintStaff, String contactPhone,
			String complaintContent) {
		this.caseId = caseId; 
		this.customerId = customerId;
		this.merchantId = merchantId;
		this.companyId = companyId;
		this.complaintStaff = complaintStaff;
		this.complaintContent = complaintContent;
	}
	
	/**
	 * Constructor:有參數建構子
	 */
	public SrmComplaint(String caseId, String customerId, String customerName, String merchantId, String companyId,
			String companyName, String userName, String complaintStaff, String complaintContent, 
			String handleContent, String createdById, String createdByName, Date createdDate, String updatedById, 
			String updatedByName, Date updatedDate, String tid, Date complaintDate, String contactWay, String isVip,
			String questionType, String isCustomer, Integer customerAmount, String isVendor, Integer vendorAmount, String deleted) {
		super();
		this.caseId = caseId;
		this.customerId = customerId;
		this.customerName = customerName;
		this.merchantId = merchantId;
		this.companyId = companyId;
		this.companyName = companyName;
		this.userName = userName;
		this.complaintStaff = complaintStaff;
		this.complaintContent = complaintContent;
		this.handleContent = handleContent;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.tid = tid;
		this.complaintDate = complaintDate;
		this.contactWay = contactWay;
		this.isVip = isVip;
		this.questionType = questionType;
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
