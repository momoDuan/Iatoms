package com.cybersoft4u.xian.iatoms.services.dmo;


import cafe.core.bean.CompositeIdentifier;

/**
 * 
 * Purpose: 設備狀態IdDMO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-18
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStocktakeStatusId extends CompositeIdentifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7487783179777697806L;
	private String stocktackId;
	private String assetStatus;
	public DmmAssetStocktakeStatusId() {
		super();
	}
	public DmmAssetStocktakeStatusId(String stocktackId,String assetStatus) {
		super();
		this.stocktackId = stocktackId;
		this.assetStatus = assetStatus;
	}
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
