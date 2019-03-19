package com.cybersoft4u.xian.iatoms.services.dmo;


import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStocktakeCategroyDTO;

/**
 * 
 * Purpose: 設備盤點表DMO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-18
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStocktakeCategory extends  DomainModelObject<DmmAssetStocktakeCategoryId,AssetStocktakeCategroyDTO> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1271796279334507389L;
	private DmmAssetStocktakeCategoryId id;

	public DmmAssetStocktakeCategory() {
		super();
	}
	public DmmAssetStocktakeCategory(DmmAssetStocktakeCategoryId id) {
		super();
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public DmmAssetStocktakeCategoryId getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(DmmAssetStocktakeCategoryId id) {
		this.id = id;
	}
}
