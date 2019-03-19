package com.cybersoft4u.xian.iatoms.services.dmo;

import java.sql.Timestamp;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;

public class DmmAssetBorrowList extends DomainModelObject<String, DmmAssetBorrowInfoDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6022891666368754651L;
	private String id;
	private String borrowCaseId;
	private String assetTypeId;
	private String serialNumber;
	private Timestamp borrowEndDate;
	
	
	
	public DmmAssetBorrowList() {
	}
	
	public DmmAssetBorrowList(String id, String borrowCaseId,
			String assetTypeId, String serialNumber, Timestamp borrowEndDate) {
		this.id = id;
		this.borrowCaseId = borrowCaseId;
		this.assetTypeId = assetTypeId;
		this.serialNumber = serialNumber;
		this.borrowEndDate = borrowEndDate;
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
	 * @return the borrowCaseId
	 */
	public String getBorrowCaseId() {
		return borrowCaseId;
	}
	/**
	 * @param borrowCaseId the borrowCaseId to set
	 */
	public void setBorrowCaseId(String borrowCaseId) {
		this.borrowCaseId = borrowCaseId;
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
	 * @return the borrowEndDate
	 */
	public Timestamp getBorrowEndDate() {
		return borrowEndDate;
	}

	/**
	 * @param borrowEndDate the borrowEndDate to set
	 */
	public void setBorrowEndDate(Timestamp borrowEndDate) {
		this.borrowEndDate = borrowEndDate;
	}
	
	
}
