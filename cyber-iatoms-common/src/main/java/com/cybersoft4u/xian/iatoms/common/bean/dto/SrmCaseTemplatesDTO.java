package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;

public class SrmCaseTemplatesDTO  extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -6573917877835785323L;
	/**
	 * 空構造
	 */
	public SrmCaseTemplatesDTO() {
	}
	
	public static enum ATTRIBUTE {
		ID("id"),
		CATEGORY("category"),
		FILE_NAME("fileName"),
		FILE_PATH("filePath"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_BY_DATE("updatedDate"),
		DELETED("deleted");
		/**
		 * value
		 */
		private String value;
		/**
		 * @param value
		 */
		ATTRIBUTE(String value) {
			this.value = value;
		};
		/**
		 * @return this.value
		 */
		public String getValue() {
			return this.value;
		}
	};
	//ID
	private String id;
	//類型
	private String category;
	//文件名
	private String fileName;
	//路徑
	private String filePath;
	//新增人員編號
	private String createdById;
	//新增人員姓名
	private String createdByName;
	//新增日期
	private Date createdDate;
	//異動人員編號
	private String updatedById;
	//異動人員姓名
	private String updatedByName;
	//異動日期
	private Date updatedDate;
	//刪除標記
	private String deleted;
	
	public SrmCaseTemplatesDTO(String id,
			String category, String fileName,
			String filePath, String createdById,
			String createdByName, Date createdDate, String updatedById,
			String updatedByName, Date updatedDate, String deleted) {
		this.id = id;
		this.category = category;
		this.fileName = fileName;
		this.filePath = filePath;
		this.deleted = deleted;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	
}
