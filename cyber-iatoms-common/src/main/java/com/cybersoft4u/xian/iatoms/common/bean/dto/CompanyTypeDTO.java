package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 公司資料類型DTO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年8月19日
 * @MaintenancePersonnel ElvaHe
 */
public class CompanyTypeDTO extends DataTransferObject<String> {
	
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 2380044162698206492L;

	public static enum ATTRIBUTE {
		COMPANY_ID("companyId"),
		COMPANY_TYPE("companyType");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	
	/**
	 * 公司編號
	 */
	private String companyId;
	/**
	 * 公司類型
	 */
	private String companyType;
	
	/**
	 * Constructor:無參構造函數
	 */
	public CompanyTypeDTO() {
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
	/**
	 * @return the companyType
	 */
	public String getCompanyType() {
		return companyType;
	}
	/**
	 * @param companyType the companyType to set
	 */
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

}
