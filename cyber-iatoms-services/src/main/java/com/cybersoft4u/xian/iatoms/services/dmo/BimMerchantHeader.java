package com.cybersoft4u.xian.iatoms.services.dmo;


import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;

/**
 * Purpose: 特店表頭維護
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/6/8
 * @MaintenancePersonnel jasonzhou
 */
public class BimMerchantHeader extends DomainModelObject<String, BimMerchantHeaderDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4457304606071630506L;
	/**
	 * 特店表頭主鍵
	 */
	private String merchantHeaderId;
	/**
	 * 特店主鍵
	 */
	private String merchantId;
	/**
	 * 是否VIP
	 */
	private String isVip = "N";
	/**
	 * 表頭名字
	 */
	private String headerName;
	/**
	 * 地區
	 */
	private String area;
	/**
	 * 聯繫人
	 */
	private String contact;
	/**
	 * 聯繫電話
	 */
	private String contactTel;
	/**
	 * 聯繫電話2
	 */
	private String contactTel2;
	/**
	 * 聯繫人手機
	 */
	private String phone;
	/**
	 * 聯繫人Email
	 */
	private String contactEmail;
	/**
	 * 縣市
	 */
	private String location;
	/**
	 * 位置
	 */
	private String businessAddress;
	/**
	 * 營業時間起
	 */
	private String openHour;
	/**
	 * 營業時間迄
	 */
	private String closeHour;
	/**
	 * AO名字
	 */
	private String aoName;
	/**
	 * aoemail
	 */
	private String aoemail;
	/**
	 * 說明
	 */
	private String remark;
	/**
	 * 創造者id
	 */
	private String createdById;
	/**
	 * 創造者name
	 */
	private String createdByName;
	/**
	 * 創造日期
	 */
	private Date createdDate;
	/**
	 * 更新者id
	 */
	private String updatedById;
	/**
	 * 更新者name
	 */
	private String updatedByName;
	/**
	 * 更新日期
	 */
	private Date updatedDate;
	private String deleted = "N";
	/**
	 * 郵遞區號編號
	 */
	private String postCodeId;
	/**
	 * Constructor:
	 */
	public BimMerchantHeader() {
		super();
	}
	/**
	 * Constructor:
	 */
	public BimMerchantHeader(String merchantHeaderId) {
		this.merchantHeaderId = merchantHeaderId;
	}
	/**
	 * Constructor:
	 */
	public BimMerchantHeader(String merchantHeaderId, String merchantId,
			String isVip, String headerName, String area, String contact,
			String contactTel,String contactTel2,String phone, String location, String businessAddress,
			String openHour, String closeHour, String aoName, String aoemail,
			String remark, String createdById, String createdByName,
			Date createdDate, String updatedById, String updatedByName,
			Date updatedDate, String deleted, String postCodeId) {
		this.merchantHeaderId = merchantHeaderId;
		this.merchantId = merchantId;
		this.isVip = isVip;
		this.headerName = headerName;
		this.area = area;
		this.contact = contact;
		this.contactTel = contactTel;
		this.contactTel2 = contactTel2;
		this.phone = phone;
		this.location = location;
		this.businessAddress = businessAddress;
		this.openHour = openHour;
		this.closeHour = closeHour;
		this.aoName = aoName;
		this.aoemail = aoemail;
		this.remark = remark;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
		this.postCodeId = postCodeId;
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
	public String getPostCodeId() {
		return postCodeId;
	}
	public void setPostCodeId(String postCodeId) {
		this.postCodeId = postCodeId;
	}
	
}
