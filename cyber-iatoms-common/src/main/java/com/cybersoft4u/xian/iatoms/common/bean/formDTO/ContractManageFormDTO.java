package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAttachedFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;

/**
 * 
 * Purpose:合约维护FormDTO  
 * @author allenchen
 * @since  JDK 1.7
 * @date   2016/5/17
 * @MaintenancePersonnel allenchen
 */
public class ContractManageFormDTO extends AbstractSimpleListFormDTO<BimContractDTO>{
	/**
	 *  系统日志记录物件  
	 */
	private static final long serialVersionUID = -5646769166915160566L;

	/**
	 * 无参构造子
	 */
	public ContractManageFormDTO() {
		
	}
	/**
	 * 查询条件--客户
	 */
	public static final String QUERY_CUSTOMSER_ID = "queryCustomerId"; 
	/**
	 * 查询条件--厂商
	 */
	public static final String QUERY_MANU_FACTURER = "queryManuFacturer";
	/**
	 * 查詢合約設備--名稱集合
	 */
	public static final String QUERY_CONTRACT_ASSET_LIST = "queryContractAssetList";
	/**
	 * 查詢合約--設備名稱集合
	 */
	public static final String PARAM_ASSET_NAME_LIST = "assetNameList";
	/**
	 * 設備類別集合
	 */
	public static final String PARAM_ASSET_CATEGORY_LIST = "assetCategoryList";
	/**
	 * 合約狀態默認值
	 */
	public static final String PARAM_CONTRACT_STATUS_DEFAULT_VALUE = "2";
	/**
	 * 付款方式默認值
	 */
	public static final String PARAM_PAY_MODE_DEFAULT_VALUE = "1";
	/**
	 * 
	 */
	public static final String CODE_ISO = "iso-8859-1";
	/**
	 * 
	 */
	public static final String CODE_UTF = "utf-8";
	/**
	 * 上傳文件路徑
	 */
	public static final String PARAM_FILE_UPLOAD_PATH		= "/upload";
	/**
	 * 隱藏合約ID
	 */
	public static final String PARAM_HIDDEN_CONTRACT_ID		= "hiddenContractId";
	/**
	 * 上傳文件名稱
	 */
	public static final String PAPAM_PAGE_NAME_FILE_NAME 	= "fileName";
	/**
	 * 排序條件1
	 */
	public static final String PARAM_PAGE_SORT_ONE = "contractCode";
	/**
	 * 排序條件2
	 */
	public static final String PARAM_PAGE_SORT_TWO = "customerName";
	/**
	 * 獲取下拉列表的排序列
	 */
	public static final String PARAM_ORDER_BY_CONTRACT_CODE = "contract_code";
	
	
	/**
	 * 上傳文件標識字段
	 */
	private String qquuid;	
	/**
	 * 查询条件：客户ID
	 */
	private String queryCustomerId;
	/**
	 * 查询条件：厂商
	 */
	private String queryManuFacturer;
	/**
	 * 合约维护ＤＴＯ
	 */
	private BimContractDTO contractManageDTO;
	/**
	 * 合约维护查询列表
	 */
	private List<BimContractDTO> listContractManageDTOs ;
	/**
	 * 保存時,新增修改操作ActionId
	 */
	private String opActionId;
	/**
	 * 保存时，存储设备集合的字符串
	 */
	private String assetListRow;
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
	 * 合約ID
	 */
	private String contractId;
	/**
	 * 文件路徑
	 */
	private String filePath;
	/**
	 * 臨時文件名
	 */
	private String fileTempName;
	
	/**
	 * 文件保存名稱
	 */
	private String fileName;
	
	/**
	 * 刪除的文件ID
	 */
	private String deleteFileId;
	
	/**
	 * 合約文件集合
	 */
	private List<BimContractAttachedFileDTO> contractAttachedFileDTOs;
	/**
	 * 下載文件ID
	 */
	private String attachedFileId;
	/**
	 * 
	 */
	private Map<String, MultipartFile> fileMap;
	/**
	 * 上传文件最大值
	 */
	private String uploadFileSize;
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
	 * @return the queryCustomerId
	 */
	public String getQueryCustomerId() {
		return queryCustomerId;
	}
	/**
	 * @param queryCustomerId the queryCustomerId to set
	 */
	public void setQueryCustomerId(String queryCustomerId) {
		this.queryCustomerId = queryCustomerId;
	}
	/**
	 * @return the queryManuFacturer
	 */
	public String getQueryManuFacturer() {
		return queryManuFacturer;
	}
	/**
	 * @param queryManuFacturer the queryManuFacturer to set
	 */
	public void setQueryManuFacturer(String queryManuFacturer) {
		this.queryManuFacturer = queryManuFacturer;
	}
	/**
	 * @return the contractManageDTO
	 */
	public BimContractDTO getContractManageDTO() {
		return contractManageDTO;
	}
	/**
	 * @param contractManageDTO the contractManageDTO to set
	 */
	public void setContractManageDTO(BimContractDTO contractManageDTO) {
		this.contractManageDTO = contractManageDTO;
	}
	/**
	 * @return the listContractManageDTOs
	 */
	public List<BimContractDTO> getListContractManageDTOs() {
		return listContractManageDTOs;
	}
	/**
	 * @param listContractManageDTOs the listContractManageDTOs to set
	 */
	public void setListContractManageDTOs(
			List<BimContractDTO> listContractManageDTOs) {
		this.listContractManageDTOs = listContractManageDTOs;
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
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	/**
	 * @return the opActionId
	 */
	public String getOpActionId() {
		return opActionId;
	}
	/**
	 * @param opActionId the opActionId to set
	 */
	public void setOpActionId(String opActionId) {
		this.opActionId = opActionId;
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
	 * @return the fileTempName
	 */
	public String getFileTempName() {
		return fileTempName;
	}
	/**
	 * @param fileTempName the fileTempName to set
	 */
	public void setFileTempName(String fileTempName) {
		this.fileTempName = fileTempName;
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
	 * @return the contractAttachedFileDTOs
	 */
	public List<BimContractAttachedFileDTO> getContractAttachedFileDTOs() {
		return contractAttachedFileDTOs;
	}
	/**
	 * @param contractAttachedFileDTOs the contractAttachedFileDTOs to set
	 */
	public void setContractAttachedFileDTOs(
			List<BimContractAttachedFileDTO> contractAttachedFileDTOs) {
		this.contractAttachedFileDTOs = contractAttachedFileDTOs;
	}
	/**
	 * @return the deleteFileId
	 */
	public String getDeleteFileId() {
		return deleteFileId;
	}
	/**
	 * @param deleteFileId the deleteFileId to set
	 */
	public void setDeleteFileId(String deleteFileId) {
		this.deleteFileId = deleteFileId;
	}
	/**
	 * @return the fileMap
	 */
	public Map<String, MultipartFile> getFileMap() {
		return fileMap;
	}
	/**
	 * @param fileMap the fileMap to set
	 */
	public void setFileMap(Map<String, MultipartFile> fileMap) {
		this.fileMap = fileMap;
	}
	/**
	 * @return the attachedFileId
	 */
	public String getAttachedFileId() {
		return attachedFileId;
	}
	/**
	 * @param attachedFileId the attachedFileId to set
	 */
	public void setAttachedFileId(String attachedFileId) {
		this.attachedFileId = attachedFileId;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUploadFileSize() {
		return uploadFileSize;
	}
	public void setUploadFileSize(String uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}
	
	
	
}
