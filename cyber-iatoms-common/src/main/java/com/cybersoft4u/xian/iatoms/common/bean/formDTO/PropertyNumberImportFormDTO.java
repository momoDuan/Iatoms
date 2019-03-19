package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;

/**
 * Purpose: 財產批次匯入FormDTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016-7-7
 * @MaintenancePersonnel CarrieDuan
 */
public class PropertyNumberImportFormDTO extends AbstractSimpleListFormDTO<DmmRepositoryDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4075453212406722300L;

	/**
	 * 无参构造子
	 */
	public PropertyNumberImportFormDTO() {
	}
	/**
	 * 下載範文文件中文名稱
	 */
	public static final String UPLOAD_CN_FILE_NAME					= "設備資料批次異動.xls";
	/**
	 * 下載範文文件英文名稱
	 */
	public static final String UPLOAD_EN_FILE_NAME					= "equipmentInfoImportTemplate.xls";
	/**
	 * 下載範本文件路徑
	 */
	public static final String UPLOAD_FILE							= "/com/cybersoft4u/xian/iatoms/services/download/Importtemplate";
	/**
	 * 文件上傳筆數最大限制
	 */
	public static final String UPLOAD_NUMBER						= "500";
	/**
	 * 臨時文件名稱
	 */
	public static final String TEMP_ERROR_FILE_PATH					= "tempErrorFile";
	/**
	 * 錯誤文件名稱
	 */
	public static final String ERROR_FILE_NAME						= "errorFileName";
	/**
	 * 錯誤文件路徑
	 */
	public static final String ERROR_FILE_PATH						= "errorFilePath";
	/**
	 * 提示信息
	 */
	public static final String NOT_ASSET_INFO						= "設備序號不存在";
	/**
	 * 提示信息
	 */
	public static final String SERIAL_NUMBER_NOTEQ_ASSET_NAME		= "設備序號與設備名稱不符，不可異動財產編號";
	/**
	 * 提示信息
	 */
	public static final String SUCCESS_EXAMINE						= "檢核成功";
	/**
	 * 提示信息
	 */
	public static final String ASSET_NAME							= "設備名稱";
	/**
	 * 提示信息
	 */
	public static final String SERIAL_NUMBER						= "設備序號";
	/**
	 * 提示信息
	 */
	public static final String PROPERTY_ID							= "財產編號";
	/**
	 * 提示信息
	 */
	public static final String EFFECTIVE_DATA						= "資料正確";
	/**
	 * 提示信息
	 */
	public static final String INVALID_DATA							= "資料有誤,請參考下方資訊";
	/**
	 * 需要匯入修改的列
	 */
	public static final String UPDATE_COLUMN_LIST					="updateColumns";
	/**
	 * 設備月檔可修改的月份
	 */
	public static final String UPDATE_MONTH_YEAR_LISY				= "monthYears";
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
	 * 上传文档
	 */
	private MultipartFile uploadFiled;
	/**
	 * 
	 */
	private boolean isDone =true;
	/**
	 * 設備入庫主檔DTO
	 */
	private AssetInInfoDTO assetInInfoDTO;
	/**
	 * 庫存DTO
	 */
	private List<DmmRepositoryDTO> repositoryDTOs;
	/**
	 * 庫存DTO
	 */
	private List<DmmRepositoryDTO> repositoryDTOTaiXins;
	/**
	 * 庫存DTO
	 */
	private List<DmmRepositoryDTO> repositoryDTOJdws;
	/**
	 * 錯誤文件名稱
	 */
	private String errorFileName;
	/**
	 * 需匯入的財產編號集合
	 */
	private String assetListRow;
	/**
	 * 需要匯入的臺新租賃維護數據
	 */
	private String assetListTaiXinRow;
	/**
	 * 需要匯入的捷達威維護數據
	 */
	private String assetListJdwRow;
	/**
	 * 設備Map集合
	 */
	private Map<String, String> assetMap;
	
	/**
	 * 異動欄位
	 */
	private List<String> updateColumns;
	/**
	 * 上传文件最大值
	 */
	private String uploadFileSize;
	/**
	 * 保存数据的临时文件名称
	 */
	private String fileName;
	
	/**
	 * 需修改的表
	 */
	private String updateTable;
	/**
	 * 需修改的月份
	 */
	private String monthYear;
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
	 * @return the repositoryDTOs
	 */
	public List<DmmRepositoryDTO> getRepositoryDTOs() {
		return repositoryDTOs;
	}
	/**
	 * @param repositoryDTOs the repositoryDTOs to set
	 */
	public void setRepositoryDTOs(List<DmmRepositoryDTO> repositoryDTOs) {
		this.repositoryDTOs = repositoryDTOs;
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
	 * @return the assetListRow
	 */
	public String getAssetListRow() {
		return assetListRow;
	}
	/**
	 * @param assetListRow the assetListRow to set
	 */
	public void setAssetListRow(String assetListRow) {
		this.assetListRow = assetListRow;
	}
	/**
	 * @return the updateColumns
	 */
	public List<String> getUpdateColumns() {
		return updateColumns;
	}
	/**
	 * @param updateColumns the updateColumns to set
	 */
	public void setUpdateColumns(List<String> updateColumns) {
		this.updateColumns = updateColumns;
	}
	/**
	 * @return the assetMap
	 */
	public Map<String, String> getAssetMap() {
		return assetMap;
	}
	/**
	 * @param assetMap the assetMap to set
	 */
	public void setAssetMap(Map<String, String> assetMap) {
		this.assetMap = assetMap;
	}
	public String getUploadFileSize() {
		return uploadFileSize;
	}
	public void setUploadFileSize(String uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}
	/**
	 * @return the assetListTaiXinRow
	 */
	public String getAssetListTaiXinRow() {
		return assetListTaiXinRow;
	}
	/**
	 * @param assetListTaiXinRow the assetListTaiXinRow to set
	 */
	public void setAssetListTaiXinRow(String assetListTaiXinRow) {
		this.assetListTaiXinRow = assetListTaiXinRow;
	}
	/**
	 * @return the assetListJdwRow
	 */
	public String getAssetListJdwRow() {
		return assetListJdwRow;
	}
	/**
	 * @param assetListJdwRow the assetListJdwRow to set
	 */
	public void setAssetListJdwRow(String assetListJdwRow) {
		this.assetListJdwRow = assetListJdwRow;
	}
	/**
	 * @return the repositoryDTOTaiXins
	 */
	public List<DmmRepositoryDTO> getRepositoryDTOTaiXins() {
		return repositoryDTOTaiXins;
	}
	/**
	 * @param repositoryDTOTaiXins the repositoryDTOTaiXins to set
	 */
	public void setRepositoryDTOTaiXins(List<DmmRepositoryDTO> repositoryDTOTaiXins) {
		this.repositoryDTOTaiXins = repositoryDTOTaiXins;
	}
	/**
	 * @return the repositoryDTOJdws
	 */
	public List<DmmRepositoryDTO> getRepositoryDTOJdws() {
		return repositoryDTOJdws;
	}
	/**
	 * @param repositoryDTOJdws the repositoryDTOJdws to set
	 */
	public void setRepositoryDTOJdws(List<DmmRepositoryDTO> repositoryDTOJdws) {
		this.repositoryDTOJdws = repositoryDTOJdws;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the updateTable
	 */
	public String getUpdateTable() {
		return updateTable;
	}
	/**
	 * @param updateTable the updateTable to set
	 */
	public void setUpdateTable(String updateTable) {
		this.updateTable = updateTable;
	}
	/**
	 * @return the monthYear
	 */
	public String getMonthYear() {
		return monthYear;
	}
	/**
	 * @param monthYear the monthYear to set
	 */
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	
}
