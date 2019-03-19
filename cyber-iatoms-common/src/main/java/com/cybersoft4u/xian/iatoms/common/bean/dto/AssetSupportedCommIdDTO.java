package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.CompositeIdentifier;

/**
 * Purpose: 設備支援通訊模式關系表主鍵
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月11日
 * @MaintenancePersonnel ericdu
 */
public class AssetSupportedCommIdDTO extends CompositeIdentifier {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -3238127896202158671L;
	
	private String assetTypeId;
	
	private String commModeId;
	
	/**
	 * Constructor: 無參構造函數
	 */
	public AssetSupportedCommIdDTO() {
		super();
	}

	/**
	 * Constructor: 有參構造函數
	 */
	public AssetSupportedCommIdDTO(String assetTypeId, String commModeId) {
		super();
		this.assetTypeId = assetTypeId;
		this.commModeId = commModeId;
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
	 * @return the commModeId
	 */
	public String getCommModeId() {
		return commModeId;
	}

	/**
	 * @param commModeId the commModeId to set
	 */
	public void setCommModeId(String commModeId) {
		this.commModeId = commModeId;
	}

}
