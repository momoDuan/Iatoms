package com.cybersoft4u.xian.iatoms.common.bean.formDTO;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;


/**
 * Purpose: 設備轉倉作業-轉入驗收
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月15日
 * @MaintenancePersonnel ericdu
 */

public class CheckTransInfoFormDTO extends AbstractSimpleListFormDTO<DmmAssetTransListDTO> {

	/**
	 * 系統日志記錄
	 */
	private static final long serialVersionUID = -6605566956320225050L;
	
	/*
	 * 查詢條件常量設定
	 */
	public static final String QUERY_PAGE_ASSET_TRANS_ID				= "queryAssetTransId";
	public static final String QUERY_PAGE_OPTION						= "option";
	public static final String REPORT_JRXML_NAME						= "ASSET_TRANS_INFO";
	public static final String REPORT_FILE_NAME 						= "設備轉倉匯出";
	public static final String CHECK_TRANS_MAILSUBJECT					= "CHECK_TRANS_MAILSUBJECT";
	
	/*
	 * 查詢條件--轉倉批號
	 */
	private String queryAssetTransId;
	
	/*
	 * 排序欄位
	 */
	private String sort;
	
	/*
	 * 排序方式
	 */
	private String order;
	
	/**
	 * 當前頁碼
	 */
	private Integer page;

	/**
	 * 一頁顯示多少筆
	 */
	private Integer rows;
	
	/*
	 * 入庫/退回標記
	 */
	private String option;
	/**
	 * 退回原因
	 */
	private String refuseReason;

	/**
	 * 
	 * Constructor: 無參構造
	 */
	public CheckTransInfoFormDTO() {
		super();
	}

	/**
	 * @return the queryAssetTransId
	 */
	public String getQueryAssetTransId() {
		return queryAssetTransId;
	}

	/**
	 * @param queryAssetTransId the queryAssetTransId to set
	 */
	public void setQueryAssetTransId(String queryAssetTransId) {
		this.queryAssetTransId = queryAssetTransId;
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
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * @return the refuseReason
	 */
	public String getRefuseReason() {
		return refuseReason;
	}

	/**
	 * @param refuseReason the refuseReason to set
	 */
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
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

}
