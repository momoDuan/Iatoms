package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 案件處理中設備支援功能檔DTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/3/1
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseAssetFunctionDTO extends DataTransferObject<String> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 2453315587755776177L;

	/**
	 * Purpose: enum
	 * @author CrissZhang
	 * @since  JDK 1.6
	 * @date   2017/3/1
	 * @MaintenancePersonnel CrissZhang
	 */
	public static enum ATTRIBUTE {
		ID("id"),
		CASE_ID("caseId"),
		FUNCTION_CATEGORY("functionCategory"),
		FUNCTION_ID("functionId");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 編號
	 */
	private String id;
	
	/**
	 * 案件單號
	 */
	private String caseId;
	
	/**
	 * 設備名稱功能類型
	 */
	private String functionCategory;
	
	/**
	 * 支援功能
	 */
	private String functionId;

	/**
	 * Constructor:無參構造函數
	 */
	public SrmCaseAssetFunctionDTO() {
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}

	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	/**
	 * @return the functionCategory
	 */
	public String getFunctionCategory() {
		return functionCategory;
	}

	/**
	 * @param functionCategory the functionCategory to set
	 */
	public void setFunctionCategory(String functionCategory) {
		this.functionCategory = functionCategory;
	}

	/**
	 * @return the functionId
	 */
	public String getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	
}
