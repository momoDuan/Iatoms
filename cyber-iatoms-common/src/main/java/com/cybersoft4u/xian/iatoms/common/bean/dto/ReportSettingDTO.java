package com.cybersoft4u.xian.iatoms.common.bean.dto;

import java.sql.Timestamp;

import cafe.core.bean.dto.DataTransferObject;
/**
 * Purpose: 報表發送功能設定DTO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel ElvaHe
 */
public class ReportSettingDTO extends DataTransferObject<String> {
	
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 1771227791345568285L;
	
	public static enum ATTRIBUTE {
		SETTING_ID("settingId"),
		COMPANY_ID("companyId"),
		CUSTOMER_CODE("customerCode"),
		CUSTOMER_NAME("customerName"),
		REPORT_CODE("reportCode"),
		REPORT_NAME("reportName"),
		RECIPIENT("recipient"),
		CREATED_BY_ID("createdById"),
		CREATED_BY_NAME("createdByName"),
		CREATED_DATE_STRING("createdDateString"),
		UPDATED_BY_ID("updatedById"),
		UPDATED_BY_NAME("updatedByName"),
		UPDATED_DATE("updatedDate"),
		COPY("copy"),
		REPORT_DETAIL("reportDetail"),
		REPORT_DETAIL_ID("reportDetatilId"),
		REMARK("remark");

		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 報表發送主鍵編號
	 */
	private String settingId;
	/**
	 * 客戶編號
	 */
	private String companyId;
	/**
	 * 客戶名稱
	 */
	private String customerName;
	/**
	 * 客戶code
	 */
	private String customerCode;
	/**
	 * 報表編號
	 */
	private String reportCode;
	/**
	 * 報表名稱
	 */
	private String reportName;
	/**
	 * 報表明細
	 */
	private String reportDetail;
	/**
	 * 報表明細編號
	 */
	private String reportDetatilId;
	/**
	 * 收件人 
	 */
	private String recipient;
	/**
	 * 創建者編號
	 */
	private String createdById;
	/**
	 * 創建者姓名
	 */
	private String createdByName;
	/**
	 * 創建日期
	 */
	private String createdDateString;
	/**
	 * 更新者編號
	 */
	private String updatedById;
	/**
	 * 更新者名稱
	 */
	private String updatedByName;
	/**
	 * 更新日期
	 */
	private Timestamp updatedDate;
	/**
	 * 副本
	 */
	private String copy;
	/**
	 * 備註
	 */
	private String remark;
	/**
	 * 序號
	 */
	private Integer rowNumber;
	
	/**
	 * Constructor:
	 */
	public ReportSettingDTO() {
	}
	/**
	 * Constructor:
	 */
	public ReportSettingDTO(String settingId, String companyId,
			String customerName, String reportCode, String reportName,
			String reportDetail, String reportDetatilId, String recipient,
			String createdById, String createdByName, String createdDateString,
			String updatedById, String updatedByName, Timestamp updatedDate,
			String copy, String remark) {
		this.settingId = settingId;
		this.companyId = companyId;
		this.customerName = customerName;
		this.reportCode = reportCode;
		this.reportName = reportName;
		this.reportDetail = reportDetail;
		this.reportDetatilId = reportDetatilId;
		this.recipient = recipient;
		this.createdById = createdById;
		this.createdByName = createdByName;
		this.createdDateString = createdDateString;
		this.updatedById = updatedById;
		this.updatedByName = updatedByName;
		this.updatedDate = updatedDate;
		this.copy = copy;
		this.remark = remark;
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
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the reportCode
	 */
	public String getReportCode() {
		return reportCode;
	}
	/**
	 * @param reportCode the reportCode to set
	 */
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
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
	 * @return the reportDetatilId
	 */
	public String getReportDetatilId() {
		return reportDetatilId;
	}
	/**
	 * @param reportDetatilId the reportDetatilId to set
	 */
	public void setReportDetatilId(String reportDetatilId) {
		this.reportDetatilId = reportDetatilId;
	}
	/**
	 * @return the recipient
	 */
	public String getRecipient() {
		return recipient;
	}
	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the createdById
	 */
	public String getCreatedById() {
		return createdById;
	}
	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}
	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	
	/**
	 * @return the createdDateString
	 */
	public String getCreatedDateString() {
		return createdDateString;
	}
	/**
	 * @param createdDateString the createdDateString to set
	 */
	public void setCreatedDateString(String createdDateString) {
		this.createdDateString = createdDateString;
	}
	/**
	 * @return the updatedById
	 */
	public String getUpdatedById() {
		return updatedById;
	}
	/**
	 * @param updatedById the updatedById to set
	 */
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}
	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	/**
	 * @return the updatedDate
	 */
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	/**
	 * @return the copy
	 */
	public String getCopy() {
		return copy;
	}
	/**
	 * @param copy the copy to set
	 */
	public void setCopy(String copy) {
		this.copy = copy;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the rowNumber
	 */
	public Integer getRowNumber() {
		return rowNumber;
	}
	/**
	 * @param rowNumber the rowNumber to set
	 */
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}
	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	
}
