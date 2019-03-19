package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.CompositeIdentifier;

/**
 * Purpose: 用户控管资料主键DTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/8/19
 * @MaintenancePersonnel CrissZhang
 */
public class AdmUserWarehouseIdDTO extends CompositeIdentifier{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -7220689570340143870L;
	
	/**
	 * Constructor:空的构造函数
	 */
	public AdmUserWarehouseIdDTO() {
	}
	
	/**
	 * 使用者主鍵id
	 */
	private String userId;
	/**
	 * 倉庫控管主鍵id
	 */
	private String warehouseId;

	/**
	 * Constructor:有参构造函数
	 */
	public AdmUserWarehouseIdDTO(String userId, String warehouseId) {
		this.userId = userId;
		this.warehouseId = warehouseId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the warehouseId
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

}
