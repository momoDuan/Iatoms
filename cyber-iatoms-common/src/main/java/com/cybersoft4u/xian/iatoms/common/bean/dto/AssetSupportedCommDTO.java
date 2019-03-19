package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;

/**
 * 
 * Purpose: 設備支援通訊模式檔
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel ericdu
 */
public class AssetSupportedCommDTO extends DataTransferObject<AssetSupportedCommIdDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -6374586188562514618L;
	
	private String assetTypeId;
	private String commModeId;
	
	/**
	 * Constructor: 無參構造函數
	 */
	public AssetSupportedCommDTO() {
		super();
	}
	
	/**
	 * Constructor: 有參構造函數
	 */
	public AssetSupportedCommDTO(AssetSupportedCommIdDTO id) {
		super(id);
	}
	
	public static enum ATTRIBUTE {
		ASSET_TYPE_ID("assetTypeId"),
		COMM_MODE_ID("commModeId");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
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
