package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
/**
 * Purpose: 客戶特店FormDTO
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/6/20
 * @MaintenancePersonnel HermanWang
 */
public class MerchantFormDTO extends AbstractSimpleListFormDTO<MerchantDTO> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID 										= -568679119810699846L;

	/**
	 * 查詢參數客戶id
	 */
	public static final String QUERY_COMPANY_ID 									= "queryCompanyId";
	
	/**
	 * 查詢參數特店名稱
	 */
	public static final String QUERY_NAME 											= "queryName";
	/**
	 * 查詢參數特店代號
	 */
	public static final String QUERY_MERCHANT_CODE 									= "queryMerchantCode";
	/**
	 * 查詢參數分期特店代號
	 */
	public static final String QUERY_STAGES_MERCHANT_CODE 							= "queryStagesMerchantCode";
	/**
	 * 客戶特店错误信息名字
	 */
	public static final String ERROR_FILE_NAME										= "errorFileName";
	/**
	 * 客戶特店错误信息地址
	 */
	public static final String MERCHANT_TEMP_PATH									= "MERCHANT_TEMP_PATH";
	/**
	 * 客戶特店匯入格式下載信息路徑
	 */
	public static final String TEMP_FILE_PATH 										= "tempFilePath";
	/**
	 * 客戶特店匯入錯誤名稱
	 */
	public static final String TEMP_ERROR_FILE_PATH									= "tempErrorFile";
	/**
	 * 客戶特店匯入格式下載模板中文名称
	 */
	public static final String MERCHANT_TEMPLATE_NAME_FOR_CN						= "客戶特店範本.xls";
	/**
	 * 客戶特店匯入格式下載模板英文名称
	 */
	public static final String MERCHANT_TEMPLATE_NAME_FOR_EN						= "merchantTemplate.xls";
	/**
	 * Constructor:有參的構造方法
	 */
	public MerchantFormDTO(String queryCompanyId, String queryName, String queryMerchantCode,
			String merchantId, MerchantDTO bimMerchantDTO, Integer rows, String queryStagesMerchantCode,
			Integer page, String sort, String order, Integer totalSize) {
		super();
		this.queryCompanyId = queryCompanyId;
		this.queryName = queryName;
		this.queryMerchantCode = queryMerchantCode;
		this.queryStagesMerchantCode = queryStagesMerchantCode;
		this.merchantId = merchantId;
		this.bimMerchantDTO = bimMerchantDTO;
		this.rows = rows;
		this.page = page;
		this.sort = sort;
		this.order = order;
		this.totalSize = totalSize;
	}
	
	/**
	 * 查尋參數，客戶id
	 */
	private String queryCompanyId;

	/**
	 * 查詢參數,客戶特店名稱
	 */
	private String queryName;
	/**
	 * 查詢參數特店代號
	 */
	private String queryMerchantCode;
	/**
	 * 查詢參數-營業地址
	 */
	private String queryBusinessAddress;
	/**
	 * 查詢參數分期特店代號
	 */
	private String queryStagesMerchantCode;
 
	/**
	 * 客戶特店的主鍵
	 */
	private String merchantId;

	/**
	 * 客戶特店DTO,edit時使用
	 */
	private MerchantDTO bimMerchantDTO;
	
	/**
	 * 公司DTO
	 */
	private CompanyDTO companyDTO;
	/**
	 * 客戶特店DTO
	 */
	private BimMerchantHeaderDTO bimMerchantHeaderDTO;
	
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
	 * 查詢結果總條數
	 */
	private Integer totalSize;
	/**
	 * 臨時文件路徑
	 */
	private String tempErrorFile;

	/**
	 * 上传文档
	 */
	private MultipartFile uploadFiled;
	/**
	 * 錯誤文件名
	 */
	private String errorFileName;
	/**
	 * file地址
	 */
	private String tempFilePath;
	/**
	 * 上传文件最大值
	 */
	private String uploadFileSize;
	/**
	 * Task #3583 當前登入者角色屬性
	 */
	private String roleAttribute;
	
	/**
	 * 無參的構造方法
	 */
	public MerchantFormDTO() {
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
	 * @return the queryName
	 */
	public String getQueryName() {
		return queryName;
	}

	/**
	 * @param queryName the queryName to set
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	/**
	 * @return the bimMerchantDTO
	 */
	public MerchantDTO getBimMerchantDTO() {
		return bimMerchantDTO;
	}

	/**
	 * @param bimMerchantDTO the bimMerchantDTO to set
	 */
	public void setBimMerchantDTO(MerchantDTO bimMerchantDTO) {
		this.bimMerchantDTO = bimMerchantDTO;
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
	 * @return the merchantId
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the bimMerchantHeaderDTO
	 */
	public BimMerchantHeaderDTO getBimMerchantHeaderDTO() {
		return bimMerchantHeaderDTO;
	}

	/**
	 * @param bimMerchantHeaderDTO the bimMerchantHeaderDTO to set
	 */
	public void setBimMerchantHeaderDTO(BimMerchantHeaderDTO bimMerchantHeaderDTO) {
		this.bimMerchantHeaderDTO = bimMerchantHeaderDTO;
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
	 * @return the queryStagesMerchantCode
	 */
	public String getQueryStagesMerchantCode() {
		return queryStagesMerchantCode;
	}

	/**
	 * @param queryStagesMerchantCode the queryStagesMerchantCode to set
	 */
	public void setQueryStagesMerchantCode(String queryStagesMerchantCode) {
		this.queryStagesMerchantCode = queryStagesMerchantCode;
	}

	/**
	 * @return the tempErrorFile
	 */
	public String getTempErrorFile() {
		return tempErrorFile;
	}

	/**
	 * @param tempErrorFile the tempErrorFile to set
	 */
	public void setTempErrorFile(String tempErrorFile) {
		this.tempErrorFile = tempErrorFile;
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
	 * @return the companyDTO
	 */
	public CompanyDTO getCompanyDTO() {
		return companyDTO;
	}

	/**
	 * @param companyDTO the companyDTO to set
	 */
	public void setCompanyDTO(CompanyDTO companyDTO) {
		this.companyDTO = companyDTO;
	}

	/**
	 * @return the queryBusinessAddress
	 */
	public String getQueryBusinessAddress() {
		return queryBusinessAddress;
	}

	/**
	 * @param queryBusinessAddress the queryBusinessAddress to set
	 */
	public void setQueryBusinessAddress(String queryBusinessAddress) {
		this.queryBusinessAddress = queryBusinessAddress;
	}

	public String getUploadFileSize() {
		return uploadFileSize;
	}

	public void setUploadFileSize(String uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}

	/**
	 * @return the roleAttribute
	 */
	public String getRoleAttribute() {
		return roleAttribute;
	}

	/**
	 * @param roleAttribute the roleAttribute to set
	 */
	public void setRoleAttribute(String roleAttribute) {
		this.roleAttribute = roleAttribute;
	}
	
	
}