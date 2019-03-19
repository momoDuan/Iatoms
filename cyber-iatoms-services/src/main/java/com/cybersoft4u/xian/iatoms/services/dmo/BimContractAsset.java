package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;

// Generated 2016/6/3 �U�� 01:30:43 by Hibernate Tools 3.4.0.CR1

/**
 * Purpose: 合约设备DMO
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016/6/17
 * @MaintenancePersonnel allenchen
 */
public class BimContractAsset extends  DomainModelObject<BimContractAssetId,BimContractAssetDTO> {

	private BimContractAssetId id;//厂商设备ID
	private Long amount;//设备数量
	private Long safetyStock;//设备价格
	private Double price;// 售價
	
	public BimContractAsset() {
	}

	public BimContractAsset(BimContractAssetId id) {
		this.id = id;
	}

	public BimContractAsset(BimContractAssetId id, Long amount,
			Long safetyStock, Double price) {
		this.id = id;
		this.amount = amount;
		this.safetyStock = safetyStock;
		this.price = price;
	}

	/**
	 * @return the id
	 */
	public BimContractAssetId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BimContractAssetId id) {
		this.id = id;
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
}
