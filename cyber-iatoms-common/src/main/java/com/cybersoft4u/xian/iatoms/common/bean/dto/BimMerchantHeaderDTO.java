package com.cybersoft4u.xian.iatoms.common.bean.dto;
import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 特店表頭維護DTO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/8/8
 * @MaintenancePersonnel echomou
 */
public class BimMerchantHeaderDTO extends DataTransferObject<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8119520869284568672L;
	
	public static enum ATTRIBUTE {
		MERCHANT_HEADER_ID("merchantHeaderId"),
		VIP_NAME("vipName"),
		IS_VIP("isVip"),
		MERCHANT_ID("merchantId"),
		HEADER_NAME("headerName"),
		MERCHANT_AREA_NAME("merchantAreaName"),
		AREA("area"),
		AREA_NAME("areaName"),
		CONTACT("contact"),
		CONTACT_TEL("contactTel"),
		CONTACT_TEL2("contactTel2"),
		PHONE("phone"),
		BUSINESS_ADDRESS("businessAddress"),
		OPEN_HOUR("openHour"),
		CLOSE_HOUR("closeHour"),
		AO_NAME("aoName"),
		AO_EMAIL("aoemail"),
		REMARK("remark"),
		CUSTOMER_NAME("customerName"),
		COMPANY_ID("companyId"),
		MERCHANT_CODE("merchantCode"),
		NAME("name"),
		LOCATION("location"),
		MERCHANT_LOCATION_NAME("merchantLocationName"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		DELETED("deleted"),
		ADDRESS("address"),
		UNITY_NUMBER("unityNumber"),
		CONTACT_EMAIL("contactEmail"),
		POST_CODE_ID("postCodeId"),
		POST_NAME("postName"),
		POST_CODE("postCode");
		private String value;
		/**
		 * Constructor:
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 特店主鍵編號
	 */
	private String merchantHeaderId;
	/**
	 * 是否爲vip名稱
	 */
	private String vipName;
	/**
	 * 是否爲vip編號
	 */
	private String isVip = "N";
	/**
	 * 客戶特點主鍵編號
	 */
	private String merchantId;
	/**
	 * 表頭
	 */
	private String headerName;
	/**
	 * 特店所在區域名稱
	 */
	private String merchantAreaName;
	/**
	 * 特店所在區域編號
	 */
	private String area;
	/**
	 * 
	 */
	private String areaName;
	/**
	 * 特店聯絡人
	 */
	private String contact;
	/**
	 * 特店聯絡人電話
	 */
	private String contactTel;
	/**
	 * 特店聯絡人電話2
	 */
	private String contactTel2;
	/**
	 * 特店聯絡人Email
	 */
	private String contactEmail;
	/**
	 * 聯繫電話
	 */
	private String phone;
	/**
	 * 營業地址
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
	 * AO人員
	 */
	private String aoName;
	/**
	 * AO Email
	 */
	private String aoemail;
	/**
	 * 備注
	 */
	private String remark;
	/**
	 * 客戶名稱
	 */
	private String customerName;
	/**
	 * 客戶編號
	 */
	private String companyId;
	/**
	 * 特店編號
	 */
	private String merchantCode;
	/**
	 * 分期特店編號
	 *//*
	private String stagesMerchantCode;*/
	/**
	 * 特店名稱
	 */
	private String name;
	/**
	 * 縣市
	 */
	private String location;
	/**
	 * 縣市編號MERCHANT_LOCATION
	 */
	private String merchantLocationName;
	/**
	 * 具體地址
	 */
	private String address;

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
	 * 是否被刪除
	 */
	private String deleted = "N";
	private String isAssetMManage;
	/**
	 * 統一編號
	 */
	private String unityNumber;
	/**
	 * 郵遞區號編號
	 */
	private String postCodeId;
	/**
	 * 郵遞區域
	 */
	private String postName;
	/**
	 * 郵遞區號
	 */
	private String postCode;
	
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
	 * Constructor:
	 */
	public BimMerchantHeaderDTO() {
		super();
	}

	/**
	 * Constructor:
	 */
	public BimMerchantHeaderDTO(String merchantHeaderId, String isVip,
			String merchantId, String merchantAnnouncedName,
			String merchantArea, String merchantContact,
			String merchantContactPhone, String merchantBusinessAddr,
			String openHour, String closeHour, String aoName, String aoEmail,
			String remark, String customerId, String mid, String registeredName) {
		this.merchantHeaderId = merchantHeaderId;
		this.isVip = isVip;
		this.merchantId = merchantId;
		this.headerName = merchantAnnouncedName;
		this.area = merchantArea;
		this.contact = merchantContact;
		this.contactTel = merchantContactPhone;
		this.businessAddress = merchantBusinessAddr;
		this.openHour = openHour;
		this.closeHour = closeHour;
		this.aoName = aoName;
		this.aoemail = aoEmail;
		this.remark = remark;
		this.companyId = customerId;
		this.merchantCode = mid;
		this.name = registeredName;
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
	 * @return the merchantLocationName
	 */
	public String getMerchantLocationName() {
		return merchantLocationName;
	}

	/**
	 * @param merchantLocationName the merchantLocationName to set
	 */
	public void setMerchantLocationName(String merchantLocationName) {
		this.merchantLocationName = merchantLocationName;
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
	 * @return the vipName
	 */
	public String getVipName() {
		return vipName;
	}

	/**
	 * @param vipName the vipName to set
	 */
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	/**
	 * @return the merchantAreaName
	 */
	public String getMerchantAreaName() {
		return merchantAreaName;
	}

	/**
	 * @param merchantAreaName the merchantAreaName to set
	 */
	public void setMerchantAreaName(String merchantAreaName) {
		this.merchantAreaName = merchantAreaName;
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
	 * @return the areaName
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * @param areaName the areaName to set
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	 * @return the stagesMerchantCode
	 */
	/*public String getStagesMerchantCode() {
		return stagesMerchantCode;
	}

	*//**
	 * @param stagesMerchantCode the stagesMerchantCode to set
	 *//*
	public void setStagesMerchantCode(String stagesMerchantCode) {
		this.stagesMerchantCode = stagesMerchantCode;
	}*/

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
	 * @return the isAssetMManage
	 */
	public String getIsAssetMManage() {
		return isAssetMManage;
	}

	/**
	 * @param isAssetMManage the isAssetMManage to set
	 */
	public void setIsAssetMManage(String isAssetMManage) {
		this.isAssetMManage = isAssetMManage;
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

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
}
