package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetBorrowInfoDTO;

public class DmmAssetBorrowNumber extends DomainModelObject<String, DmmAssetBorrowInfoDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6022891666368754651L;
	private String id;
	private String borrowCaseId;
	private String assetTypeId;
	private String borrowNumber;
	
	
	
	public DmmAssetBorrowNumber() {
	}
	
	public DmmAssetBorrowNumber(String id, String borrowCaseId,
			String assetTypeId, String borrowNumber) {
		this.id = id;
		this.borrowCaseId = borrowCaseId;
		this.assetTypeId = assetTypeId;
		this.borrowNumber = borrowNumber;
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
	 * @return the borrowNumber
	 */
	public String getBorrowNumber() {
		return borrowNumber;
	}
	/**
	 * @param borrowNumber the borrowNumber to set
	 */
	public void setBorrowNumber(String borrowNumber) {
		this.borrowNumber = borrowNumber;
	}
	
	
}
