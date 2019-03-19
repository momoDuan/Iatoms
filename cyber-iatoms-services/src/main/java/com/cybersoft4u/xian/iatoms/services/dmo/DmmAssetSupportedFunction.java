package com.cybersoft4u.xian.iatoms.services.dmo;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedFunctionDTO;

import cafe.core.dmo.DomainModelObject;

/**
 * Purpose: 設備支援功能關系表
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月11日
 * @MaintenancePersonnel ericdu
 */
public class DmmAssetSupportedFunction extends
		DomainModelObject<DmmAssetSupportedFunctionId, AssetSupportedFunctionDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -5882910833208215921L;
	
	/**
	 * 
	 * Constructor: 無參構造函數
	 */
	public DmmAssetSupportedFunction(){
		super();
	}
	
	/**
	 * Constructor: 有參構造
	 */
	public DmmAssetSupportedFunction(DmmAssetSupportedFunctionId id) {
		super(id);
		this.id = id;
	}
}
