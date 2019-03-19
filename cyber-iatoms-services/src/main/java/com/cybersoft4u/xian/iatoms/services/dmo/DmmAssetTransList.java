package com.cybersoft4u.xian.iatoms.services.dmo;

// Generated 2016/6/3 �U�� 01:30:43 by Hibernate Tools 3.4.0.CR1

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;

/**
 * AssetTransList generated by hbm2java
 */
public class DmmAssetTransList extends DomainModelObject<String, DmmAssetTransListDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -478468731858542336L;
	
	/**
	 * 轉倉批號
	 */
	private String assetTransId;
	
	/**
	 * 設備序號
	 */
	private String serialNumber;
	private String isCup = "N";
	private String isEnabled = "N";
	private String contractId;
	private String comment;
	private String createdById;
	private String createdByName;
	private Timestamp createdDate;
	private String updatedById;
	private String updatedByName;
	private Timestamp updatedDate;
	private String id;
	private String assetUser;
	private String oldStatus;
	private String transStatus;
	/**
	 * Constructor:
	 */
	public DmmAssetTransList() {
	}

	public DmmAssetTransList(String id) {
		this.id = id;
	}

	/**
	 * Constructor:
	 */
	public DmmAssetTransList(String assetTransId, String serialNumber, String isCup, String isEnabled,
			String contractId, String comment, String createdById, String createdByName, Timestamp createdDate,
			String updatedById, String updatedByName, Timestamp updatedDate, String id, String assetUser,
			String oldStatus) {
		this.assetTransId = assetTransId;
		this.serialNumber = serialNumber;
		this.isCup = isCup;
		this.isEnabled = isEnabled;
		this.contractId = contractId;
		this.comment = comment;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.id = id;
		this.assetUser = assetUser;
		this.oldStatus = oldStatus;
	}

	/**
	 * @return the assetTransId
	 */
	public String getAssetTransId() {
		return assetTransId;
	}

	/**
	 * @param assetTransId the assetTransId to set
	 */
	public void setAssetTransId(String assetTransId) {
		this.assetTransId = assetTransId;
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
	 * @return the isCup
	 */
	public String getIsCup() {
		return isCup;
	}

	/**
	 * @param isCup the isCup to set
	 */
	public void setIsCup(String isCup) {
		this.isCup = isCup;
	}

	/**
	 * @return the isEnabled
	 */
	public String getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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
	public Date getCreatedDate() {
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
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the assetUser
	 */
	public String getAssetUser() {
		return assetUser;
	}

	/**
	 * @param assetUser the assetUser to set
	 */
	public void setAssetUser(String assetUser) {
		this.assetUser = assetUser;
	}

	/**
	 * @return the oldStatus
	 */
	public String getOldStatus() {
		return oldStatus;
	}

	/**
	 * @param oldStatus the oldStatus to set
	 */
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
	/**
	 * @return the transStatus
	 */
	public String getTransStatus() {
		return transStatus;
	}

	/**
	 * @param transStatus the transStatus to set
	 */
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
}