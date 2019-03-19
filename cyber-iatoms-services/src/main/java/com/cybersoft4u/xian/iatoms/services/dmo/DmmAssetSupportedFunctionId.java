package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

/**
 * Purpose: 設備支援功能關系表主鍵對象
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月11日
 * @MaintenancePersonnel ericdu
 */
public class DmmAssetSupportedFunctionId extends CompositeIdentifier {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 6008914197013658207L;
	
	private String functionId;
	private String assetTypeId;
	
	/**
	 * 
	 * Constructor: 無參構造
	 */
	public DmmAssetSupportedFunctionId(){
		super();
	}
	
	/**
	 * Constructor: 有參構造
	 */
	public DmmAssetSupportedFunctionId(String functionId, String assetTypeId) {
		super();
		this.functionId = functionId;
		this.assetTypeId = assetTypeId;
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
