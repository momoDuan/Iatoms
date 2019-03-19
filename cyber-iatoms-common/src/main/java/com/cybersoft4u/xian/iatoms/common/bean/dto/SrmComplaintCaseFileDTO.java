package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 客訴管理附加檔案DTO
 * @author nicklin
 * @since  JDK 1.7
 * @date   2018/03/26
 * @MaintenancePersonnel cybersoft
 */
public class SrmComplaintCaseFileDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6380469822297292194L;

	/**
	 * Purpose: 枚舉類型參數
	 * @author nicklin
	 * @since  JDK 1.7
	 * @date   2018/03/26
	 * @MaintenancePersonnel cybersoft
	 */
	public static enum ATTRIBUTE {
		FILE_ID("fileId"),
		CASE_ID("caseId"),
		FILE_NAME("fileName"),
		FILE_PATH("filePath"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE("createdDate");
		
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		
		public String getValue() {
			return this.value;
		}
	}
	
	/**
	 * 文件編號
	 */
	private String fileId;
	/**
	 * 客訴編號
	 */
	private String caseId;
	/**
	 * 文件名稱
	 */
	private String fileName;
	/**
	 * 文件路徑
	 */
	private String filePath;
	/**
	 * 新增人員編號
	 */
	private String createdById;
	/**
	 * 新增人員名稱
	 */
	private String createdByName;
	/**
	 * 新增日期
	 */
	private Date createdDate;
	
	/**
	 * Constructor:無參數建構子
	 */
	public SrmComplaintCaseFileDTO() {
	}
	/**
	 * Constructor:有參數建構子
	 */
	public SrmComplaintCaseFileDTO(String fileId, String caseId,
			String fileName, String filePath, String createdById,
			String createdByName, Date createdDate) {
		super();
		this.fileId = fileId;
		this.caseId = caseId;
		this.fileName = fileName;
		this.filePath = filePath;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
	}
	
	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}
	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
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
}
