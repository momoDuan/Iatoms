package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

public class ApiAuthorizationInfoDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7342253083811182899L;

	public static enum ATTRIBUTE {
		CLIENT_CODE("clientCode"),
		IP("ip"),
		STATUS("status"),
		RETRY("retry"),
		AUTHORIZATION_TIME("authorizationTime"),
		HAS_TOKEN("hasToken"),
		TOKEN("token"),
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
	 * 唯一編號
	 */
	private String uuid;
	/**
	 * 介接系統
	 */
	private String clientCode;
	/**
	 * ip
	 */
	private String ip;
	/**
	 * 授權狀態
	 */
	private String status;
	/**
	 * 錯誤次數
	 */
	private int retry;
	/**
	 * 授權時間
	 */
	private Timestamp authorizationTime;
	/**
	 * 令牌
	 */
	private String token;
	/**
	 * 授權密文
	 */
	private String code;
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 維護廠商
	 */
	private String merchantCode;
	/**
	 * 案件類型
	 */
	private String caseType;
	/**
	 * 案件種類
	 */
	private String caseCategory;
	/**
	 * session timeout時間
	 */
	private int maxInactiveInterval;
	/**
	 * 請求ip
	 */
	private String remoteAddr;
	/**
	 * 電文內容
	 */
	private String jsonString;
	/**
	 * 聯繫聯絡人
	 */
	private String contact; 
	/**
	 * 聯繫手機
	 */
	private String contactPhone;
	/**
	 * 聯繫電話
	 */
	private String contactTel;
	/**
	 * 聯繫EMAIL
	 */
	private String contactEmail;
	/**
	 * 聯繫地址-縣市
	 */
	private String contactCounty ;
	/**
	 * 聯繫地址
	 */
	private String contactAddress;
	/**
	 * 報修原因
	 */
	private String description; 
	/**
	 * 拆機類型
	 */
	private String uninstallType;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 授權或者通知標誌位
	 */
	private String flag;
	/**
	 * 是否會有token
	 */
	private String hasToken;
	/**
	 * 郵遞區號--Task #3480
	 */
	private String contactCode;
	/**
	 * 郵遞區域--Task #3480
	 */
	private String contactArea;
	/**
	 * 案件編號--Task #3548
	 */
	private String caseId;
	/**
	 * 到場註記--Task #3548
	 */
	private String hasArrive;
	
	/**
	 * 求償編號
	 */
	private String paymentId;
	
	/**
	 * Constructor:無參構造
	 */
	public ApiAuthorizationInfoDTO() {
	}
	
	/**
	 * Constructor:有參構造
	 */
	public ApiAuthorizationInfoDTO(String uuid, String clientCode, String ip,
			String status, int retry, Timestamp authorizationTime,
			String token, String code, String dtid,
			String merchantCode, String caseType, String caseCategory,
			int maxInactiveInterval, String remoteAddr, String jsonString,
			String contact, String contactPhone, String contactTel,
			String contactEmail, String contactCounty, String contactAddress,
			String description, String uninstallType, String serialNumber, String paymentId) {
		super();
		this.uuid = uuid;
		this.clientCode = clientCode;
		this.ip = ip;
		this.status = status;
		this.retry = retry;
		this.authorizationTime = authorizationTime;
		this.token = token;
		this.code = code;
		this.dtid = dtid;
		this.merchantCode = merchantCode;
		this.caseType = caseType;
		this.caseCategory = caseCategory;
		this.maxInactiveInterval = maxInactiveInterval;
		this.remoteAddr = remoteAddr;
		this.jsonString = jsonString;
		this.contact = contact;
		this.contactPhone = contactPhone;
		this.contactTel = contactTel;
		this.contactEmail = contactEmail;
		this.contactCounty = contactCounty;
		this.contactAddress = contactAddress;
		this.description = description;
		this.uninstallType = uninstallType;
		this.serialNumber = serialNumber;
		this.paymentId = paymentId;
	}
	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the clientCode
	 */
	public String getClientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the retry
	 */
	public int getRetry() {
		return retry;
	}

	/**
	 * @param retry the retry to set
	 */
	public void setRetry(int retry) {
		this.retry = retry;
	}

	/**
	 * @return the authorizationTime
	 */
	public Timestamp getAuthorizationTime() {
		return authorizationTime;
	}

	/**
	 * @param authorizationTime the authorizationTime to set
	 */
	public void setAuthorizationTime(Timestamp authorizationTime) {
		this.authorizationTime = authorizationTime;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the dtid
	 */
	public String getDtid() {
		return dtid;
	}

	/**
	 * @param dtid the dtid to set
	 */
	public void setDtid(String dtid) {
		this.dtid = dtid;
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
	 * @return the caseType
	 */
	public String getCaseType() {
		return caseType;
	}

	/**
	 * @param caseType the caseType to set
	 */
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	/**
	 * @return the caseCategory
	 */
	public String getCaseCategory() {
		return caseCategory;
	}

	/**
	 * @param caseCategory the caseCategory to set
	 */
	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
	}

	/**
	 * @return the maxInactiveInterval
	 */
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	/**
	 * @param maxInactiveInterval the maxInactiveInterval to set
	 */
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	/**
	 * @return the remoteAddr
	 */
	public String getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * @param remoteAddr the remoteAddr to set
	 */
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * @return the jsonString
	 */
	public String getJsonString() {
		return jsonString;
	}

	/**
	 * @param jsonString the jsonString to set
	 */
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
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
	 * @return the contactPhone
	 */
	public String getContactPhone() {
		return contactPhone;
	}

	/**
	 * @param contactPhone the contactPhone to set
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
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
	 * @return the contactCounty
	 */
	public String getContactCounty() {
		return contactCounty;
	}

	/**
	 * @param contactCounty the contactCounty to set
	 */
	public void setContactCounty(String contactCounty) {
		this.contactCounty = contactCounty;
	}

	/**
	 * @return the contactAddress
	 */
	public String getContactAddress() {
		return contactAddress;
	}

	/**
	 * @param contactAddress the contactAddress to set
	 */
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the uninstallType
	 */
	public String getUninstallType() {
		return uninstallType;
	}

	/**
	 * @param uninstallType the uninstallType to set
	 */
	public void setUninstallType(String uninstallType) {
		this.uninstallType = uninstallType;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * @return the hasToken
	 */
	public String getHasToken() {
		return hasToken;
	}

	/**
	 * @param hasToken the hasToken to set
	 */
	public void setHasToken(String hasToken) {
		this.hasToken = hasToken;
	}

	/**
	 * @return the contactCode
	 */
	public String getContactCode() {
		return contactCode;
	}

	/**
	 * @param contactCode the contactCode to set
	 */
	public void setContactCode(String contactCode) {
		this.contactCode = contactCode;
	}

	/**
	 * @return the contactArea
	 */
	public String getContactArea() {
		return contactArea;
	}

	/**
	 * @param contactArea the contactArea to set
	 */
	public void setContactArea(String contactArea) {
		this.contactArea = contactArea;
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
	 * @return the hasArrive
	 */
	public String getHasArrive() {
		return hasArrive;
	}

	/**
	 * @param hasArrive the hasArrive to set
	 */
	public void setHasArrive(String hasArrive) {
		this.hasArrive = hasArrive;
	}

	/**
	 * @return the paymentId
	 */
	public String getPaymentId() {
		return paymentId;
	}

	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	
}
