package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

public class PvmApplicationAssetLinkId extends CompositeIdentifier {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1611340961614235504L;
	private String applicationId;
	private String assetTypeId;
	/**
	 * Constructor:
	 */
	public PvmApplicationAssetLinkId() {
		super();
	}
	/**
	 * Constructor:
	 */
	public PvmApplicationAssetLinkId(String applicationId, String assetTypeId) {
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

}
