package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;
import java.util.Date;

import cafe.core.bean.dto.DataTransferObject;
/**
 * 
 * Purpose:設備入庫明細檔DTO
 * @author hungli 
 * @since  JDK 1.6
 * @date   2016/7/5
 * @MaintenancePersonnel cybersoft
 */
public class AssetInListDTO extends DataTransferObject<String> {

	public static enum ATTRIBUTE {
		ASSET_ID("assetId"),
		SERIAL_NUMBER("serialNumber"),
		ASSET_IN_ID("assetInId"),
		PROPERTY_ID("propertyId"),
		IS_CHECKED("isChecked"),
		IS_CUSTOMER_CHECKED("isCustomerChecked"),
		IS_BAD("isBad"),
		UPDATE_USER("updateUser"),
		UPDATE_USER_NAME("updateUserName"),
		UPDATE_DATE("updateDate"),
		ASSET_NAME("assetName"),
		ASSET_TYPE_ID("assetTypeId"),
		ERROR_MESSAGE("errorMessage"),
		KEEPER_NAME("keeperName"),
		OLD_PROPERTY_ID("oldPropertyId");
		;

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	private String assetId;
	private String serialNumber;
	private String assetInId;
	private String assetName;//設備名稱
	private String propertyId;
	private String isChecked = "N";
	private String isCustomerChecked = "N";
	private boolean isBad;
	private String updateUser;
	private String updateUserName;
	private Timestamp updateDate;
	private String assetTypeId;//設備類型ＩＤ
	private String oldPropertyId;//原財產編號
	private String errorMessage;//错误讯息
	private String keeperName;//保管人
	public AssetInListDTO() {
	}

	public AssetInListDTO(String id, String isChecked, boolean isBad,
			String updateUser, Timestamp updateDate) {
		this.id = id;
		this.isChecked = isChecked;
		this.isBad = isBad;
		this.updateUser = updateUser;
		this.updateDate = updateDate;
	}

	

	public AssetInListDTO(String assetId, String serialNumber,
			String assetInId, String assetName, String propertyId,
			String isChecked, String isCustomerChecked, boolean isBad,
			String updateUser, String updateUserName, Timestamp updateDate,
			String assetTypeId, String oldPropertyId, String errorMessage,
			String keeperName) {
		super();
		this.assetId = assetId;
		this.serialNumber = serialNumber;
		this.assetInId = assetInId;
		this.assetName = assetName;
		this.propertyId = propertyId;
		this.isChecked = isChecked;
		this.isCustomerChecked = isCustomerChecked;
		this.isBad = isBad;
		this.updateUser = updateUser;
		this.updateUserName = updateUserName;
		this.updateDate = updateDate;
		this.assetTypeId = assetTypeId;
		this.oldPropertyId = oldPropertyId;
		this.errorMessage = errorMessage;
		this.keeperName = keeperName;
	}

	/**
	 * @return the assetId
	 */
	public String getAssetId() {
		return assetId;
	}

	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(String assetId) {
		this.assetId = assetId;
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
	 * @return the assetInId
	 */
	public String getAssetInId() {
		return assetInId;
	}

	/**
	 * @param assetInId the assetInId to set
	 */
	public void setAssetInId(String assetInId) {
		this.assetInId = assetInId;
	}

	/**
	 * @return the assetName
	 */
	public String getAssetName() {
		return assetName;
	}

	/**
	 * @param assetName the assetName to set
	 */
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * @return the isChecked
	 */
	public String getIsChecked() {
		return isChecked;
	}

	/**
	 * @param isChecked the isChecked to set
	 */
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * @return the isCustomerChecked
	 */
	public String getIsCustomerChecked() {
		return isCustomerChecked;
	}

	/**
	 * @param isCustomerChecked the isCustomerChecked to set
	 */
	public void setIsCustomerChecked(String isCustomerChecked) {
		this.isCustomerChecked = isCustomerChecked;
	}

	/**
	 * @return the isBad
	 */
	public boolean isBad() {
		return isBad;
	}

	/**
	 * @param isBad the isBad to set
	 */
	public void setBad(boolean isBad) {
		this.isBad = isBad;
	}

	/**
	 * @return the updateUser
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateUserName
	 */
	public String getUpdateUserName() {
		return updateUserName;
	}

	/**
	 * @param updateUserName the updateUserName to set
	 */
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	/**
	 * @return the updateDate
	 */
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
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
	 * @return the oldPropertyId
	 */
	public String getOldPropertyId() {
		return oldPropertyId;
	}

	/**
	 * @param oldPropertyId the oldPropertyId to set
	 */
	public void setOldPropertyId(String oldPropertyId) {
		this.oldPropertyId = oldPropertyId;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the keeperName
	 */
	public String getKeeperName() {
		return keeperName;
	}

	/**
	 * @param keeperName the keeperName to set
	 */
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}
}
