package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 程式版本維護DTO
 * @author echomou
 * @since  JDK 1.6
 * @date   2017/3/10
 * @MaintenancePersonnel echomou
 */
public class ApplicationAssetLinkDTO extends DataTransferObject<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1528540862826543213L;
	private String applicationId;
	private String assetTypeId;
	private String customerId;
	/**
	 * Constructor:
	 */
	public ApplicationAssetLinkDTO() {
		super();
	}
	/**
	 * Constructor:
	 */
	public ApplicationAssetLinkDTO(String applicationId, String assetTypeId) {
		super();
		this.applicationId = applicationId;
		this.assetTypeId = assetTypeId;
	}
	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}
	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
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
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
