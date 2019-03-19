package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;

/**
 * 
 * Purpose:设备DTO 
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016/5/17
 * @MaintenancePersonnel allenchen
 */
public class BimContractAssetDTO extends DataTransferObject<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -753399749641595848L;
	
	/*public static void main(String[] args) {
		BindPageDataUtils.generateAttributeEnum(AssetTypeDTO.class);
	}*/
	
	/**
	 * 
	 * Purpose: 枚举类型参数
	 * @author allenchen
	 * @since  JDK 1.7
	 * @date   2016/5/18
	 * @MaintenancePersonnel allenchen
	 */
	public static enum ATTRIBUTE {
		NUMBER("number"),
		ASSET_TYPE_NAME("assetTypeName"),
		SAFETY_STOCK("safetyStock"),
		ASSET_TYPE_ID("assetTypeId"),
		AMOUNT("amount"),
		RENT("rent"),
		FINE("fine"),
		ASSET_CATEGORY("assetCategory"),
		ASSET_CATEGORY_NAME("assetCategoryName"),
		PRICE("price");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	private String number;//設備數量 
	private String assetTypeName;//设备名称
	private Long safetyStock;//安全库存
	private String assetTypeId;//ID 
	
	private Long amount; //数量
	private Integer rent;//租赁金额
	private Integer fine;//赔偿金额
	
	private String assetCategory;//設備類別
	private String assetCategoryName;//設備類別名稱
	/**
	 * 售價
	 */
	private Double price;
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	/**
	 * @return the safetyStock
	 */
	public Long getSafetyStock() {
		return safetyStock;
	}
	/**
	 * @param safetyStock the safetyStock to set
	 */
	public void setSafetyStock(Long safetyStock) {
		this.safetyStock = safetyStock;
	}
	/**
	 * @return the amount
	 */
	public Long getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return the rent
	 */
	public Integer getRent() {
		return rent;
	}
	/**
	 * @param rent the rent to set
	 */
	public void setRent(Integer rent) {
		this.rent = rent;
	}
	/**
	 * @return the fine
	 */
	public Integer getFine() {
		return fine;
	}
	/**
	 * @param fine the fine to set
	 */
	public void setFine(Integer fine) {
		this.fine = fine;
	}
	/**
	 * @return the assetTypeName
	 */
	public String getAssetTypeName() {
		return assetTypeName;
	}
	/**
	 * @param assetTypeName the assetTypeName to set
	 */
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
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
	/**
	 * @return the assetCategory
	 */
	public String getAssetCategory() {
		return assetCategory;
	}
	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}
	/**
	 * @return the assetCategoryName
	 */
	public String getAssetCategoryName() {
		return assetCategoryName;
	}
	/**
	 * @param assetCategoryName the assetCategoryName to set
	 */
	public void setAssetCategoryName(String assetCategoryName) {
		this.assetCategoryName = assetCategoryName;
	}
	
	
}
