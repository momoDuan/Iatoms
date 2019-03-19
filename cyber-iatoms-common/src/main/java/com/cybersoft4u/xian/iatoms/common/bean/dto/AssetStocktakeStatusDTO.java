package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 設備盤點狀態DTO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-6-30
 * @MaintenancePersonnel allenchen
 */
public class AssetStocktakeStatusDTO extends DataTransferObject<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4450595432406523127L;
	/*public static void main(String[] args) {
		BindPageDataUtils.generateAttributeEnum(AssetStocktackStatusDTO.class);
	}*/
	public static enum ATTRIBUTE {
		STOCKTACK_ID("stocktackId"),
		ASSET_STATUS("assetStatus");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	

	private String stocktackId;//盤點批號
	private String assetStatus;//設備状态
	/**
	 * @return the stocktackId
	 */
	public String getStocktackId() {
		return stocktackId;
	}
	/**
	 * @return the assetStatus
	 */
	public String getAssetStatus() {
		return assetStatus;
	}
	/**
	 * @param stocktackId the stocktackId to set
	 */
	public void setStocktackId(String stocktackId) {
		this.stocktackId = stocktackId;
	}
	/**
	 * @param assetStatus the assetStatus to set
	 */
	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}
	
	
	
}
