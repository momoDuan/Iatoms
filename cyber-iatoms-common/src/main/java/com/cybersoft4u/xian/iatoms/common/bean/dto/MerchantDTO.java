package com.cybersoft4u.xian.iatoms.common.bean.dto;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 客戶特店的DTO
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/5/25
 * @MaintenancePersonnel HermanWang
 */
public class MerchantDTO extends DataTransferObject<String>{
private static final long serialVersionUID = 1L;
	
	/**
	 * 定義的一一對應的常量
	 */
	public static enum ATTRIBUTE {
		CUSTOMER_ID("customerId"),
		//MID("mId"),
		SMID("smId"),
		REGISTERED_NAME("registeredName"),
		MERCHANT_HEADER_ID("merchantHeaderId"),
		VIP_NAME("vipName"),
		IS_VIP("isVip"),
		MERCHANT_ID("merchantId"),
		HEADER_NAME("headerName"),
		MERCHANT_AREA_NAME("merchantAreaName"),
		AREA("area"),
		CONTACT("contact"),
		CONTACT_TEL("contactTel"),
		BUSINESS_ADDRESS("businessAddress"),
		OPEN_HOUR("openHour"),
		CLOSE_HOUR("closeHour"),
		AO_NAME("aoName"),
		AO_EMAIL("aoEmail"),
		REMARK("remark"),
		CUSTOMER_NAME("customerName"),
		COMPANY_ID("companyId"),
		MERCHANT_CODE("merchantCode"),
		STAGES_MERCHANT_CODE("stagesMerchantCode"),
		NAME("name"),
		LOCATION("location"),
		MERCHANT_LOCATION_NAME("merchantLocationName"),
		SHORT_NAME("shortName"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		DELETED("deleted"),
		ADDRESS("address"),
		EXPORT_REPEAT_MERCHANT("exportRepeatMerchant"),
		UNITY_NUMBER("unityNumber");
		/**
		 * value
		 */
		private String value;
		/**
		 * @param value the value to set
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
		
	};
	/**
	 * 特店所在區域編號
	 */
	private String area;
	/**
	 * 客戶特店的主鍵Id
	 */
	private String merchantId;
	/**
	 * 客戶id
	 */
	private String customerId;
	/**
	 * 特店代號code
	 */
	private String merchantCode;
	/**
	 * 分期特店代號
	 */
	private String stagesMerchantCode;
	/**
	 * 特店代號
	 */
	//private String mId;
	/**
	 * 特店註冊名稱
	 */
	private String registeredName;
	/**
	 * 備註
	 */
	private String remark;
	/**
	 * 客戶名稱
	 */
	private String customerName;
	/**
	 * 特店名稱
	 */
	private String name;
	/**
	 * 刪除標誌
	 */
	private String deleted = "N";
	/**
	 * 特店表頭DTO
	 */
	private BimMerchantHeaderDTO bimMerchantHeaderDTO;
	/**
	 * 特店表頭DTOlist
	 */
	private List<BimMerchantHeaderDTO> bimMerchantHeaderDTOs;
	/**
	 * 縣市
	 */
	private String location;
	/**
	 * 表頭
	 */
	private String headerName;
	/**
	 * 特店表頭id
	 */
	private String merchantHeaderId;
	/**
	 * 是否vip
	 */
	private String isVip;
	/**
	 * 特店聯絡人
	 */
	private String contact;
	/**
	 * 特店聯絡人電話
	 */
	private String contactTel;
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
	private String aoEmail;
	/**
	 * smId
	 */
	private String smId;
	/**
	 * 客戶
	 */
	private String shortName;
	/**
	 * 公司id
	 */
	private String companyId;
	/**
	 * 表頭的map
	 */
	private Map<String, String> headMap = new HashMap<String, String>();
	
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
	 * 匯入是否特店重複
	 */
	private String exportRepeatMerchant;
	/**
	 * 統一編號
	 */
	private String unityNumber;
	/**
	 * 無參的構造方法
	 */
	public MerchantDTO() {
	}

	/**
	 * 有參的構造方法
	 */
	public MerchantDTO(String merchantId, String customerId,
			String registeredName, String remark, String customerName) {
		this.merchantId = merchantId;
		this.customerId = customerId;
		this.registeredName = registeredName;
		this.remark = remark;
		this.customerName = customerName;
	}
	
	/**
	 * @return the smId
	 */
	public String getSmId() {
		return smId;
	}

	/**
	 * @param smId the smId to set
	 */
	public void setSmId(String smId) {
		this.smId = smId;
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
	 * @return the aoEmail
	 */
	public String getAoEmail() {
		return aoEmail;
	}

	/**
	 * @param aoEmail the aoEmail to set
	 */
	public void setAoEmail(String aoEmail) {
		this.aoEmail = aoEmail;
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
	 * @return the registeredName
	 */
	public String getRegisteredName() {
		return registeredName;
	}

	/**
	 * @param registeredName the registeredName to set
	 */
	public void setRegisteredName(String registeredName) {
		this.registeredName = registeredName;
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
	 * @return the stagesMerchantCode
	 */
	public String getStagesMerchantCode() {
		return stagesMerchantCode;
	}

	/**
	 * @param stagesMerchantCode the stagesMerchantCode to set
	 */
	public void setStagesMerchantCode(String stagesMerchantCode) {
		this.stagesMerchantCode = stagesMerchantCode;
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
	 * @return the bimMerchantHeaderDTO
	 */
	public BimMerchantHeaderDTO getBimMerchantHeaderDTO() {
		return bimMerchantHeaderDTO;
	}

	/**
	 * @param bimMerchantHeaderDTO the bimMerchantHeaderDTO to set
	 */
	public void setBimMerchantHeaderDTO(BimMerchantHeaderDTO bimMerchantHeaderDTO) {
		this.bimMerchantHeaderDTO = bimMerchantHeaderDTO;
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
	 * @return the bimMerchantHeaderDTOs
	 */
	public List<BimMerchantHeaderDTO> getBimMerchantHeaderDTOs() {
		return bimMerchantHeaderDTOs;
	}

	/**
	 * @param bimMerchantHeaderDTOs the bimMerchantHeaderDTOs to set
	 */
	public void setBimMerchantHeaderDTOs(
			List<BimMerchantHeaderDTO> bimMerchantHeaderDTOs) {
		this.bimMerchantHeaderDTOs = bimMerchantHeaderDTOs;
	}

	/**
	 * @return the headMap
	 */
	public Map<String, String> getHeadMap() {
		return headMap;
	}

	/**
	 * @param headMap the headMap to set
	 */
	public void setHeadMap(Map<String, String> headMap) {
		this.headMap = headMap;
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
	 * @return the exportRepeatMerchant
	 */
	public String getExportRepeatMerchant() {
		return exportRepeatMerchant;
	}

	/**
	 * @param exportRepeatMerchant the exportRepeatMerchant to set
	 */
	public void setExportRepeatMerchant(String exportRepeatMerchant) {
		this.exportRepeatMerchant = exportRepeatMerchant;
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
