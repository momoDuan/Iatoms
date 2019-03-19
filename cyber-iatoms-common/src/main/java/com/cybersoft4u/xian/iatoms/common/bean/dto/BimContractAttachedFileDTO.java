package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 合約文件DTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/9/14
 * @MaintenancePersonnel cybersoft
 */
public class BimContractAttachedFileDTO extends DataTransferObject<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -753399749641595848L;

	public static enum ATTRIBUTE {
		ATTACHED_FILE_ID("attachedFileId"),
		CONTRACT_ID("contractId"),
		FILE_NAME("fileName"),
		ATTACHED_FILE("attachedFile");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	private String attachedFileId;
	private String contractId;
	private String fileName;
	private String attachedFile;
	
	/**
	 * Constructor:無參構造函數
	 */
	public BimContractAttachedFileDTO() {
		super();
	}

	/**
	 * @return the attachedFileId
	 */
	public String getAttachedFileId() {
		return attachedFileId;
	}

	/**
	 * @param attachedFileId the attachedFileId to set
	 */
	public void setAttachedFileId(String attachedFileId) {
		this.attachedFileId = attachedFileId;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
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
	 * @return the attachedFile
	 */
	public String getAttachedFile() {
		return attachedFile;
	}

	/**
	 * @param attachedFile the attachedFile to set
	 */
	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
	}
	
	
	
}
