package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PvmDtidDefDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;
/**
 * Purpose: DTID號碼管理FORMDTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/1/24
 * @MaintenancePersonnel CarrieDuan
 */
public class DtidDefFormDTO extends AbstractSimpleListFormDTO{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -7042379273424905774L;
	
	/**
	 * 查詢條件-客戶
	 */
	public static final String QUERY_CUSTOMERS_ID						= "queryCustomer";
	/**
	 * 查詢條件-設備名稱
	 */
	public static final String QUERY_ASSET_NAME							= "queryAssetTypeId";
	/**
	 * 匯出模板名稱
	 */
	public static final String REPORT_JRXML_NAME						= "PVM_DTID_DEF";
	/**
	 * 匯出模板名稱
	 */
	public static final String REPORT_FILE_NAME							= "DTID號碼管理";
	
	/**
	 * 查詢條件 - DTID起
	 */
	public static final String QUERY_DTID_START							= "queryDtidStart";
	/**
	 * 查詢條件 - DTID止
	 */
	public static final String QUERY_DTID_END							= "queryDtidEnd";
	
	
	
	/**
	 * 客戶 - 查詢使用
	 */
	private String queryCustomer;
	
	/**
	 * 設備名稱 - 查詢使用
	 */
	private String queryAssetTypeId;
	
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
	 * DTID DTO集合
	 */
	private List<PvmDtidDefDTO> dtidDefDTOs;
	
	/**
	 * DTID
	 */
	private PvmDtidDefDTO dtidDefDTO;
	/**
	 * DTID - 查詢使用起
	 */
	private String queryDtidStart;
	/**
	 * DTID - 查詢時間止
	 */
	private String queryDtidEnd;
	
	/**
	 * id
	 */
	private String id;
	/**
	 * dtidId
	 */
	private String dtidId;

	
	/**
	 * Constructor:
	 */
	public DtidDefFormDTO() {
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
	 * @return the queryAssetTypeId
	 */
	public String getQueryAssetTypeId() {
		return queryAssetTypeId;
	}
	

	/**
	 * @param queryAssetTypeId the queryAssetTypeId to set
	 */
	public void setQueryAssetTypeId(String queryAssetTypeId) {
		this.queryAssetTypeId = queryAssetTypeId;
	}

	/**
	 * @return the queryDtidStart
	 */
	public String getQueryDtidStart() {
		return queryDtidStart;
	}

	/**
	 * @param queryDtidStart the queryDtidStart to set
	 */
	public void setQueryDtidStart(String queryDtidStart) {
		this.queryDtidStart = queryDtidStart;
	}

	/**
	 * @return the queryDtidEnd
	 */
	public String getQueryDtidEnd() {
		return queryDtidEnd;
	}

	/**
	 * @param queryDtidEnd the queryDtidEnd to set
	 */
	public void setQueryDtidEnd(String queryDtidEnd) {
		this.queryDtidEnd = queryDtidEnd;
	}

	/**
	 * @return the dtidDefDTOs
	 */
	public List<PvmDtidDefDTO> getDtidDefDTOs() {
		return dtidDefDTOs;
	}

	/**
	 * @param dtidDefDTOs the dtidDefDTOs to set
	 */
	public void setDtidDefDTOs(List<PvmDtidDefDTO> dtidDefDTOs) {
		this.dtidDefDTOs = dtidDefDTOs;
	}

	/**
	 * @return the dtidDefDTO
	 */
	public PvmDtidDefDTO getDtidDefDTO() {
		return dtidDefDTO;
	}

	/**
	 * @param dtidDefDTO the dtidDefDTO to set
	 */
	public void setDtidDefDTO(PvmDtidDefDTO dtidDefDTO) {
		this.dtidDefDTO = dtidDefDTO;
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
	 * @return the dtidId
	 */
	public String getDtidId() {
		return dtidId;
	}

	/**
	 * @param dtidId the dtidId to set
	 */
	public void setDtidId(String dtidId) {
		this.dtidId = dtidId;
	}
	
	
}
