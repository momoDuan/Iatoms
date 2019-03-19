package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

public class ApiCmsRepositoryDTO  extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1595298500588020349L;
	
	public static enum ATTRIBUTE {
		SYSTEM_ID("systemId"),
		SERIAL_NUMBER("serialNumber"),
		JOB_TYPE("jobType"),
		MERCHANT_CODE("merchantCode"),
		DTID("dtid"),
		NOTIFY_FLAG("notifyFlag"),
		MEMO("memo"),
		STATUS("status"),
		CREATED_DATE("createdDate"),
		UPDATED_DATE("updatedDate"),
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
	 * 系統編號
	 */
	private String systemId;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	/**
	 * 作業類型：
	 * 05 設備停用，06 設備啟用
	 */
	private String jobType;
	/**
	 * 特店代號
	 */
	private String merchantCode;
	/**
	 * 設備序號
	 */
	private String dtid;
	/**
	 * 通知註記：
	 * 0 否，1 是
	 */
	private String notifyFlag;
	/**
	 * 備註
	 */
	private String memo;
	/**
	 * 狀態
	 */
	private String status;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	
	public ApiCmsRepositoryDTO() {
	}

	public ApiCmsRepositoryDTO(String jobType, String merchantCode,
			String dtid, String notifyFlag, String memo) {
		super();
		this.jobType = jobType;
		this.merchantCode = merchantCode;
		this.dtid = dtid;
		this.notifyFlag = notifyFlag;
		this.memo = memo;
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
