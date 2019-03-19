package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;
/**
 * Purpose: 設備轉倉單主檔FormDTO
 * @author Amanda Wang 
 * @since  JDK 1.6
 * @date   2016/7/13
 * @MaintenancePersonnel Amanda Wang
 */
public class AssetTransInfoFormDTO extends AbstractSimpleListFormDTO<DmmAssetTransListDTO> {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID 						= -6918825871455162372L;
	
	/**
	 * 分組列名
	 */
	public static final String PARAM_PAGE_SORT 						= "ASSET_TRANS_ID";
	/**
	 * 匯出文件名
	 */
	public static final String UPLOAD_FILE_NAME						= "設備轉倉範本.xls";
	/**
	 * 下載
	 */
	public static final String UPLOAD_FILE							= "/com/cybersoft4u/xian/iatoms/services/download/Importtemplate/";
	/**
	 * 模板路径
	 */
	public static final String TEMP_ERROR_FILE_PATH					= "tempFilePath";
	/**
	 * 錯誤文件名
	 */
	public static final String ERROR_FILE_NAME						= "errorFileName";
	/**
	 * 查詢條件--轉倉批號
	 */
	public static final String QUERY_ASSET_TRANS_ID					= "queryAssetTransId";
	/**
	 * 查詢條件 -- 設備序號
	 */
	public static final String QUERY_SERIAL_NUMBER					= "serialNumber";
	/**
	 * 匯出使用的jrxml文件
	 */
	public static final String REPORT_JRXML_NAME					= "ASSET_TRANS_LIST";
	/**
	 * 匯出使用的jrxml文件
	 */
	public static final String REPORT_ASSET_JRXML_NAME				= "ASSET_TRANS_GOOD_LIST";
	/**
	 * 匯出后文件名
	 */
	public static final String REPORT_FILE_NAME 					= "設備轉倉匯出範本";
	/**
	 * 匯出后文件名
	 */
	public static final String REPORT_GOOD_OUT_NAME 				= "出庫單範本";
	/**
	 * 匯出后文件名
	 */
	public static final String ERROR_TEMP_FILE_NAME					= "匯入錯誤檔.txt";
	/**
	 * 匯出使用的jrxml文件
	 */
	public static final String REPORT_JRXML_GOOD_NAME_SUB		    = "ASSET_TRANS_SUB_LIST";
	/**
	 * SUBREPORT_DIR
	 */
	public static final String SUBREPORT_DIR						= "SUBREPORT_DIR";
	/**
	 * 轉出
	 */
	public static final String TAB_ASSET_TRANFER_OUT				= "assetTranferOut";
	/**
	 * 轉入驗收
	 */
	public static final String TAB_ASSET_TRANFER_IN_CHECK			= "assetTranferinCheck";
	/**
	 * 歷史轉倉查詢
	 */
	public static final String TAB_ASSET_TRANFER_HIS				= "assetTranferHis";
	/**
	 * 類型
	 */
	public static final String TAB_TYPE								= "tabType";
	/**
	 * 檢查轉入的設備信息
	 */
	public static final String PARAM_LIST_CHECK_TRANST_INFO			= "checkTransInfos";
	
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
	 * 轉倉批號
	 */
	private String assetTransId;
	
	/**
	 * 用來區分確認轉倉與取消轉倉
	 */
	private String confirmTrans;
	
	/**
	 * 是否已確認轉倉
	 */
	private Boolean isListDone;
	
	/**
	 * 設備序號
	 */
	private String serialNumber;
	
	/**
	 * 轉倉說明
	 */
	private String comment;
	
	/**
	 * 設備轉倉單主檔
	 */
	private DmmAssetTransInfoDTO assetTransInfoDTO;
	
	/**
	 * 上传文档
	 */
	private MultipartFile uploadFiled;
	
	/**
	 * 設備轉倉明細DTO
	 */
	private DmmAssetTransListDTO assetTransListDTO;
	
	/**
	 * 設備轉倉明細DTO集合
	 */
	private List<DmmAssetTransListDTO> assetTransListDTOs;
	
	/**
	 * 錯誤代碼文件名
	 */
	private String errorFileName;
	
	/**
	 * 錯誤文件路徑
	 */
	private  String tempFilePath;
	
	/**
	 * 轉倉確認通知人員
	 */
	private String toMailId;
	
	/**
	 * 需要儲存的轉倉說明信息JSON格式
	 */
	private String assetTransListRow;
	
	/**
	 * 轉入驗收類型
	 */
	private String option;
	
	/**
	 * 退回原因
	 */
	private String refuseReason;
	
	/**
	 * 查存參數-轉倉批號
	 */
	private String queryAssetTransId;
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
	 * 轉出倉
	 */
	private int queryFromWarehouseId;
	/**
	 * 轉入倉
	 */
	private int queryToWarehouseId;
	/**
	 * 是否查詢歷史
	 */
	private Boolean isHistory;
	/**
	 * 上传文件最大值
	 */
	private String uploadFileSize;
	/**
	 * 是否顯示轉倉狀態 --CR #2703 轉倉作業轉入驗收調整 2017/11/08
	 */
	private Boolean showStatusFlag;
	/**
	 * 確認入庫時勾選的設備序號 --CR #2703 轉倉作業轉入驗收調整 2017/11/08
	 */
	private String serialNumbers;
	/**
	 * 轉倉驗收是否批量處理
	 */
	private String isSelectedAll;
	/**
	 * Constructor:無參構造函數
	 */
	public AssetTransInfoFormDTO() {
		super();
	}
	
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
	 * @return the assetTransId
	 */
	public String getAssetTransId() {
		return assetTransId;
	}

	/**
	 * @param assetTransId the assetTransId to set
	 */
	public void setAssetTransId(String assetTransId) {
		this.assetTransId = assetTransId;
	}

	/**
	 * @return the isListDone
	 */
	public Boolean getIsListDone() {
		return isListDone;
	}
	
	/**
	 * @param isListDone the isListDone to set
	 */
	public void setIsListDone(Boolean isListDone) {
		this.isListDone = isListDone;
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
	 * @return the assetInInfoDTO
	 */
	public DmmAssetTransListDTO getAssetTransListDTO() {
		return assetTransListDTO;
	}
	
	/**
	 * @param assetInInfoDTO the assetInInfoDTO to set
	 */
	public void setAssetInInfoDTO(DmmAssetTransListDTO assetTransListDTO) {
		this.assetTransListDTO = assetTransListDTO;
	}
	
	/**
	 * @return the assetTransListDTOs
	 */
	public List<DmmAssetTransListDTO> getAssetTransListDTOs() {
		return assetTransListDTOs;
	}

	/**
	 * @param assetTransListDTOs the assetTransListDTOs to set
	 */
	public void setAssetTransListDTOs(List<DmmAssetTransListDTO> assetTransListDTOs) {
		this.assetTransListDTOs = assetTransListDTOs;
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @param assetTransListDTO the assetTransListDTO to set
	 */
	public void setAssetTransListDTO(DmmAssetTransListDTO assetTransListDTO) {
		this.assetTransListDTO = assetTransListDTO;
	}

	/**
	 * @return the assetTransListRow
	 */
	public String getAssetTransListRow() {
		return assetTransListRow;
	}

	/**
	 * @param assetTransListRow the assetTransListRow to set
	 */
	public void setAssetTransListRow(String assetTransListRow) {
		this.assetTransListRow = assetTransListRow;
	}

	/**
	 * @return the confirmTrans
	 */
	public String getConfirmTrans() {
		return confirmTrans;
	}

	/**
	 * @param confirmTrans the confirmTrans to set
	 */
	public void setConfirmTrans(String confirmTrans) {
		this.confirmTrans = confirmTrans;
	}

	/**
	 * @return the toMailId
	 */
	public String getToMailId() {
		return toMailId;
	}

	/**
	 * @param toMailId the toMailId to set
	 */
	public void setToMailId(String toMailId) {
		this.toMailId = toMailId;
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
	 * @return the queryFromWarehouseId
	 */
	public int getQueryFromWarehouseId() {
		return queryFromWarehouseId;
	}

	/**
	 * @param queryFromWarehouseId the queryFromWarehouseId to set
	 */
	public void setQueryFromWarehouseId(int queryFromWarehouseId) {
		this.queryFromWarehouseId = queryFromWarehouseId;
	}

	/**
	 * @return the queryToWarehouseId
	 */
	public int getQueryToWarehouseId() {
		return queryToWarehouseId;
	}

	/**
	 * @param queryToWarehouseId the queryToWarehouseId to set
	 */
	public void setQueryToWarehouseId(int queryToWarehouseId) {
		this.queryToWarehouseId = queryToWarehouseId;
	}

	/**
	 * @return the isHistory
	 */
	public Boolean getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory the isHistory to set
	 */
	public void setIsHistory(Boolean isHistory) {
		this.isHistory = isHistory;
	}

	public String getUploadFileSize() {
		return uploadFileSize;
	}

	public void setUploadFileSize(String uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}

	/**
	 * @return the showStatusFlag
	 */
	public Boolean getShowStatusFlag() {
		return showStatusFlag;
	}

	/**
	 * @param showStatusFlag the showStatusFlag to set
	 */
	public void setShowStatusFlag(Boolean showStatusFlag) {
		this.showStatusFlag = showStatusFlag;
	}

	/**
	 * @return the serialNumbers
	 */
	public String getSerialNumbers() {
		return serialNumbers;
	}

	/**
	 * @param serialNumbers the serialNumbers to set
	 */
	public void setSerialNumbers(String serialNumbers) {
		this.serialNumbers = serialNumbers;
	}

	public String getIsSelectedAll() {
		return isSelectedAll;
	}

	public void setIsSelectedAll(String isSelectedAll) {
		this.isSelectedAll = isSelectedAll;
	}
	
	
}
