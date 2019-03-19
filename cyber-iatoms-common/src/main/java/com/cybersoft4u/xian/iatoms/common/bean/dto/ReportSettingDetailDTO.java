package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * Purpose:報表發送明細檔DTO 
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年8月5日
 * @MaintenancePersonnel ElvaHe
 */
public class ReportSettingDetailDTO extends DataTransferObject<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6215111241717454667L;

	public static enum ATTRIBUTE {
		SETTING_ID("settingId"),
		REPORT_DETAIL("reportDetail");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};

	
	/**
	 * 報表ID
	 */
	private String settingId;
	
	/**
	 * 報表明細編號
	 */
	private String reportDetail;
	/**
	 * 報表明細數組
	 */
	private String[] details;
	/**
	 * 
	 * Constructor:
	 */
	public ReportSettingDetailDTO() {
		
	}

	/**
	 * 
	 * Constructor:
	 */
	public ReportSettingDetailDTO(String settingId, String reportDetail) {
		this.settingId = settingId;
		this.reportDetail = reportDetail;
	}

	/**
	 * @return the settingId
	 */
	public String getSettingId() {
		return settingId;
	}

	/**
	 * @param settingId the settingId to set
	 */
	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	/**
	 * @return the reportDetail
	 */
	public String getReportDetail() {
		return reportDetail;
	}

	/**
	 * @param reportDetail the reportDetail to set
	 */
	public void setReportDetail(String reportDetail) {
		this.reportDetail = reportDetail;
	}

	/**
	 * @return the details
	 */
	public String[] getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String[] details) {
		this.details = details;
	}

}
