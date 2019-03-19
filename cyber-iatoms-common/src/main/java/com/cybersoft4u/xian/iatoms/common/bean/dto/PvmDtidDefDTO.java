package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: DTID維護DTO
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年8月5日
 * @MaintenancePersonnel CarrieDuan
 */
public class PvmDtidDefDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 8933433810127467675L;
	
	public static enum ATTRIBUTE {
		ID("id"),
		COMPANY_ID("companyId"),
		COMPANY_NAME("companyName"),
		ASSET_TYPE_ID("assetTypeId"),
		ASSET_NAME("assetName"),
		DTID_START("dtidStart"),
		DTID_END("dtidEnd"),
		CURRENT_NUMBER("currentNumber"),
		COMMENT("comment"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		DTID_START_END("dtidStartEnd");
		
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * id
	 */
	private String id;
	/**
	 * 客戶ID
	 */
	private String companyId;
	/**
	 * 客戶名稱
	 */
	private String companyName;
	/**
	 * 設備ID
	 */
	private String assetTypeId;
	/**
	 * 設備名稱
	 */
	private String assetName;
	/**
	 * DTID開始
	 */
	private String dtidStart;
	/**
	 * DTID結束
	 */
	private String dtidEnd;
	/**
	 * DTID起止
	 */
	private String dtidStartEnd;
	/**
	 * 已存在DTID
	 */
	private String currentNumber;
	/**
	 * 描述
	 */
	private String comment;
	/**
	 * 創建人員ID
	 */
	private String createdById;
	/**
	 * 創建人員名稱
	 */
	private String createdByName;
	/**
	 * 創建日期
	 */
	private Timestamp createdDate;
	/**
	 * 更新人員ID
	 */
	private String updatedById;
	/**
	 * 更新日期
	 */
	private String updatedByName;
	/**
	 * 更新時間
	 */
	private Timestamp updatedDate;
	
	/**
	 * Constructor:無參構造函數
	 */
	public PvmDtidDefDTO() {
		super();
	}

	

	/**
	 * Constructor: 有參構造函數
	 */
	public PvmDtidDefDTO(String id, String companyId, String companyName,
			String assetTypeId, String assetName, String dtidStart,
			String dtidEnd, String currentNumber, String comment,
			String createdById, String createdByName, Timestamp createdDate,
			String updatedById, String updatedByName, Timestamp updatedDate,
			String dtidStartEnd) {
		super();
		this.id = id;
		this.companyId = companyId;
		this.companyName = companyName;
		this.assetTypeId = assetTypeId;
		this.assetName = assetName;
		this.dtidStart = dtidStart;
		this.dtidEnd = dtidEnd;
		this.currentNumber = currentNumber;
		this.comment = comment;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.dtidStartEnd = dtidStartEnd;
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the assetTypeId
	 */
	public String getAssetTypeId() {
		return assetTypeId;
	}

	/**
	 * @param assetTypeId the assetTypeId to set
	 */
	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	/**
	 * @return the dtidStart
	 */
	public String getDtidStart() {
		return dtidStart;
	}

	/**
	 * @param dtidStart the dtidStart to set
	 */
	public void setDtidStart(String dtidStart) {
		this.dtidStart = dtidStart;
	}

	/**
	 * @return the dtidEnd
	 */
	public String getDtidEnd() {
		return dtidEnd;
	}

	/**
	 * @param dtidEnd the dtidEnd to set
	 */
	public void setDtidEnd(String dtidEnd) {
		this.dtidEnd = dtidEnd;
	}

	/**
	 * @return the currentNumber
	 */
	public String getCurrentNumber() {
		return currentNumber;
	}

	/**
	 * @param currentNumber the currentNumber to set
	 */
	public void setCurrentNumber(String currentNumber) {
		this.currentNumber = currentNumber;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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

	/**
	 * @return the assetName
	 */
	public String getAssetName() {
		return assetName;
	}

	/**
	 * @param assetName the assetName to set
	 */
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	/**
	 * @return the dtidStartEnd
	 */
	public String getDtidStartEnd() {
		return dtidStartEnd;
	}

	/**
	 * @param dtidStartEnd the dtidStartEnd to set
	 */
	public void setDtidStartEnd(String dtidStartEnd) {
		this.dtidStartEnd = dtidStartEnd;
	}
	
	
}
