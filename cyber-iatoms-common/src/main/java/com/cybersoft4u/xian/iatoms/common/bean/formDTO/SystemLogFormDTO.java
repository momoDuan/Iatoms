package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;
import java.util.Map;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

/**
 * Purpose: 系統日志查詢FormDTO
 * @author amandawang
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel amandawang
 */
public class SystemLogFormDTO extends AbstractSimpleListFormDTO<AdmSystemLoggingDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -4066187159293493303L;
	/**
	 * JRXML名稱
	 */
	public static final String REPORT_JRXML_NAME												= "SYSTEM_LOG_REPORT";
	/**
	 * 報表名
	 */
	public static final String REPORT_FILE_NAME													= "系統log清單";
	
	/**
	 * 時間迄
	 */
	public static final String QUERY_PAGE_PARAM_FROM_DATE										= "queryFromDate";
	/**
	 * 時間乾
	 */
	public static final String QUERY_PAGE_PARAM_TO_DATE											= "queryToDate";
	/**
	 * 帳號
	 */
	public static final String QUERY_PAGE_PARAM_ACCOUNT											= "queryAccount";
	/**
	 * 排序欄位
	 */
	public static final String PARAM_PAGE_SORT													="operationTime";
	/**
	 * 排序方式
	 */
	public static final String PARAM_PAGE_ORDER													="desc";
	/**
	 * 時間迄
	 */
	private String queryFromDate;
	/**
	 * 時間乾
	 */
	private String queryToDate;
	/**
	 * 帳號
	 */
	private String queryAccount;
	/**
	 * 圖片名稱
	 */
	private String iamgeName;
	/**
	 * 每頁顯示條數
	 */
	private Integer rows;
	/**
	 * 當前頁碼
	 */
	private Integer page;
	/**
	 * 排序方式
	 */
	private String sort;
	/**
	 * 排序字段
	 */
	private String order;
	/**
	 * 查詢結果總筆數
	 */
	private Integer totalSize;
	/**
	 * 日誌數據
	 */
	private String logContent;
	/**
	 * 日誌編號
	 */
	private Integer logId;
	/**
	 * 日誌類型
	 */
	private String logCategre;
	/**
	 * 功能編號
	 */
	private String functionName;
	/**
	 * 功能權限
	 */
	private Map<String, List<String>> functionPermissions;
	
	/**
	 * 
	 * Constructor: 無參構造函數
	 */
	public SystemLogFormDTO(){
		super();
	}

	/**
	 * @return the queryToDate
	 */
	public String getQueryToDate() {
		return queryToDate;
	}

	/**
	 * @param queryToDate the queryToDate to set
	 */
	public void setQueryToDate(String queryToDate) {
		this.queryToDate = queryToDate;
	}

	/**
	 * @return the queryFromDate
	 */
	public String getQueryFromDate() {
		return queryFromDate;
	}

	/**
	 * @param queryFromDate the queryFromDate to set
	 */
	public void setQueryFromDate(String queryFromDate) {
		this.queryFromDate = queryFromDate;
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
	 * @return the queryAccount
	 */
	public String getQueryAccount() {
		return queryAccount;
	}

	/**
	 * @param queryAccount the queryAccount to set
	 */
	public void setQueryAccount(String queryAccount) {
		this.queryAccount = queryAccount;
	}

	/**
	 * @return the iamgeName
	 */
	public String getIamgeName() {
		return iamgeName;
	}

	/**
	 * @param iamgeName the iamgeName to set
	 */
	public void setIamgeName(String iamgeName) {
		this.iamgeName = iamgeName;
	}

	/**
	 * @return the totalSize
	 */
	public Integer getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
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
	 * @return the logId
	 */
	public Integer getLogId() {
		return logId;
	}

	/**
	 * @param logId the logId to set
	 */
	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	/**
	 * @return the logCategre
	 */
	public String getLogCategre() {
		return logCategre;
	}

	/**
	 * @param logCategre the logCategre to set
	 */
	public void setLogCategre(String logCategre) {
		this.logCategre = logCategre;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @return the logContent
	 */
	public String getLogContent() {
		return logContent;
	}

	/**
	 * @param logContent the logContent to set
	 */
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	/**
	 * @return the functionPermissions
	 */
	public Map<String, List<String>> getFunctionPermissions() {
		return functionPermissions;
	}

	/**
	 * @param functionPermissions the functionPermissions to set
	 */
	public void setFunctionPermissions(Map<String, List<String>> functionPermissions) {
		this.functionPermissions = functionPermissions;
	}
}
