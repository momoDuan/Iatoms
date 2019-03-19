package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.CompositeIdentifier;

/**
 * Purpose: 廠商使用設備檔主鍵
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月13日
 * @MaintenancePersonnel ericdu
 */
public class AssetTypeCompanyIdDTO extends CompositeIdentifier {

	/**
	 *  序列號
	 */
	private static final long serialVersionUID = -5920771222731719099L;

	private String assetTypeId;
	private String companyId;
	
	/**
	 * Constructor: 無參構造
	 */
	public AssetTypeCompanyIdDTO() {
		super();
	}
	
	/**
	 * Constructor: 有參構造
	 */
	public AssetTypeCompanyIdDTO(String assetTypeId, String companyId) {
		this.assetTypeId = assetTypeId;
		this.companyId = companyId;
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
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
