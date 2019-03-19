package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmComplaintCaseFileDTO;
/**
 * Purpose: 客訴管理FormDTO
 * @author 	nicklin
 * @since	JDK 1.7
 * @date	2018/2/27
 * @MaintenancePersonnel cybersoft
 */
public class ComplaintFormDTO extends AbstractSimpleListFormDTO<ComplaintDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7574515941110500953L;
	/**
	 * 客訴編號
	 */
	public static final String CASE_ID = "caseId";
	/**
	 * 查詢條件:客訴案號
	 */
	//public static final String QUERY_CASE_ID = "queryCaseId";
	/**
	 * 查詢條件:客戶
	 */
	public static final String QUERY_CUSTOMER_ID = "queryCustomerId";
	/**
	 * 查詢條件:特店代號
	 */
	public static final String QUERY_MERCHANT = "queryMerchant";
	/**
	 * 查詢條件:TID/DTID
	 */
	public static final String QUERY_TID = "queryTid";
	/**
	 * 查詢條件:特店名稱
	 */
	public static final String QUERY_MERCHANT_NAME = "queryMerchantName";
	/**
	 * 查詢條件:問題分類
	 */
	public static final String QUERY_QUESTION_TYPE = "queryQuestionType";
	/**
	 * 查詢條件:歸責廠商
	 */
	public static final String QUERY_VENDOR = "queryVendor";
	/**
	 * 查詢條件:賠償客戶
	 */
	public static final String QUERY_IS_CUSTOMER = "queryIsCustomer";
	/**
	 * 查詢條件:廠商罰款
	 */
	public static final String QUERY_IS_VENDOR = "queryIsVendor";
	/**
	 * 查詢條件:發生日期起
	 */
	public static final String QUERY_START_DATE = "queryStartDate";
	/**
	 * 查詢條件:發生日期迄
	 */
	public static final String QUERY_END_DATE = "queryEndDate";
	/**
	 * 客訴案件報表文件名稱
	 */
	public static final String PROJECT_REPORT_JRXML_NAME = "SRM_COMPLAINT_REPORT";
	/**
	 * 客訴案件報表匯出名稱
	 */
	public static final String PROJECT_REPORT_FILE_NAME = "客訴案件清單";
	/**
	 * 排序欄位
	 */
	public static final String PARAM_PAGE_SORT = "createdDate";
	/**
	 * 排序方式
	 */
	public static final String PARAM_PAGE_ORDER = "desc";
	/**
	 * 上傳文件名稱
	 */
	public static final String PAPAM_PAGE_NAME_FILE_NAME = "fileName";
	/**
	 * 客訴編號
	 */
	private String caseId;
	/**
	 * 查詢條件:客戶
	 */
	private String queryCustomerId;
	/**
	 * 查詢條件:客訴編號
	 */
	//private String queryCaseId;
	/**
	 * 查詢條件:特店代號
	 */
	private String queryMerchant;
	/**
	 * 查詢條件:TID/DTID
	 */
	private String queryTid;
	/**
	 * 查詢條件:特店名稱
	 */
	private String queryMerchantName;
	/**
	 * 查詢條件:問題分類
	 */
	private String queryQuestionType;
	/**
	 * 查詢條件:歸責廠商
	 */
	private String queryVendor;
	/**
	 * 查詢條件:賠償客戶
	 */
	private String queryIsCustomer;
	/**
	 * 查詢條件:廠商罰款
	 */
	private String queryIsVendor;
	/**
	 * 查詢條件:發生日期起
	 */
	private String queryStartDate;
	/**
	 * 查詢條件:發生日期迄
	 */
	private String queryEndDate;
	/**
	 * 上傳文件標識字段
	 */
	private String qquuid;
	/**
	 * 文件路徑
	 */
	private String filePath;
	/**
	 * 上傳文件最大值
	 */
	private String uploadFileSize;
	/**
	 * 文件ID
	 */
	private String fileId;
	/**
	 * 文件名稱
	 */
	private String fileName;
	/**
	 * 上傳文件的map集合
	 */
	private Map<String, MultipartFile> fileMap;
	/**
	 * 欲刪除的文件ID
	 */
	private String deleteFileId;
	/**
	 * 客訴管理文件集合
	 */
	private List<SrmComplaintCaseFileDTO> srmComplaintCaseFileDTOs;
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
	 * 客訴管理DTO
	 */
	private ComplaintDTO complaintDTO;
	/**
	 * 特店formDTO
	 */
	private MerchantFormDTO merchantFormDTO;
	
	/**
	 * Constructor: 無參數建構子
	 */
	public ComplaintFormDTO() {
		super();
	}

	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}
	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
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
	 * @return the queryIsCustomer
	 */
	public String getQueryIsCustomer() {
		return queryIsCustomer;
	}
	/**
	 * @param queryIsCustomer the queryIsCustomer to set
	 */
	public void setQueryIsCustomer(String queryIsCustomer) {
		this.queryIsCustomer = queryIsCustomer;
	}
	/**
	 * @return the queryMerchant
	 */
	public String getQueryMerchant() {
		return queryMerchant;
	}
	/**
	 * @param queryMerchant the queryMerchant to set
	 */
	public void setQueryMerchant(String queryMerchant) {
		this.queryMerchant = queryMerchant;
	}
	/**
	 * @return the queryMerchantName
	 */
	public String getQueryMerchantName() {
		return queryMerchantName;
	}
	/**
	 * @param queryMerchantName the queryMerchantName to set
	 */
	public void setQueryMerchantName(String queryMerchantName) {
		this.queryMerchantName = queryMerchantName;
	}
	/**
	 * @return the queryCompany
	 */
	public String getQueryVendor() {
		return queryVendor;
	}
	/**
	 * @param queryCompany the queryCompany to set
	 */
	public void setQueryVendor(String queryVendor) {
		this.queryVendor = queryVendor;
	}
	/**
	 * @return the queryTid
	 */
	public String getQueryTid() {
		return queryTid;
	}
	/**
	 * @param queryTid the queryTid to set
	 */
	public void setQueryTid(String queryTid) {
		this.queryTid = queryTid;
	}
	/**
	 * @return the queryQuestionType
	 */
	public String getQueryQuestionType() {
		return queryQuestionType;
	}
	/**
	 * @param queryQuestionType the queryQuestionType to set
	 */
	public void setQueryQuestionType(String queryQuestionType) {
		this.queryQuestionType = queryQuestionType;
	}
	/**
	 * @return the queryIsVendor
	 */
	public String getQueryIsVendor() {
		return queryIsVendor;
	}
	/**
	 * @param queryIsVendor the queryIsVendor to set
	 */
	public void setQueryIsVendor(String queryIsVendor) {
		this.queryIsVendor = queryIsVendor;
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
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}
	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
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
	 * @return the srmComplaintCaseFileDTOs
	 */
	public List<SrmComplaintCaseFileDTO> getSrmComplaintCaseFileDTOs() {
		return srmComplaintCaseFileDTOs;
	}
	/**
	 * @param srmComplaintCaseFileDTOs the srmComplaintCaseFileDTOs to set
	 */
	public void setSrmComplaintCaseFileDTOs(
			List<SrmComplaintCaseFileDTO> srmComplaintCaseFileDTOs) {
		this.srmComplaintCaseFileDTOs = srmComplaintCaseFileDTOs;
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
	 * @return the merchantFormDTO
	 */
	public MerchantFormDTO getMerchantFormDTO() {
		return merchantFormDTO;
	}
	/**
	 * @param merchantFormDTO the merchantFormDTO to set
	 */
	public void setMerchantFormDTO(MerchantFormDTO merchantFormDTO) {
		this.merchantFormDTO = merchantFormDTO;
	}
	/**
	 * @return the complaintDTO
	 */
	public ComplaintDTO getComplaintDTO() {
		return complaintDTO;
	}
	/**
	 * @param complaintDTO the complaintDTO to set
	 */
	public void setComplaintDTO(ComplaintDTO complaintDTO) {
		this.complaintDTO = complaintDTO;
	}
}
