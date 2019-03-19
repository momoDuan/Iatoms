package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
import cafe.core.web.controller.util.BindPageDataUtils;

/**
 * Purpose: 廠商使用設備檔
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月8日
 * @MaintenancePersonnel ericdu
 */
public class AssetTypeCompanyDTO extends
		DataTransferObject<AssetTypeCompanyIdDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 590161249939980762L;
	private String assetTypeId;
	private String companyId;
	
	/**
	 * Constructor: 無參構造
	 */
	public AssetTypeCompanyDTO() {
		super();
	}
	
	
	public static enum ATTRIBUTE {
		ASSET_TYPE_ID("assetTypeId"),
		COMPANY_ID("companyId");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * Constructor: 有參構造
	 */
	public AssetTypeCompanyDTO(AssetTypeCompanyIdDTO id) {
		super(id);
		this.id = id;
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
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
