package com.cybersoft4u.xian.iatoms.services.dmo;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeCompanyDTO;

import cafe.core.dmo.DomainModelObject;

/**
 * Purpose: 廠商使用設備檔
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel ericdu
 */
public class DmmAssetTypeCompany extends DomainModelObject<DmmAssetTypeCompanyId, AssetTypeCompanyDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -7974984563179097831L;
	
	/**
	 * 
	 * Constructor: 構造函數
	 */
	public DmmAssetTypeCompany(){
		super();
	}
	
	/**
	 * 
	 * Constructor: 構造函數
	 */
	public DmmAssetTypeCompany(DmmAssetTypeCompanyId id){
		super(id);
		this.id = id;
	}
}
