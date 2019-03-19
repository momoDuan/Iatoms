package com.cybersoft4u.xian.iatoms.common.reportsetting;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose:维护费设备費用單價 
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/8/23
 * @MaintenancePersonnel CarrieDuan
 */
public class AssetMaintainFeeSetting extends DataTransferObject<String> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -8202807629225445585L;
	/**
	 * 
	 * Constructor:空構造
	 */
	public AssetMaintainFeeSetting() {
		super();
	}
	/**
	 * 設備數量
	 */
	private String assetTotal;
	/**
	 * 第一個維修費
	 */
	private String firstMaintainFee;
	/**
	 * 其餘維修費
	 */
	private String otherMaintainFee;
	/**
	 * @return the assetTotal
	 */
	public String getAssetTotal() {
		return assetTotal;
	}
	/**
	 * @param assetTotal the assetTotal to set
	 */
	public void setAssetTotal(String assetTotal) {
		this.assetTotal = assetTotal;
	}
	/**
	 * @return the firstMaintainFee
	 */
	public String getFirstMaintainFee() {
		return firstMaintainFee;
	}
	/**
	 * @param firstMaintainFee the firstMaintainFee to set
	 */
	public void setFirstMaintainFee(String firstMaintainFee) {
		this.firstMaintainFee = firstMaintainFee;
	}
	/**
	 * @return the otherMaintainFee
	 */
	public String getOtherMaintainFee() {
		return otherMaintainFee;
	}
	/**
	 * @param otherMaintainFee the otherMaintainFee to set
	 */
	public void setOtherMaintainFee(String otherMaintainFee) {
		this.otherMaintainFee = otherMaintainFee;
	}
	
}
