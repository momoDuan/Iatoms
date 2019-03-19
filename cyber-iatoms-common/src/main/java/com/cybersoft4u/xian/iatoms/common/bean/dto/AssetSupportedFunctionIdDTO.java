package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.CompositeIdentifier;

/**
 * Purpose: 
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月11日
 * @MaintenancePersonnel ericdu
 */
public class AssetSupportedFunctionIdDTO extends CompositeIdentifier {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 3588998230948017343L;
	
	private String functionId;
	private String assetTypteId;
	
	/**
	 * Constructor:無參構造
	 */
	public AssetSupportedFunctionIdDTO() {
		super();
	}
	/**
	 * Constructor: 有參構造
	 */
	public AssetSupportedFunctionIdDTO(String functionId, String assetTypteId) {
		super();
		this.functionId = functionId;
		this.assetTypteId = assetTypteId;
	}
	/**
	 * @return the functionId
	 */
	public String getFunctionId() {
		return functionId;
	}
	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	/**
	 * @return the assetTypteId
	 */
	public String getAssetTypteId() {
		return assetTypteId;
	}
	/**
	 * @param assetTypteId the assetTypteId to set
	 */
	public void setAssetTypteId(String assetTypteId) {
		this.assetTypteId = assetTypteId;
	}

}
