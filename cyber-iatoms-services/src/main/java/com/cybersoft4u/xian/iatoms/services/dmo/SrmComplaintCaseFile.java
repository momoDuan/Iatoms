package com.cybersoft4u.xian.iatoms.services.dmo;

import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmComplaintCaseFileDTO;

/**
 * Purpose: 客訴管理附加檔案DMO
 * @author  nicklin
 * @since	JDK 1.7
 * @date	2018/03/26
 * @MaintenancePersonnel cybersoft
 */
public class SrmComplaintCaseFile extends DomainModelObject<String, SrmComplaintCaseFileDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5634032737181391759L;

	private String fileId;
	private String caseId;
	private String fileName;
	private String filePath;
	private String createdById;
	private String createdByName;
	private Date createdDate;
	
	/**
	 * Constructor:無參數建構子
	 */
	public SrmComplaintCaseFile() {
	}
	
	/**
	 * Constructor:有參數建構子
	 */
	public SrmComplaintCaseFile(String fileId) {
		this.fileId = fileId;
	}
	
	/**
	 * Constructor:有參數建構子
	 */
	public SrmComplaintCaseFile(String fileId, String caseId,
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
