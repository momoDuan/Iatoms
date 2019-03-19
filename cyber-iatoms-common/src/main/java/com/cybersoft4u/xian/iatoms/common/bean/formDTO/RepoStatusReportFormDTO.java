package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;

/**
 * 
 * Purpose: 設備狀態報表FormDTO
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月22日
 * @MaintenancePersonnel ericdu
 */
public class RepoStatusReportFormDTO extends
		AbstractSimpleListFormDTO<RepoStatusReportDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -8682701181250914712L;
	
	/**
	 * 匯出總計數據的報表名稱
	 */
	public static final String EXPORT_JRXML_NAME_DETAIL									= "REPOSITORY_STATUS_REPORT";
	/**
	 * 設備狀態報表匯出的報表名稱
	 */
	public static final String EXPORT_FILE_NAME_DETAIL									= "IATOMS_MSG_DMM03090_I0001";
	/**
	 * 匯出具體數據的報表名稱
	 */
	public static final String EXPORT_JRXML_NAME_LIST									= "REPOSITORY_STATUS_LIST_REPORT";
	/**
	 * 設備狀態報表匯出的報表名稱集合
	 */
	public static final String EXPORT_FILE_NAME_LIST									= "IATOMS_MSG_DMM03090_I0002";
	/**
	 * 庫存狀態
	 */
	public static final String EXPORT_REPORT_TYPE										= "reportType";
	/**
	 * 庫存狀態集合
	 */
	public static final String EXPORT_REPORT_TYPE_LIST									= "statusList";
	/**
	 * 狀態明細
	 */
	public static final String EXPORT_EXPORT_TYPE_DETAIL								= "statusDetail";
	/**
	 * 查詢條件 -- 客戶
	 */
	public static final String QUERY_PAGE_PARAM_CUSTOMER								= "queryCustomer";
	/**
	 * 查詢條件 -- 維護模式
	 */
	public static final String QUERY_PAGE_PARAM_MA_TYPE									= "queryMaType";
	/**
	 * 查詢條件 -- 設備名稱
	 */
	public static final String QUERY_PAGE_PARAM_ASSET_NAMES								= "queryAssetName";
	/**
	 * 查詢條件 -- 通訊模式
	 */
	public static final String QUERY_PAGE_PARAM_COMM_MODES								= "queryCommMode";
	/**
	 * 查詢條件 -- 查詢月份
	 */
	public static final String QUERY_PAGE_PARAM_DATE									= "queryDate";
	
	private String queryCustomer;
	private String queryMaType;
	private String queryDate;
	private String queryAssetNames;
	private String queryCommModes;
	/**
	 * 排序參考列
	 */
	private String sort;
	/**
	 * 排序方式
	 */
	private String order;
	
	/**
	 * 當前頁
	 */
	private Integer page;
	
	/**
	 * 每頁顯示多少筆
	 */
	private Integer rows;
	/**
	 * 格式化后的查詢月份
	 */
	private String yyyyMM;
	/**
	 * 庫存狀態
	 */
	private String reportType;
	
	
	/**
	 * 當前登入者角色屬性
	 */
	private String roleAttribute;
	
	/**
	 * 當前登入者為客戶屬性對應之公司
	 */
	private String logonUserCompanyId;
	
	/**
	 * 公司ID集合
	 */
	private List<String> companyIdList;
	/**
	 * Constructor: 無參構造
	 */
	public RepoStatusReportFormDTO() {
		super();
	}

	/**
	 * @return the queryCustomer
	 */
	public String getQueryCustomer() {
		return queryCustomer;
	}

	/**
	 * @param queryCustomer the queryCustomer to set
	 */
	public void setQueryCustomer(String queryCustomer) {
		this.queryCustomer = queryCustomer;
	}

	/**
	 * @return the queryMaType
	 */
	public String getQueryMaType() {
		return queryMaType;
	}

	/**
	 * @param queryMaType the queryMaType to set
	 */
	public void setQueryMaType(String queryMaType) {
		this.queryMaType = queryMaType;
	}

	/**
	 * @return the queryDate
	 */
	public String getQueryDate() {
		return queryDate;
	}

	/**
	 * @param queryDate the queryDate to set
	 */
	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	/**
	 * @return the queryAssetNames
	 */
	public String getQueryAssetNames() {
		return queryAssetNames;
	}

	/**
	 * @param queryAssetNames the queryAssetNames to set
	 */
	public void setQueryAssetNames(String queryAssetNames) {
		this.queryAssetNames = queryAssetNames;
	}

	/**
	 * @return the queryCommModes
	 */
	public String getQueryCommModes() {
		return queryCommModes;
	}

	/**
	 * @param queryCommModes the queryCommModes to set
	 */
	public void setQueryCommModes(String queryCommModes) {
		this.queryCommModes = queryCommModes;
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
	 * @return the yyyyMM
	 */
	public String getYyyyMM() {
		return yyyyMM;
	}

	/**
	 * @param yyyyMM the yyyyMM to set
	 */
	public void setYyyyMM(String yyyyMM) {
		this.yyyyMM = yyyyMM;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * @return the roleAttribute
	 */
	public String getRoleAttribute() {
		return roleAttribute;
	}

	/**
	 * @param roleAttribute the roleAttribute to set
	 */
	public void setRoleAttribute(String roleAttribute) {
		this.roleAttribute = roleAttribute;
	}

	/**
	 * @return the logonUserCompanyId
	 */
	public String getLogonUserCompanyId() {
		return logonUserCompanyId;
	}

	/**
	 * @param logonUserCompanyId the logonUserCompanyId to set
	 */
	public void setLogonUserCompanyId(String logonUserCompanyId) {
		this.logonUserCompanyId = logonUserCompanyId;
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
	 * @return the companyIdList
	 */
	public List<String> getCompanyIdList() {
		return companyIdList;
	}

	/**
	 * @param companyIdList the companyIdList to set
	 */
	public void setCompanyIdList(List<String> companyIdList) {
		this.companyIdList = companyIdList;
	}
}
