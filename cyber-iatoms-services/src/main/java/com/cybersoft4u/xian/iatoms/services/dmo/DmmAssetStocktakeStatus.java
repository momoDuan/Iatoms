package com.cybersoft4u.xian.iatoms.services.dmo;


import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStocktakeStatusDTO;

/**
 * 
 * Purpose: 設備盤點表ＤＭＯ
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-18
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStocktakeStatus extends  DomainModelObject<DmmAssetStocktakeStatusId,AssetStocktakeStatusDTO> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7060569027664820750L;
	private DmmAssetStocktakeStatusId id;
	public DmmAssetStocktakeStatus() {
		super();
	}

	public DmmAssetStocktakeStatus(DmmAssetStocktakeStatusId id) {
		super();
		this.id = id;
	}

	/**
	 * @return the assetStocktackStatusId
	 */
	public DmmAssetStocktakeStatusId getAssetStocktackStatusId() {
		return id;
	}

	/**
	 * @param assetStocktackStatusId the assetStocktackStatusId to set
	 */
	public void setAssetStocktackStatusId(
			DmmAssetStocktakeStatusId id) {
		this.id = id;
	}


}
