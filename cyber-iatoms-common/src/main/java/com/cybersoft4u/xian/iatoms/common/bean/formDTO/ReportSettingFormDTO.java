package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDetailDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ReportSettingDTO;

/**
 * Purpose:報表發送功能設定FormDTO 
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel ElvaHe
 */
@SuppressWarnings("rawtypes")
public class ReportSettingFormDTO extends AbstractSimpleListFormDTO<ReportSettingDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3166952486022656963L;

	/**
	 * 常量
	 */
	/**
	 * 客戶集合
	 */
	public static final String CUSTOMER_LIST 						= "customerList";
	/**
	 * 查詢條件 --- 顧客ID
	 */
	public static final String QUERY_CUSTOMER_ID					= "queryCustomerId";
	/**
	 * 查詢條件 --- 報表編號
	 */
	public static final String QUERY_REPORT_CODE 					= "queryReportCode";
	/**
	 * 靜態常量-頁面排序字段參數
	 */
	public static final String PARAM_SORT_NAME						= "customerName,reportName";
	/**
	 * 明細集合
	 */
	public static final String PARAM_REPORT_DETAIL_LIST				= "reportDetailList";
	/**
	 * 報表名稱的bptdCode
	 */
	public static final String PARAM_REPORT_BPTDCODE				= "bptdCode";
	/**
	 * 明細的bptdCode
	 */
	public static final String PARAM_REPORT_DETAIL_BPTDCODE			= "detailCode";
	/**
	 * 重送標誌
	 */
	public static final String PARAM_REPORT_SENDFLAG			= "sendFlag";
	/**
	 * EDC合約到期提示報表 -- 郵件主題
	 */
	public static final String EDC_CONTRACT_EXPIRE_SUBJECT_TEMPLATE	= "IATOMS-EMAIL-SUBJECT-BRM08040-E0001";
	/**
	 * 當前頁碼
	 */
	private Integer page;

	/**
	 * 一頁顯示多少筆
	 */
	private Integer rows;

	/**
	 * 按此字段進行排序
	 */
	private String sort;

	/**
	 * 排序方式
	 */
	private String order;

	/**
	 * 主鍵
	 */
	private String settingId;
	
	/**
	 * 客戶編號
	 */
	private String customerId;
	
	/**
	 * 報表編號
	 */
	private String reportCode;
	
	/**
	 * 查詢條件：客戶編號
	 */
	private String queryCustomerId;
	/**
	 * 明細編號
	 */
	private String reportDetatilId;
	/**
	 * 查詢條件：報表名稱
	 */
	private String queryReportCode;
	
	/**
	 * 報表發送功能設定
	 */
	private ReportSettingDTO reportSettingDTO;

	/**
	 * 報表明細
	 */
	private ReportSettingDetailDTO reportSettingDetailDTO;
	/**
	 * 報表名稱下拉列表
	 */
	private List<Parameter> reportList;
	/**
	 * 報表的明細列表
	 */
	private List<ReportSettingDetailDTO> reportSettingDetatilDTOs;
	/**
	 * 報表的明細列表（List<String>）
	 */
	private List<String> details;
	/**
	 * 報表查詢結果集合
	 */
	private List<ReportSettingDTO> reportSettingList;

	/**
	 * 重送標誌
	 */
	private String sendFlag;
	/**
	 * 重送頁面 -- 報表日期
	 */
	private String createdDateString;
	
	/**
	 * Constructor:
	 */
	public ReportSettingFormDTO() {
	}

	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * @return the rows
	 */
	public Integer getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
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
	 * @return the queryCustomerId
	 */
	public String getQueryCustomerId() {
		return queryCustomerId;
	}

	/**
	 * @param queryCustomerId the queryCustomerId to set
	 */
	public void setQueryCustomerId(String queryCustomerId) {
		this.queryCustomerId = queryCustomerId;
	}

	/**
	 * @return the queryReportCode
	 */
	public String getQueryReportCode() {
		return queryReportCode;
	}

	/**
	 * @param queryReportCode the queryReportCode to set
	 */
	public void setQueryReportCode(String queryReportCode) {
		this.queryReportCode = queryReportCode;
	}

	/**
	 * @return the reportSettingDTO
	 */
	public ReportSettingDTO getReportSettingDTO() {
		return reportSettingDTO;
	}

	/**
	 * @param reportSettingDTO the reportSettingDTO to set
	 */
	public void setReportSettingDTO(ReportSettingDTO reportSettingDTO) {
		this.reportSettingDTO = reportSettingDTO;
	}

	
	/**
	 * @return the reportSettingDetailDTO
	 */
	public ReportSettingDetailDTO getReportSettingDetailDTO() {
		return reportSettingDetailDTO;
	}

	/**
	 * @param reportSettingDetailDTO the reportSettingDetailDTO to set
	 */
	public void setReportSettingDetailDTO(
			ReportSettingDetailDTO reportSettingDetailDTO) {
		this.reportSettingDetailDTO = reportSettingDetailDTO;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	 * @return the reportList
	 */
	public List<Parameter> getReportList() {
		return reportList;
	}

	/**
	 * @param reportList the reportList to set
	 */
	public void setReportList(List<Parameter> reportList) {
		this.reportList = reportList;
	}

	/**
	 * @return the reportSettingList
	 */
	public List<ReportSettingDTO> getReportSettingList() {
		return reportSettingList;
	}

	/**
	 * @param reportSettingList the reportSettingList to set
	 */
	public void setReportSettingList(List<ReportSettingDTO> reportSettingList) {
		this.reportSettingList = reportSettingList;
	}

	/**
	 * @return the reportSettingDetatilDTOs
	 */
	public List<ReportSettingDetailDTO> getReportSettingDetatilDTOs() {
		return reportSettingDetatilDTOs;
	}

	/**
	 * @param reportSettingDetatilDTOs the reportSettingDetatilDTOs to set
	 */
	public void setReportSettingDetatilDTOs(
			List<ReportSettingDetailDTO> reportSettingDetatilDTOs) {
		this.reportSettingDetatilDTOs = reportSettingDetatilDTOs;
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
	 * @return the details
	 */
	public List<String> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<String> details) {
		this.details = details;
	}

	/**
	 * @return the sendFlag
	 */
	public String getSendFlag() {
		return sendFlag;
	}

	/**
	 * @param sendFlag the sendFlag to set
	 */
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
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

	
}
