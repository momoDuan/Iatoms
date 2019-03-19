package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 涉筆盤點種類DTO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-6-30
 * @MaintenancePersonnel allenchen
 */
public class AssetStocktakeCategroyDTO extends DataTransferObject<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2462995322646895812L;
	/*public static void main(String[] args) {
		BindPageDataUtils.generateAttributeEnum(AssetStocktackCategroyDTO.class);
	}
		*/
	public static enum ATTRIBUTE {
		STOCKTACK_ID("stocktackId"),
		ASSET_TYPE_ID("assetTypeId");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};


	private String stocktackId;//盤點批號
	private String assetTypeId;//設備種類
	/**
	 * @return the stocktackId
	 */
	public String getStocktackId() {
		return stocktackId;
	}
	
	/**
	 * @param stocktackId the stocktackId to set
	 */
	public void setStocktackId(String stocktackId) {
		this.stocktackId = stocktackId;
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
