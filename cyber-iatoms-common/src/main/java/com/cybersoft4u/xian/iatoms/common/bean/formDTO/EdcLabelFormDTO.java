package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import org.springframework.web.multipart.MultipartFile;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmEdcLabelDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

/**
 * Purpose:刷卡機標籤管理formDTO
 * @author NickLin 
 * @since  JDK 1.7
 * @date   2018/06/27
 * @MaintenancePersonnel CyberSoft
 */
public class EdcLabelFormDTO extends AbstractSimpleListFormDTO<DmmEdcLabelDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -7177366234125672604L;
	/**
	 * 查詢條件:特店代號
	 */
	public static final String QUERY_MERCHANT_CODE = "queryMerchantCode";
	/**
	 * 查詢條件:DTID
	 */
	public static final String QUERY_DTID = "queryDtid";
	/**
	 * 查詢條件:匯入日期起
	 */
	public static final String QUERY_START_DATE = "queryStartDate";
	/**
	 * 查詢條件:匯入日期迄
	 */
	public static final String QUERY_END_DATE = "queryEndDate";
	/**
	 * 排序方式
	 */
	public static final String PARAM_PAGE_ORDER = "desc";
	/**
	 * 排序欄位
	 */
	public static final String PARAM_PAGE_SORT = "seqNo, merchantCode";
	/**
	 * 刷卡機標籤匯入格式下載模板中文名稱
	 */
	public static final String EDC_LABEL_TEMPLATE_NAME_FOR_CN = "刷卡機標籤匯入檔.xls";
	/**
	 * 刷卡機標籤匯入格式下載模板英文名稱
	 */
	public static final String EDC_LABEL_TEMPLATE_NAME_FOR_EN = "edcLabelTemplate.xls";
	/**
	 * 錯誤訊息檔案名稱
	 */
	public static final String ERROR_FILE_NAME = "errorFileName";
	/**
	 * 匯入格式下載訊息路徑
	 */
	public static final String TEMP_FILE_PATH = "tempFilePath";
	/**
	 * 刷卡機標籤列印檔模板
	 */
	public static final String PROJECT_REPORT_JRXML_NAME = "DMM_EDC_LABEL";
	/**
	 * 刷卡機標籤列印檔檔名
	 */
	public static final String PROJECT_REPORT_FILE_NAME = "刷卡機標籤列印檔";
	/**
	 * 刷卡機標籤列印檔Doc模板
	 */
	public static final String EDC_LABEL_DOC_TEMPLATE_NAME = "edc_label_template";
	/**
	 * 查詢條件:特店代號
	 */
	private String queryMerchantCode;
	/**
	 * 查詢條件:DTID
	 */
	private String queryDtid;
	/**
	 * 查詢條件:匯入日期起
	 */
	private String queryStartDate;
	/**
	 * 查詢條件:匯入日期迄
	 */
	private String queryEndDate;
	/**
	 * 複合主鍵
	 */
	private String compositeKeys;
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
	 * 上傳檔案
	 */
	private MultipartFile uploadFiled;
	/**
	 * 檔案上傳最大值
	 */
	private String uploadFileSize;
	/**
	 * 錯誤文件名
	 */
	private String errorFileName;
	/**
	 * 錯誤文件路徑
	 */
	private String tempFilePath;
	/**
	 * 刷卡機標籤管理DTO
	 */
	private DmmEdcLabelDTO edcLabelDTO;
	
	/**
	 * Constructor: 無參數建構子
	 */
	public EdcLabelFormDTO() {
		super();
	}

	/**
	 * @return the queryMerchantCode
	 */
	public String getQueryMerchantCode() {
		return queryMerchantCode;
	}

	/**
	 * @param queryMerchantCode the queryMerchantCode to set
	 */
	public void setQueryMerchantCode(String queryMerchantCode) {
		this.queryMerchantCode = queryMerchantCode;
	}

	/**
	 * @return the queryDtid
	 */
	public String getQueryDtid() {
		return queryDtid;
	}

	/**
	 * @param queryDtid the queryDtid to set
	 */
	public void setQueryDtid(String queryDtid) {
		this.queryDtid = queryDtid;
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
	 * @return the compositeKeys
	 */
	public String getCompositeKeys() {
		return compositeKeys;
	}

	/**
	 * @param compositeKeys the compositeKeys to set
	 */
	public void setCompositeKeys(String compositeKeys) {
		this.compositeKeys = compositeKeys;
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
	 * @return the uploadFiled
	 */
	public MultipartFile getUploadFiled() {
		return uploadFiled;
	}

	/**
	 * @param uploadFiled the uploadFiled to set
	 */
	public void setUploadFiled(MultipartFile uploadFiled) {
		this.uploadFiled = uploadFiled;
	}

	/**
	 * @return the uploadFileSize
	 */
	public String getUploadFileSize() {
		return uploadFileSize;
	}

	/**
	 * @param uploadFileSize the uploadFileSize to set
	 */
	public void setUploadFileSize(String uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}

	/**
	 * @return the errorFileName
	 */
	public String getErrorFileName() {
		return errorFileName;
	}

	/**
	 * @param errorFileName the errorFileName to set
	 */
	public void setErrorFileName(String errorFileName) {
		this.errorFileName = errorFileName;
	}

	/**
	 * @return the tempFilePath
	 */
	public String getTempFilePath() {
		return tempFilePath;
	}

	/**
	 * @param tempFilePath the tempFilePath to set
	 */
	public void setTempFilePath(String tempFilePath) {
		this.tempFilePath = tempFilePath;
	}

	/**
	 * @return the edcLabelDTO
	 */
	public DmmEdcLabelDTO getEdcLabelDTO() {
		return edcLabelDTO;
	}

	/**
	 * @param edcLabelDTO the edcLabelDTO to set
	 */
	public void setEdcLabelDTO(DmmEdcLabelDTO edcLabelDTO) {
		this.edcLabelDTO = edcLabelDTO;
	}
}
