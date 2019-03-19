package com.cybersoft4u.xian.iatoms.services.dmo;

// Generated 2016/9/14 �U�� 02:37:12 by Hibernate Tools 3.4.0.CR1

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAttachedFileDTO;

/**
 * BimContractAttachedFile generated by hbm2java
 */
public class BimContractAttachedFile extends DomainModelObject<String,BimContractAttachedFileDTO> {

	private String attachedFileId;
	private String contractId;
	private String fileName;
	private String attachedFile;

	public BimContractAttachedFile() {
	}

	public BimContractAttachedFile(String attachedFileId, String contractId,
			String attachedFile) {
		this.attachedFileId = attachedFileId;
		this.contractId = contractId;
		this.attachedFile = attachedFile;
	}

	public BimContractAttachedFile(String attachedFileId, String contractId,
			String fileName, String attachedFile) {
		this.attachedFileId = attachedFileId;
		this.contractId = contractId;
		this.fileName = fileName;
		this.attachedFile = attachedFile;
	}

	public String getAttachedFileId() {
		return this.attachedFileId;
	}

	public void setAttachedFileId(String attachedFileId) {
		this.attachedFileId = attachedFileId;
	}

	public String getContractId() {
		return this.contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAttachedFile() {
		return this.attachedFile;
	}

	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
	}

}
