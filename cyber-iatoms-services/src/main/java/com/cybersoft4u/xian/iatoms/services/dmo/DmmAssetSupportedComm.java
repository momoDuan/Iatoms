package com.cybersoft4u.xian.iatoms.services.dmo;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedCommDTO;

import cafe.core.dmo.DomainModelObject;

/**
 * 
 * Purpose: 設備支援通訊模式檔
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel ericdu
 */
public class DmmAssetSupportedComm extends
		DomainModelObject<DmmAssetSupportedCommId, AssetSupportedCommDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -8711192494423427249L;
	
	/**
	 * Constructor: 無參構造
	 */
	public DmmAssetSupportedComm() {
		super();
	}
	
	/**
	 * Constructor: 有參構造
	 */
	public DmmAssetSupportedComm(DmmAssetSupportedCommId id) {
		super(id);
	}
	
	
}
