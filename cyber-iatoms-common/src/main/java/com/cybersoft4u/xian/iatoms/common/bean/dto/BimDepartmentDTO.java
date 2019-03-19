package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 部門維護DTO
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/6/15
 * @MaintenancePersonnel Amanda Wang
 */
public class BimDepartmentDTO extends DataTransferObject<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7202448105257707096L;
	/**
	 * Purpose: 枚举类型参数
	 * @author amandawang
	 * @since  JDK 1.6
	 * @date   2016/6/21
	 * @MaintenancePersonnel amandawang
	 */
	public static enum ATTRIBUTE {
		DEPT_CODE("deptCode"),
		DEPT_NAME("deptName"),
		REMARK("remark"),
		CONTACT("contact"),
		CONTACT_TEL("contactTel"),
		LOCATION("location"),
		ADDRESS("address"),
		CONTACT_EMAIL("contactEmail"),
		CREATE_USER_NAME("createUserName"),
		CREATE_DATE("createDate"),
		CREATE_USER("createUser"),
		CONTACT_FAX("contactFax"),
		UPDATE_USER("updateUser"),
		UPDATE_USER_NAME("updateUserName"),
		UPDATE_DATE("updateDate"),
		COMPANY_ID("companyId"), 
		VENDOR_ID("vendorId"), 
		COMPANY_NAME("companyName");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 部門ID
	 */
	private String deptCode;
	/**
	 * 公司ID
	 */
	private String companyId;
	
	/**
	 * 部門名稱
	 */
	private String deptName;
	/**
	 *  聯絡人
	 */
	private String contact;
	
	/**
	 * 地址-縣市
	 */
	private String location;
	
	/**
	 * 聯絡人地址
	 */
	private String address;
	
	/**
	 * 聯絡人email
	 */
	private String contactEmail;
	
	/**
	 * 聯絡人電話
	 */
	private String contactTel;
	
	/**
	 * 聯絡人傳真
	 */
	private String contactFax;
	
	/**
	 * 部門描述
	 */
	private String remark;	
	
	/**
	 * 創建人員
	 */
	private String createdById;
	
	/**
	 * 創建人員姓名
	 */
	private String createdByName;
	
	/**
	 * 創建日期
	 */
	private Date createdDate;
	
	/**
	 * 異動人員
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
	
	private String deleted = "N";
	
	/**
	 * 公司名
	 */
	private String companyName;
		
	/**
	 * Constructor:無參構造函數
	 */
	public BimDepartmentDTO() {
		super();
	}

	/**
	 * Constructor:有參構造函數
	 */
	public BimDepartmentDTO(String deptCode, String companyId, String deptName,
			String contact, String contactTel, String contactFax,
			String contactEmail, String location, String address,
			String remark, String createdById, String createdByName,
			Date createdDate, String updatedById, String updatedByName,
			Date updatedDate, String deleted) {
		this.deptCode = deptCode;
		this.companyId = companyId;
		this.deptName = deptName;
		this.contact = contact;
		this.contactTel = contactTel;
		this.contactFax = contactFax;
		this.contactEmail = contactEmail;
		this.location = location;
		this.address = address;
		this.remark = remark;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
	}

	/**
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}

	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
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
	 * @return the contactFax
	 */
	public String getContactFax() {
		return contactFax;
	}

	/**
	 * @param contactFax the contactFax to set
	 */
	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
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
}
