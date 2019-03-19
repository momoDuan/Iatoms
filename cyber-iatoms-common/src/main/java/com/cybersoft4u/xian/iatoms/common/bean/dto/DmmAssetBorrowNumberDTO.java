package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 设备借用数量DTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年7月31日
 * @MaintenancePersonnel CarrieDuan
 */
public class DmmAssetBorrowNumberDTO  extends DataTransferObject<String>{
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 2380044162698206492L;
	
	public static enum ATTRIBUTE {
		ID("numberId"),
		BORROW_CASE_ID("borrowCaseId"),
		ASSET_TYPE_ID("assetTypeId"),
		BORROW_NUMBER("borrowNumber"),
		ASSET_TYPE_NAME("assetTypeName"),
		ASSET_CATEGORY_NAME("assetCategoryName");

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
	private String numberId;
	/**
	 * 設備借用id
	 */
	private String borrowCaseId;
	/**
	 * 借用設備id
	 */
	private String assetTypeId;
	/**
	 * 借用設備名稱
	 */
	private String assetTypeName;
	/**
	 * 設備類別
	 */
	private String assetCategoryName;
	/**
	 * 設備借用數量
	 */
	private String borrowNumber;
	
	/**
	 * Constructor:
	 */
	public DmmAssetBorrowNumberDTO() {
	}
	
	/**
	 * 
	 * Constructor:
	 */
	public DmmAssetBorrowNumberDTO(String numberId, String borrowCaseId,
			String assetTypeId, String borrowNumber) {
		this.numberId = numberId;
		this.borrowCaseId = borrowCaseId;
		this.assetTypeId = assetTypeId;
		this.borrowNumber = borrowNumber;
	}
	
	/**
	 * @return the numberId
	 */
	public String getNumberId() {
		return numberId;
	}

	/**
	 * @param numberId the numberId to set
	 */
	public void setNumberId(String numberId) {
		this.numberId = numberId;
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

	/**
	 * @return the assetTypeName
	 */
	public String getAssetTypeName() {
		return assetTypeName;
	}

	/**
	 * @param assetTypeName the assetTypeName to set
	 */
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	/**
	 * @return the assetCategoryName
	 */
	public String getAssetCategoryName() {
		return assetCategoryName;
	}

	/**
	 * @param assetCategoryName the assetCategoryName to set
	 */
	public void setAssetCategoryName(String assetCategoryName) {
		this.assetCategoryName = assetCategoryName;
	}
	
}
