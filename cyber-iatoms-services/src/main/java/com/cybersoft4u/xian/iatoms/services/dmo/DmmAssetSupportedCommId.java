package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

/**
 * Purpose: 設備支援通訊模式關系表
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月11日
 * @MaintenancePersonnel ericdu
 */
public class DmmAssetSupportedCommId extends CompositeIdentifier {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -972760652061172468L;
	
	private String assetTypeId;
	
	private String commModeId;
	
	/**
	 * Constructor: 無參構造
	 */
	public DmmAssetSupportedCommId() {
		super();
	}
	
	/**
	 * Constructor: 有參構造
	 */
	public DmmAssetSupportedCommId(String assetTypeId, String commModeId) {
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
