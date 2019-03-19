package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiCmsRepositoryDTO;

public class ApiCmsRepository extends DomainModelObject<String,ApiCmsRepositoryDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4461184466467202035L;
	private String systemId;
	private String serialNumber;
	private String jobType;
	private String merchantCode;
	private String dtid;
	private String notifyFlag;
	private String memo;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	
	public ApiCmsRepository() {
	}

	public ApiCmsRepository(String systemId, String serialNumber,
			String jobType, String merchantCode, String dtid,
			String notifyFlag, String memo, Timestamp createdDate,
			Timestamp updatedDate) {
		super();
		this.systemId = systemId;
		this.serialNumber = serialNumber;
		this.jobType = jobType;
		this.merchantCode = merchantCode;
		this.dtid = dtid;
		this.notifyFlag = notifyFlag;
		this.memo = memo;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the systemId
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * @param systemId the systemId to set
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
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
	 * @return the jobType
	 */
	public String getJobType() {
		return jobType;
	}

	/**
	 * @param jobType the jobType to set
	 */
	public void setJobType(String jobType) {
		this.jobType = jobType;
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
	 * @return the notifyFlag
	 */
	public String getNotifyFlag() {
		return notifyFlag;
	}

	/**
	 * @param notifyFlag the notifyFlag to set
	 */
	public void setNotifyFlag(String notifyFlag) {
		this.notifyFlag = notifyFlag;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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
	
}
