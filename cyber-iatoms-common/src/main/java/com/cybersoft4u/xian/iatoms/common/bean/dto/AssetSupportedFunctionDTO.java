package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;

/**
 * Purpose: 設備支援功能關系表
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月11日
 * @MaintenancePersonnel ericdu
 */
public class AssetSupportedFunctionDTO extends DataTransferObject<AssetSupportedFunctionIdDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 6063168259090546777L;
	
	private String functionId;
	private String assetTypeId;
	
	
	public static enum ATTRIBUTE {
		FUNCTION_ID("functionId"),
		ASSET_TYPE_ID("assetTypeId");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	/**
	 * Constructor: 無參構造
	 */
	public AssetSupportedFunctionDTO() {
		super();
	}
	
	/**
	 * Constructor: 有參構造
	 */
	public AssetSupportedFunctionDTO(AssetSupportedFunctionIdDTO id) {
		super(id);
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
