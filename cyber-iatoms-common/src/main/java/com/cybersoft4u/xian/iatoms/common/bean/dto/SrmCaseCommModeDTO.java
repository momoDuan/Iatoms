package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose: 案件處理中設備支援功能檔DTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017/3/1
 * @MaintenancePersonnel CrissZhang
 */
public class SrmCaseCommModeDTO extends DataTransferObject<String> {

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
		COMM_MODE_ID("commModeId");

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
	 * 通訊方式id
	 */
	private String commModeId;
	

	/**
	 * Constructor:無參構造函數
	 */
	public SrmCaseCommModeDTO() {
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
	 * @return the commModeId
	 */
	public String getCommModeId() {
		return commModeId;
	}


	/**
	 * @param commModeId the commModeId to set
	 */
	public void setCommModeId(String commModeId) {
		this.commModeId = commModeId;
	}

}
