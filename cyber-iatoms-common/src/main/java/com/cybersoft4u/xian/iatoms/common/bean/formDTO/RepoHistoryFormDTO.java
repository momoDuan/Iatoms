package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;

/**
 * 
 * Purpose: 設備歷史記錄查詢FormDTO
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月19日
 * @MaintenancePersonnel ericdu
 */
public class RepoHistoryFormDTO extends
		AbstractSimpleListFormDTO<DmmRepositoryHistoryDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 3937557270684024690L;
	
	public static final String QUERY_PAGE_SERIAL_NUMBER										= "querySerialNumber";
	public static final String QUERY_PAGE_TID												= "queryTID";
	public static final String QUERY_PAGE_DTID												= "queryDTID";
	
	public static final String EXPORT_PARAM_FIELDS											= "exportField";
	public static final String EXPORT_PARAM_HIST_IDS										= "histId";
	
	public static final String EXPORT_JRXML_NAME											= "REPO_HISTORY_REPORT";
	public static final String EXPORT_FILE_NAME												= "設備歷史記錄";
	
	private String querySerialNumber;
	private String queryTID;
	private String queryDTID;
	
	private String order;
	private String sort;
	
	private String[] exportFields;
	private String[] histIds;

	/**
	 * Constructor: 無參構造
	 */
	public RepoHistoryFormDTO() {
		super();
	}

	/**
	 * @return the querySerialNumber
	 */
	public String getQuerySerialNumber() {
		return querySerialNumber;
	}

	/**
	 * @param querySerialNumber the querySerialNumber to set
	 */
	public void setQuerySerialNumber(String querySerialNumber) {
		this.querySerialNumber = querySerialNumber;
	}

	/**
	 * @return the queryTID
	 */
	public String getQueryTID() {
		return queryTID;
	}

	/**
	 * @param queryTID the queryTID to set
	 */
	public void setQueryTID(String queryTID) {
		this.queryTID = queryTID;
	}

	/**
	 * @return the queryDTID
	 */
	public String getQueryDTID() {
		return queryDTID;
	}

	/**
	 * @param queryDTID the queryDTID to set
	 */
	public void setQueryDTID(String queryDTID) {
		this.queryDTID = queryDTID;
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
	 * @return the exportFields
	 */
	public String[] getExportFields() {
		return exportFields;
	}

	/**
	 * @param exportFields the exportFields to set
	 */
	public void setExportFields(String[] exportFields) {
		this.exportFields = exportFields;
	}

	/**
	 * @return the histIds
	 */
	public String[] getHistIds() {
		return histIds;
	}

	/**
	 * @param histIds the histIds to set
	 */
	public void setHistIds(String[] histIds) {
		this.histIds = histIds;
	}
}
