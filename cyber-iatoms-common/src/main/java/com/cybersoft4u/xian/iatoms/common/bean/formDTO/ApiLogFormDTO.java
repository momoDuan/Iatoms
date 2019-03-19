package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiLogDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

/**
 * Purpose:電文記錄查詢
 * @author NickLin
 * @since  JDK 1.7
 * @date   2018/06/12
 * @MaintenancePersonnel CyberSoft
 */
public class ApiLogFormDTO extends AbstractSimpleListFormDTO<ApiLogDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -4453270148004330889L;
	/**
	 * 查詢條件:client_code
	 */
	public static final String QUERY_CLIENT_CODE = "queryClientCode";
	/**
	 * 查詢條件:建檔日期起
	 */
	public static final String QUERY_START_DATE = "queryStartDate";
	/**
	 * 查詢條件:建檔日期迄
	 */
	public static final String QUERY_END_DATE = "queryEndDate";
	/**
	 * 排序方式
	 */
	public static final String PARAM_PAGE_ORDER = "desc";
	/**
	 * 排序欄位
	 */
	public static final String PARAM_PAGE_SORT = "createdDate";
	/**
	 * 查詢條件:client_code
	 */
	private String queryClientCode;
	/**
	 * 查詢條件:建檔日期起
	 */
	private String queryStartDate;
	/**
	 * 查詢條件:建檔日期迄
	 */
	private String queryEndDate;
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
	 * 電文紀錄DTO
	 */
	private ApiLogDTO apiLogDTO;
	/**
	 * Constructor: 無參數建構子
	 */
	public ApiLogFormDTO() {
		super();
	}
	
	/**
	 * @return the queryClientCode
	 */
	public String getQueryClientCode() {
		return queryClientCode;
	}
	/**
	 * @param queryClientCode the queryClientCode to set
	 */
	public void setQueryClientCode(String queryClientCode) {
		this.queryClientCode = queryClientCode;
	}
	/**
	 * @return the queryStartDate
	 */
	public String getQueryStartDate() {
		return queryStartDate;
	}
	/**
	 * @param queryStartDate the queryStartDate to set
	 */
	public void setQueryStartDate(String queryStartDate) {
		this.queryStartDate = queryStartDate;
	}
	/**
	 * @return the queryEndDate
	 */
	public String getQueryEndDate() {
		return queryEndDate;
	}
	/**
	 * @param queryEndDate the queryEndDate to set
	 */
	public void setQueryEndDate(String queryEndDate) {
		this.queryEndDate = queryEndDate;
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
	 * @return the apiLogDTO
	 */
	public ApiLogDTO getApiLogDTO() {
		return apiLogDTO;
	}
	/**
	 * @param apiLogDTO the apiLogDTO to set
	 */
	public void setApiLogDTO(ApiLogDTO apiLogDTO) {
		this.apiLogDTO = apiLogDTO;
	}
}
