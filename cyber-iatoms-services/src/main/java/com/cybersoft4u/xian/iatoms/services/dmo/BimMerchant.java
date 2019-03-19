package com.cybersoft4u.xian.iatoms.services.dmo;

import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
/**
 * Purpose: 客戶特店的dmo
 * @author David
 * @since  JDK 1.6
 * @date   2016/6/14
 * @MaintenancePersonnel David
 */
public class BimMerchant extends DomainModelObject<String,MerchantDTO>{
	private static final long serialVersionUID = 1L;

	private String merchantId;
	private String companyId;
	private String merchantCode;
	private String name;
	private String remark;
	private String createdById;
	private String createdByName;
	private Date createdDate;
	private String updatedById;
	private String updatedByName;
	private Date updatedDate;
	private String deleted = "N";
	private String unityNumber;

	public BimMerchant() {
	}

	public BimMerchant(String merchantId) {
		this.merchantId = merchantId;
	}

	public BimMerchant(String merchantId, String companyId,
			String merchantCode, String name, String remark,
			String createdById, String createdByName, Date createdDate,
			String updatedById, String updatedByName, Date updatedDate,
			String deleted) {
		this.merchantId = merchantId;
		this.companyId = companyId;
		this.merchantCode = merchantCode;
		this.name = name;
		this.remark = remark;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
	}

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getMerchantCode() {
		return this.merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatedById() {
		return this.createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return this.createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedById() {
		return this.updatedById;
	}

	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}

	public String getUpdatedByName() {
		return this.updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getDeleted() {
		return this.deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
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
	
}
