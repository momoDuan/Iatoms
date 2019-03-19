package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose:刷卡機標籤管理DTO
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/06/27
 * @MaintenancePersonnel CyberSoft
 */
public class DmmEdcLabelDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 880419205280197208L;
	
	public static enum ATTRIBUTE {
		SEQ_NO("seqNo"),
		MERCHANT_CODE("merchantCode"),
		DTID("dtid"),
		MERCHANT_TYPE("merchantType"),
		RELATION("relation"),
		STATUS("status"),
		IP("ip"),
		URL("url"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate")
		;
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	}
	
	/**
	 * 序號
	 */
	private String seqNo;
	/**
	 * 特店代號
	 */
	private String merchantCode;
	/**
	 * DTID
	 */
	private String dtid;
	/**
	 * 館別
	 */
	private String merchantType;
	/**
	 * 幹系
	 */
	private String relation;
	/**
	 * 型態
	 */
	private String status;
	/**
	 * IP位址
	 */
	private String ip;
	/**
	 * 聯絡資訊網址
	 */
	private String url;
	/**
	 * 新增人員ID
	 */
	private String createdById;
	/**
	 * 新增人員名稱
	 */
	private String createdByName;
	/**
	 * 新增日期
	 */
	private Timestamp createdDate;
	/**
	 * 異動人員ID
	 */
	private String updatedById;
	/**
	 * 異動人員名稱
	 */
	private String updatedByName;
	/**
	 * 異動日期
	 */
	private Timestamp updatedDate;

	/**
	 * Constructor:無參構造
	 */
	public DmmEdcLabelDTO() {
	}
	/**
	 * Constructor:有參構造
	 */
	public DmmEdcLabelDTO(String seqNo, String merchantCode, String dtid, String merchantType,
			String relation, String status, String ip, String url, String createdById, String createdByName,
			Timestamp createdDate, String updatedById, String updatedByName, Timestamp updatedDate) {
		this.seqNo = seqNo;
		this.merchantCode = merchantCode;
		this.dtid = dtid;
		this.merchantType = merchantType;
		this.relation = relation;
		this.status = status;
		this.ip = ip;
		this.url = url;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
	}
	
	/**
	 * @return the seqNo
	 */
	public String getSeqNo() {
		return seqNo;
	}
	/**
	 * @param seqNo the seqNo to set
	 */
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
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
	 * @return the merchantType
	 */
	public String getMerchantType() {
		return merchantType;
	}
	/**
	 * @param merchantType the merchantType to set
	 */
	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}
	/**
	 * @return the relation
	 */
	public String getRelation() {
		return relation;
	}
	/**
	 * @param relation the relation to set
	 */
	public void setRelation(String relation) {
		this.relation = relation;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
}
