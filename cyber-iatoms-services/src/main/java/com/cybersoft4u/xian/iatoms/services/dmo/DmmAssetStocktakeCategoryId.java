package com.cybersoft4u.xian.iatoms.services.dmo;


import cafe.core.bean.CompositeIdentifier;

/**
 * 
 * Purpose: 設備種類IdDMO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016-7-18
 * @MaintenancePersonnel allenchen
 */
public class DmmAssetStocktakeCategoryId extends CompositeIdentifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8837425210275580471L;
	private String stocktackId;
	private String assetTypeId;

	public DmmAssetStocktakeCategoryId() {
	}

	public DmmAssetStocktakeCategoryId(String stocktackId, String assetTypeId) {
		this.stocktackId = stocktackId;
		this.assetTypeId = assetTypeId;
	}

	public String getStocktackId() {
		return this.stocktackId;
	}

	public void setStocktackId(String stocktackId) {
		this.stocktackId = stocktackId;
	}

	public String getAssetTypeId() {
		return this.assetTypeId;
	}

	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DmmAssetStocktakeCategoryId))
			return false;
		DmmAssetStocktakeCategoryId castOther = (DmmAssetStocktakeCategoryId) other;

		return ((this.getStocktackId() == castOther.getStocktackId()) || (this
				.getStocktackId() != null && castOther.getStocktackId() != null && this
				.getStocktackId().equals(castOther.getStocktackId())))
				&& ((this.getAssetTypeId() == castOther.getAssetTypeId()) || (this
						.getAssetTypeId() != null
						&& castOther.getAssetTypeId() != null && this
						.getAssetTypeId().equals(castOther.getAssetTypeId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getStocktackId() == null ? 0 : this.getStocktackId()
						.hashCode());
		result = 37
				* result
				+ (getAssetTypeId() == null ? 0 : this.getAssetTypeId()
						.hashCode());
		return result;
	}
	
}
