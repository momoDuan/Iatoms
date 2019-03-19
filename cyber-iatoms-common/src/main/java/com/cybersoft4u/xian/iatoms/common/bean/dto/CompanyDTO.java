package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.List;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 公司基本訊息維護DTO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月1日
 * @MaintenancePersonnel ElvaHe
 */
public class CompanyDTO extends DataTransferObject<String>{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -1767167485793143660L;
	
	public static enum ATTRIBUTE {
		COMPANY_ID("companyId"),
		COMPANY_TYPE("companyType"),
		COMPANY_TYPE_NAME("companyTypeName"),
		COMPANY_CODE("companyCode"),
		SHORT_NAME("shortName"),
		UNITY_NUMBER("unityNumber"),
		INVOICE_HEADER("invoiceHeader"),
		LEADER("leader"),
		TEL("tel"),
		FAX("fax"),
		APPLY_DATE("applyDate"),
		PAY_DATE("payDate"),
		CONTACT("contact"),
		CONTACT_TEL("contactTel"),
		CONTACT_EMAIL("contactEmail"),
		CUSTOMER_CODE("customerCode"),
		COMPANY_EMAIL("companyEmail"),
		DTID_TYPE("dtidType"),
		DTID_TYPE_NAME("dtidTypeName"),
		ADDRESS("address"),
		INVOICE_ADDRESS("invoiceAddress"),
		REMARK("remark"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		AUTHENTICATION_TYPE("authenticationType"),
		AUTHENTICATION_TYPE_NAME("authenticationTypeName"),
		ADDRESS_LOCATION("addressLocation"),
		INVOICE_ADDRESS_LOCATION("invoiceAddressLocation"),
		ADDRESS_LOCATION_NAME("addressLocationName"),
		IS_NOTIFY_AO("isNotifyAo"),
		INVOICE_ADDRESS_LOCATION_NAME("invoiceAddressLocationName")
		;

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 公司基本訊息主鍵編號
	 */
	private String companyId;
	/**
	 * 公司類型名稱
	 */
	private String companyTypeName;
	/**
	 * 公司類型值
	 */
	private String companyType;
	/**
	 * 公司代號
	 */
	private String companyCode;
	/**
	 * 公司簡稱
	 */
	private String shortName;
	/**
	 * 統一編號
	 */
	private String unityNumber;
	/**
	 * 發票擡頭
	 */
	private String invoiceHeader;
	/**
	 * 負責人
	 */
	private String leader;
	/**
	 * 公司電話
	 */
	private String tel;
	/**
	 * 公司傳真
	 */
	private String fax;
	/**
	 * 請款日
	 */
	private String applyDate;
	/**
	 * 付款日
	 */
	private String payDate;
	/**
	 * 聯絡人
	 */
	private String contact;
	/**
	 * 聯絡人電話
	 */
	private String contactTel;
	/**
	 * 聯絡人Email
	 */
	private String contactEmail;
	/**
	 * 客戶碼
	 */
	private String customerCode;
	/**
	 * 公司Email
	 */
	private String companyEmail;
	/**
	 * DTID方式名稱
	 */
	private String dtidTypeName;
	/**
	 * DTID方式值
	 */
	private String dtidType;
	/**
	 * 登入驗證方式
	 */
	private String authenticationType;
	/**
	 * 登入驗證方式的名稱
	 */
	private String authenticationTypeName;
	/**
	 * 公司地址-縣市
	 */
	private String addressLocation;
	/**
	 * 公司地址
	 */
	private String addressLocationName;
	/**
	 * 公司地址name
	 */
	private String address;
	/**
	 * 發票地址-縣市
	 */
	private String invoiceAddressLocation;
	/**
	 * 發票地址
	 */
	private String invoiceAddressLocationName;
	/**
	 * 發票地址
	 */
	private String invoiceAddress;
	/**
	 * 說明
	 */
	private String remark;
	/**
	 * 創建者編號
	 */
	private String createdById;
	/**
	 * 創建者姓名
	 */
	private String createdByName;
	/**
	 * 創建日期
	 */
	private Timestamp createdDate;
	/**
	 * 更新者編號
	 */
	private String updatedById;
	/**
	 * 更新者姓名
	 */
	private String updatedByName;
	/**
	 * 更新日期
	 */
	private Timestamp updatedDate;
	/**
	 * 刪除標誌
	 */
	private String deleted = "N";
	/**
	 * 公司類型DTOList
	 */
	private List<CompanyTypeDTO> companyTypeDTOs;
	/**
	 * 選中的公司類型集合
	 */
	private List<String> companyTypes;
	
	/**
	 * 是否通知AO
	 */
	private String isNotifyAo = "N";
	
	/**
	 * Constructor:無參構造函數
	 */
	public CompanyDTO() {
	}
	
	/**
	 * Constructor:有參構造函數
	 */
	public CompanyDTO(String companyType, String authenticationType) {
		this.companyType = companyType;
		this.authenticationType = authenticationType;
	}
	
	/**
	 * 
	 * Constructor: 有參構造
	 */
	public CompanyDTO(String companyId, String companyType, String companyTypeName, String companyCode,
			String shortName, String unityNumber, String invoiceHeader,
			String leader, String tel, String fax, String applyDate,
			String payDate, String contact, String contactTel,
			String contactEmail, String customerCode, String companyEmail,
			String dtidTypeName, String dtidType, String authenticationType, String authenticationTypeName,
			String addressLocation, String addressLocationName, String address,
			String invoiceAddressLocation, String invoiceAddressLocationName,
			String invoiceAddress, String remark, String createdById,
			String createdByName, Timestamp createdDate, String updatedById,
			String updatedByName, Timestamp updatedDate, String deleted,
			List<CompanyTypeDTO> companyTypeDTOs, List<String> companyTypes) {
		this.companyId = companyId;
		this.companyType = companyType;
		this.companyTypeName = companyTypeName;
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
		this.dtidTypeName = dtidTypeName;
		this.dtidType = dtidType;
		this.authenticationType = authenticationType;
		this.authenticationTypeName = authenticationTypeName;
		this.addressLocation = addressLocation;
		this.addressLocationName = addressLocationName;
		this.address = address;
		this.invoiceAddressLocation = invoiceAddressLocation;
		this.invoiceAddressLocationName = invoiceAddressLocationName;
		this.invoiceAddress = invoiceAddress;
		this.remark = remark;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
		this.companyTypeDTOs = companyTypeDTOs;
		this.companyTypes = companyTypes;
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
	 * @return the companyType
	 */
	public String getCompanyType() {
		return companyType;
	}

	/**
	 * @param companyType the companyType to set
	 */
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
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
	 * @return the dtidTypeName
	 */
	public String getDtidTypeName() {
		return dtidTypeName;
	}

	/**
	 * @param dtidTypeName the dtidTypeName to set
	 */
	public void setDtidTypeName(String dtidTypeName) {
		this.dtidTypeName = dtidTypeName;
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
	 * @return the companyTypeDTOs
	 */
	public List<CompanyTypeDTO> getCompanyTypeDTOs() {
		return companyTypeDTOs;
	}

	/**
	 * @param companyTypeDTOs the companyTypeDTOs to set
	 */
	public void setCompanyTypeDTOs(List<CompanyTypeDTO> companyTypeDTOs) {
		this.companyTypeDTOs = companyTypeDTOs;
	}

	/**
	 * @return the addressLocationName
	 */
	public String getAddressLocationName() {
		return addressLocationName;
	}

	/**
	 * @param addressLocationName the addressLocationName to set
	 */
	public void setAddressLocationName(String addressLocationName) {
		this.addressLocationName = addressLocationName;
	}

	/**
	 * @return the invoiceAddressLocationName
	 */
	public String getInvoiceAddressLocationName() {
		return invoiceAddressLocationName;
	}

	/**
	 * @param invoiceAddressLocationName the invoiceAddressLocationName to set
	 */
	public void setInvoiceAddressLocationName(String invoiceAddressLocationName) {
		this.invoiceAddressLocationName = invoiceAddressLocationName;
	}

	/**
	 * @return the companyTypeName
	 */
	public String getCompanyTypeName() {
		return companyTypeName;
	}

	/**
	 * @param companyTypeName the companyTypeName to set
	 */
	public void setCompanyTypeName(String companyTypeName) {
		this.companyTypeName = companyTypeName;
	}

	/**
	 * @return the authenticationTypeName
	 */
	public String getAuthenticationTypeName() {
		return authenticationTypeName;
	}

	/**
	 * @param authenticationTypeName the authenticationTypeName to set
	 */
	public void setAuthenticationTypeName(String authenticationTypeName) {
		this.authenticationTypeName = authenticationTypeName;
	}

	public List<String> getCompanyTypes() {
		return companyTypes;
	}

	public void setCompanyTypes(List<String> companyTypes) {
		this.companyTypes = companyTypes;
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
