package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTransInfoHitoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;
/**
 * Purpose: 歷史轉倉查詢formDTO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-7-18
 * @MaintenancePersonnel echomou
 */
public class AssetTransInfoHistoryFormDTO extends AbstractSimpleListFormDTO<DmmAssetTransListDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6918825871455162373L;
	/**
	 * 頁碼id
	 */
	public static final String PARAM_PAGE_SORT = "ASSET_TRANS_ID";
	/**
	 * 轉倉id
	 */
	public static final String QUERY_ASSET_TRANS_ID					= "queryAssetTransId";
	/**
	 * 轉出日期起
	 */
	public static final String QUERY_ASSET_FROM_DATE_START			= "queryFromDateStart";
	/**
	 * 轉出日期迄
	 */
	public static final String QUERY_ASSET_FROM_DATE_END		    = "queryFromDateEnd";
	/**
	 * 轉入日期起
	 */
	public static final String QUERY_ASSET_TO_DATE_START			= "queryToDateStart";
	/**
	 * 轉入日期迄
	 */
	public static final String QUERY_ASSET_TO_DATE_END			    = "queryToDateEnd";
	/**
	 * 轉出倉
	 */
	public static final String QUERY_ASSET_FROM_WARE				= "queryFromWarehouseId";
	/**
	 * 轉入倉
	 */
	public static final String QUERY_ASSET_TO_WARE					= "queryToWarehouseId";
	/**
	 * report名字
	 */
	public static final String REPORT_FILE_NAME					    = "設備轉倉匯出";
	/**
	 * 匯出報表名字
	 */
	public static final String REPORT_SHEET_NAME					= "設備轉倉匯出範本";
	/**
	 * 出貨單 名字
	 */
	public static final String REPORT_LIST_FILE_NAME				= "iATOMS系統設備轉倉出貨單";
	/**
	 * ireport文件名
	 */
	public static final String REPORT_JRXML_NAME			     	= "ASSET_TRANS_HISTORY";
	/**
	 * ireport文件名
	 */
	public static final String REPORT_JRXML_GOOD_NAME			    = "ASSET_TRANS_GOOD_LIST";
	/**
	 * ireport文件名
	 */
	public static final String REPORT_JRXML_GOOD_NAME_SUB		    = "ASSET_TRANS_SUB_LIST";
	/**
	 * ireport文件名
	 */
	public static final String SUBREPORT_DIR						= "SUBREPORT_DIR";
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
	 * 轉入日期起
	 */
	private String queryFromDateStart;
	/**
	 * 轉入日期訖
	 */
	private String queryFromDateEnd;
	
	/**
	 * 轉出日期起
	 */
	private String queryToDateStart;
	/**
	 * 轉出日期訖
	 */
	private String queryToDateEnd;
	/**
	 * 轉倉批號
	 */
	private String queryAssetTransId;
	/**
	 * 轉出倉
	 */
	private int queryFromWarehouseId;
	/**
	 * 轉入倉
	 */
	private int queryToWarehouseId;
	/**
	 * 設備轉倉單主檔
	 */
	private DmmAssetTransInfoDTO assetTransInfoDTO;
	/**
	 * 
	 */
	private List<AssetInInfoDTO> assetInInfoDTOs;
	/**
	 * 
	 */
	private AssetTransInfoHitoryDTO assetTransInfoHitoryDTO;
	/**
	 * Constructor:無參構造函數
	 */
	public AssetTransInfoHistoryFormDTO() {
		super();
	}
	
	/**
	 * Constructor:有參構造函數
	 */
	public AssetTransInfoHistoryFormDTO(Integer rows, Integer page,
			String sort, String order, Integer totalSize,
			String queryFromDateStart, String queryFromDateEnd,
			String queryToDateStart, String queryToDateEnd,
			String queryAssetTransId, int queryFromWarehouseId,
			int queryToWarehouseId) {
		super();
		this.rows = rows;
		this.page = page;
		this.sort = sort;
		this.order = order;
		this.totalSize = totalSize;
		this.queryFromDateStart = queryFromDateStart;
		this.queryFromDateEnd = queryFromDateEnd;
		this.queryToDateStart = queryToDateStart;
		this.queryToDateEnd = queryToDateEnd;
		this.queryAssetTransId = queryAssetTransId;
		this.queryFromWarehouseId = queryFromWarehouseId;
		this.queryToWarehouseId = queryToWarehouseId;
	}


	public int getQueryFromWarehouseId() {
		return queryFromWarehouseId;
	}
	
	public int getQueryToWarehouseId() {
		return queryToWarehouseId;
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
	 * @param rows the rows to set
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
	/**
	 * @return the rows
	 */
	public Integer getRows() {
		return rows;
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
	 * @return the sort	public Date getQueryFromDate() {
		return queryFromDate;
	}

	public void setQueryFromDate(Date queryFromDate) {
		this.queryFromDate = queryFromDate;
	}

	public Date getQueryToDate() {
		return queryToDate;
	}

	public void setQueryToDate(Date queryToDate) {
		this.queryToDate = queryToDate;
	}
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
	 * @return the assetTransInfoDTO
	 */
	public DmmAssetTransInfoDTO getAssetTransInfoDTO() {
		return assetTransInfoDTO;
	}
	
	/**
	 * @param assetTransInfoDTO the assetTransInfoDTO to set
	 */
	public void setAssetTransInfoDTO(DmmAssetTransInfoDTO assetTransInfoDTO) {
		this.assetTransInfoDTO = assetTransInfoDTO;
	}

	/**
	 * @return the assetInInfoDTOs
	 */
	public List<AssetInInfoDTO> getAssetInInfoDTOs() {
		return assetInInfoDTOs;
	}

	/**
	 * @param assetInInfoDTOs the assetInInfoDTOs to set
	 */
	public void setAssetInInfoDTOs(List<AssetInInfoDTO> assetInInfoDTOs) {
		this.assetInInfoDTOs = assetInInfoDTOs;
	}

	

	/**
	 * @return the assetTransInfoHitoryDTO
	 */
	public AssetTransInfoHitoryDTO getAssetTransInfoHitoryDTO() {
		return assetTransInfoHitoryDTO;
	}

	/**
	 * @param assetTransInfoHitoryDTO the assetTransInfoHitoryDTO to set
	 */
	public void setAssetTransInfoHitoryDTO(
			AssetTransInfoHitoryDTO assetTransInfoHitoryDTO) {
		this.assetTransInfoHitoryDTO = assetTransInfoHitoryDTO;
	}

	/**
	 * @return the queryFromDateStart
	 */
	public String getQueryFromDateStart() {
		return queryFromDateStart;
	}

	/**
	 * @param queryFromDateStart the queryFromDateStart to set
	 */
	public void setQueryFromDateStart(String queryFromDateStart) {
		this.queryFromDateStart = queryFromDateStart;
	}

	/**
	 * @return the queryFromDateEnd
	 */
	public String getQueryFromDateEnd() {
		return queryFromDateEnd;
	}

	/**
	 * @param queryFromDateEnd the queryFromDateEnd to set
	 */
	public void setQueryFromDateEnd(String queryFromDateEnd) {
		this.queryFromDateEnd = queryFromDateEnd;
	}

	/**
	 * @return the queryToDateStart
	 */
	public String getQueryToDateStart() {
		return queryToDateStart;
	}

	/**
	 * @param queryToDateStart the queryToDateStart to set
	 */
	public void setQueryToDateStart(String queryToDateStart) {
		this.queryToDateStart = queryToDateStart;
	}

	/**
	 * @return the queryToDateEnd
	 */
	public String getQueryToDateEnd() {
		return queryToDateEnd;
	}

	/**
	 * @param queryToDateEnd the queryToDateEnd to set
	 */
	public void setQueryToDateEnd(String queryToDateEnd) {
		this.queryToDateEnd = queryToDateEnd;
	}

	/**
	 * @param queryFromWarehouseId the queryFromWarehouseId to set
	 */
	public void setQueryFromWarehouseId(int queryFromWarehouseId) {
		this.queryFromWarehouseId = queryFromWarehouseId;
	}

	/**
	 * @param queryToWarehouseId the queryToWarehouseId to set
	 */
	public void setQueryToWarehouseId(int queryToWarehouseId) {
		this.queryToWarehouseId = queryToWarehouseId;
	}
}
