package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;
/**
 * Purpose: 设备借用列表DTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2018年7月31日
 * @MaintenancePersonnel CarrieDuan
 */
public class DmmAssetBorrowListDTO  extends DataTransferObject<String>{
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 2380044162698206492L;
	public static enum ATTRIBUTE {
		ID("id"),
		BORROW_CASE_ID("borrowCaseId"),
		ASSET_TYPE_ID("assetTypeId"),
		SERIAL_NUMBER("serialNumber");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	public static void main(String[] args) {
		BindPageDataUtils.generateAttributeEnum(DmmAssetBorrowListDTO.class);
	}
	/**
	 * id
	 */
	private String id;
	/**
	 * 借用設備id
	 */
	private String borrowCaseId;
	/**
	 * 設備類別
	 */
	private String assetTypeId;
	/**
	 * 設備序號
	 */
	private String serialNumber;
	
	/**
	 * Constructor:
	 */
	public DmmAssetBorrowListDTO() {
	}
	
	/**
	 * Constructor:
	 */
	public DmmAssetBorrowListDTO(String id, String borrowCaseId,
			String assetTypeId, String serialNumber) {
		this.id = id;
		this.borrowCaseId = borrowCaseId;
		this.assetTypeId = assetTypeId;
		this.serialNumber = serialNumber;
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

}
