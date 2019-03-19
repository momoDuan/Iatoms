package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;
/**
 * Purpose: 設備入庫FORMDTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/1/24
 * @MaintenancePersonnel CarrieDuan
 */
public class AssetInInfoFormDTO extends AbstractSimpleListFormDTO{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -7042379273424905774L;
	/**
	 * 
	 */
	public static final String USE_CASE_NO							= "DMM03040";
	/**
	 * 查詢條件-入庫批號
	 */
	public static final String QUERY_ASSET_IN_ID 					= "queryAssetInId";
	/**
	 * 查詢條件-合約ID
	 */
	public static final String QUERY_CONTRACT_ID 					= "queryContractId";
	/**
	 * 查詢條件-公司ID
	 */
	public static final String QUERY_COMPANY_ID						= "queryCompanyId";
	/**
	 * 
	 */
	public static final String SELECT_ADD		 					= "請選擇";
	/**
	 * 
	 */
	public static final String SELECT_ADD_ASSET 					= "新增批號";
	/**
	 * 
	 */
	public static final String ASSET_OWNER							= "經貿聯網";
	/**
	 * 匯入格式範本中文名稱
	 */
	public static final String UPLOAD_FILE_NAME_CH					= "設備入庫範本.xls";
	/**
	 * 匯入格式範本英文名稱
	 */
	public static final String UPLOAD_FILE_NAME_EN					= "assetInImportTemplate.xls";
	/**
	 * 臨時文件路勁
	 */
	public static final String TEMP_ERROR_FILE_PATH					= "tempErrorFile";
	/**
	 * 錯誤文件路徑
	 */
	public static final String ERROR_FILE_PATH						= "errorFilePath";
	/**
	 * 文件名稱
	 */
	public static final String ERROR_FILE_NAME						= "fileName";
	/**
	 * 生成批號使用
	 */
	public static final String PARAM_IN								= "IN";
	/**
	 * sort
	 */
	public static final String PARAM_SORT							= "assetInId, serialNumber";
	/**
	 * order
	 */
	public static final String PARAM_ORDER							= "asc";
	
	/**
	 * order
	 */
	public static final String PARAM_ASSETS_OWNER_AND_USE_EMPLOYEE_NAME_LIST			= "ownerAndUseEmployeeNames";
	
	/**	
	 * 查詢條件--入庫批號
	 */
	private String queryAssetInId;
	/**
	 * 查詢條件--合約編號
	 */
	private String queryContractId;
	/**
	 * 查詢條件--客戶
	 */
	private String queryCompanyId;
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
	 * 
	 */
	String qquuid;
	/**
	 * 新的文件名稱
	 */
	private String newFileName;
	/**
	 * 刪除設備入庫明細檔id
	 */
	private String deleteAssetListIds;
	/**
	 * 入庫明細ID
	 */
	private List<String> assetListIds;
	/**
	 * 驗收id
	 */
	private String updateId;
	/**
	 * 上传文档
	 */
	private MultipartFile uploadFiled;
	/**
	 * 是否入庫
	 */
	private boolean isDone =true;
	/**
	 * 設備入庫主檔DTO
	 */
	private AssetInInfoDTO assetInInfoDTO;
	/**
	 * 設備入庫主檔DTO集合
	 */
	private List<AssetInInfoDTO> assetInInfoDTOs;
	/**
	 * 錯誤文件名稱
	 */
	private String errorFileName;
	/**
	 * 歷史入庫資料總條數
	 */
	private Integer deviceStockCount;
	/**
	 * 上传文件最大值
	 */
	private String uploadFileSize;
	
	/**
	 * @return the queryAssetInId
	 */
	public String getQueryAssetInId() {
		return queryAssetInId;
	}
	/**
	 * @param queryAssetInId the queryAssetInId to set
	 */
	public void setQueryAssetInId(String queryAssetInId) {
		this.queryAssetInId = queryAssetInId;
	}
	/**
	 * @return the queryContractId
	 */
	public String getQueryContractId() {
		return queryContractId;
	}
	/**
	 * @param queryContractId the queryContractId to set
	 */
	public void setQueryContractId(String queryContractId) {
		this.queryContractId = queryContractId;
	}
	/**
	 * @return the deviceStockCount
	 */
	public Integer getDeviceStockCount() {
		return deviceStockCount;
	}
	/**
	 * @param deviceStockCount the deviceStockCount to set
	 */
	public void setDeviceStockCount(Integer deviceStockCount) {
		this.deviceStockCount = deviceStockCount;
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
	 * @return the qquuid
	 */
	public String getQquuid() {
		return qquuid;
	}
	/**
	 * @param qquuid the qquuid to set
	 */
	public void setQquuid(String qquuid) {
		this.qquuid = qquuid;
	}
	/**
	 * @return the newFileName
	 */
	public String getNewFileName() {
		return newFileName;
	}
	/**
	 * @param newFileName the newFileName to set
	 */
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
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
	public AssetInInfoDTO getAssetInInfoDTO() {
		return assetInInfoDTO;
	}
	/**
	 * @param assetInInfoDTO the assetInInfoDTO to set
	 */
	public void setAssetInInfoDTO(AssetInInfoDTO assetInInfoDTO) {
		this.assetInInfoDTO = assetInInfoDTO;
	}
	/**
	 * @return the isDone
	 */
	public boolean isDone() {
		return isDone;
	}
	/**
	 * @param isDone the isDone to set
	 */
	public void setDone(boolean isDone) {
		this.isDone = isDone;
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
	 * @return the deleteAssetListIds
	 */
	public String getDeleteAssetListIds() {
		return deleteAssetListIds;
	}
	/**
	 * @param deleteAssetListIds the deleteAssetListIds to set
	 */
	public void setDeleteAssetListIds(String deleteAssetListIds) {
		this.deleteAssetListIds = deleteAssetListIds;
	}
	/**
	 * @return the updateId
	 */
	public String getUpdateId() {
		return updateId;
	}
	/**
	 * @param updateId the updateId to set
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
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
	 * @return the queryCompanyId
	 */
	public String getQueryCompanyId() {
		return queryCompanyId;
	}
	/**
	 * @param queryCompanyId the queryCompanyId to set
	 */
	public void setQueryCompanyId(String queryCompanyId) {
		this.queryCompanyId = queryCompanyId;
	}
	/**
	 * @return the assetListIds
	 */
	public List<String> getAssetListIds() {
		return assetListIds;
	}
	/**
	 * @param assetListIds the assetListIds to set
	 */
	public void setAssetListIds(List<String> assetListIds) {
		this.assetListIds = assetListIds;
	}
	public String getUploadFileSize() {
		return uploadFileSize;
	}
	public void setUploadFileSize(String uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}
	
	
}
