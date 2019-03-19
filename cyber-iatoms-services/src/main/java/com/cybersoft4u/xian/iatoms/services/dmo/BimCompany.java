package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;

import cafe.core.dmo.DomainModelObject;

/**
 * Purpose: 公司訊息維護
 * @author jasonzhou
 * @since  JDK 1.6
 * @date   2016-6-16
 * @MaintenancePersonnel jasonzhou
 */
public class BimCompany extends DomainModelObject<String, CompanyDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -955911134385006927L;
	
	private String companyId;
	private String companyCode;
	private String shortName;
	private String unityNumber;
	private String invoiceHeader;
	private String leader;
	private String tel;
	private String fax;
	private String applyDate;
	private String payDate;
	private String contact;
	private String contactTel;
	private String contactEmail;
	private String customerCode;
	private String companyEmail;
	private String dtidType;
	private String authenticationType;
	private String addressLocation;
	private String address;
	private String invoiceAddressLocation;
	private String invoiceAddress;
	private String remark;
	private String createdById;
	private String createdByName;
	private Timestamp createdDate;
	private String updatedById;
	private String updatedByName;
	private Timestamp updatedDate;
	private String deleted = "N";
	private String isNotifyAo = "N";
	
	/**
	 * Constructor:
	 */
	public BimCompany() {
	}
	/**
	 * Constructor:
	 */
	public BimCompany(String companyId, String companyCode, String shortName,
			String unityNumber, String invoiceHeader, String tel, String customerCode,
			String companyEmail) {
		this.companyId = companyId;
		this.companyCode = companyCode;
		this.shortName = shortName;
		this.unityNumber = unityNumber;
		this.invoiceHeader = invoiceHeader;
		this.tel = tel;
		this.customerCode = customerCode;
		this.companyEmail = companyEmail;
	}

	/**
	 * Constructor:
	 */
	public BimCompany(String companyId, String companyCode, String shortName, String unityNumber, String invoiceHeader,
			String leader, String tel, String fax, String applyDate, String payDate, String contact, String contactTel,
			String contactEmail, String customerCode, String companyEmail, String dtidType, String authenticationType,
			String addressLocation, String address, String invoiceAddressLocation, String invoiceAddress, String remark,
			String createdById, String createdByName, Timestamp createdDate, String updatedById, String updatedByName,
			Timestamp updatedDate, String deleted, String isNotifyAo) {
		super();
		this.companyId = companyId;
		this.companyCode = companyCode;
		this.shortName = shortName;
		this.unityNumber = unityNumber;
		this.invoiceHeader = invoiceHeader;
		this.leader = leader;
		this.tel = tel;
		this.fax = fax;
		this.applyDate = applyDate;
		this.payDate = payDate;
		this.contact = contact;
		this.contactTel = contactTel;
		this.contactEmail = contactEmail;
		this.customerCode = customerCode;
		this.companyEmail = companyEmail;
		this.dtidType = dtidType;
		this.authenticationType = authenticationType;
		this.addressLocation = addressLocation;
		this.address = address;
		this.invoiceAddressLocation = invoiceAddressLocation;
		this.invoiceAddress = invoiceAddress;
		this.remark = remark;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
		this.isNotifyAo = isNotifyAo;
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
	 * @return the invoiceHeader
	 */
	public String getInvoiceHeader() {
		return invoiceHeader;
	}
	/**
	 * @param invoiceHeader the invoiceHeader to set
	 */
	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}
	/**
	 * @return the leader
	 */
	public String getLeader() {
		return leader;
	}
	/**
	 * @param leader the leader to set
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * @return the applyDate
	 */
	public String getApplyDate() {
		return applyDate;
	}
	/**
	 * @param applyDate the applyDate to set
	 */
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	/**
	 * @return the payDate
	 */
	public String getPayDate() {
		return payDate;
	}
	/**
	 * @param payDate the payDate to set
	 */
	public void setPayDate(String payDate) {
		this.payDate = payDate;
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
	 * @return the companyEmail
	 */
	public String getCompanyEmail() {
		return companyEmail;
	}
	/**
	 * @param companyEmail the companyEmail to set
	 */
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}
	/**
	 * @return the dtidType
	 */
	public String getDtidType() {
		return dtidType;
	}
	/**
	 * @param dtidType the dtidType to set
	 */
	public void setDtidType(String dtidType) {
		this.dtidType = dtidType;
	}
	/**
	 * @return the authenticationType
	 */
	public String getAuthenticationType() {
		return authenticationType;
	}
	/**
	 * @param authenticationType the authenticationType to set
	 */
	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
	/**
	 * @return the addressLocation
	 */
	public String getAddressLocation() {
		return addressLocation;
	}
	/**
	 * @param addressLocation the addressLocation to set
	 */
	public void setAddressLocation(String addressLocation) {
		this.addressLocation = addressLocation;
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
	 * @return the invoiceAddressLocation
	 */
	public String getInvoiceAddressLocation() {
		return invoiceAddressLocation;
	}
	/**
	 * @param invoiceAddressLocation the invoiceAddressLocation to set
	 */
	public void setInvoiceAddressLocation(String invoiceAddressLocation) {
		this.invoiceAddressLocation = invoiceAddressLocation;
	}
	/**
	 * @return the invoiceAddress
	 */
	public String getInvoiceAddress() {
		return invoiceAddress;
	}
	/**
	 * @param invoiceAddress the invoiceAddress to set
	 */
	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
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
	 * @return the isNotifyAo
	 */
	public String getIsNotifyAo() {
		return isNotifyAo;
	}
	/**
	 * @param isNotifyAo the isNotifyAo to set
	 */
	public void setIsNotifyAo(String isNotifyAo) {
		this.isNotifyAo = isNotifyAo;
	}
}
