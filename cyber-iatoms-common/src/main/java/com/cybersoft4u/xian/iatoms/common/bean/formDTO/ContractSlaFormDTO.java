package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
/**
 * Purpose: 合約SLA設定FormDTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/6/28
 * @MaintenancePersonnel CrissZhang
 */
public class ContractSlaFormDTO extends AbstractSimpleListFormDTO<ContractSlaDTO>{
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -2783219996704130049L;
	
	/**
	 *  查询的客户编号
	 */
	public static final String QUERY_CUSTOMSER_ID = "queryCustomerId"; 
	/**
	 * 查詢合約ID
	 */
	public static final String QUERY_CONTRACT_ID = "queryContractId";
	/**
	 * 查詢區域主檔
	 */
	public static final String QUERY_LOCATION = "queryLocation";
	/**
	 * 查询案件類別
	 */
	public static final String QUERY_TICKET_TYPE = "queryTicketType";
	/**
	 * 查询案件類型
	 */
	public static final String QUERY_IMPORTANCE = "queryImportance";
	/**
	 * 複製合约編號
	 */
	public static final String COPY_CONTRACT_ID = "copyContractId";
	/**
	 * 複製客户編號
	 */
	public static final String COPY_CUSTOMER_ID = "copyCustomerId";
	/**
	 * 原始的客户編號
	 */
	public static final String ORIGINAL_CUSTOMER_ID = "originalCustomerId";
	/**
	 * 原始的合约編號
	 */
	public static final String ORIGINAL_CONTRACT_ID = "originalContractId";
	/**
	 * 生成错误文件的名称
	 */
	public static final String ERROR_FILE_NAME = "errorFileName";
	/**
	 * 臨時文件路徑
	 */
	public static final String TEMP_FILE_PATH = "tempFilePath";
	/**
	 * 案件類型列表
	 */
	public static final String PARAM_TICKET_MODE_LIST = "ticketModeList";
	
	/**
	 * 排序方式
	 */
	public static final String PARAM_PAGE_SORT = "customerName,contractCode,ticketTypeName,locationName,ticketModeName";
	/**
	 * 合约sla模板中文名称
	 */
	public static final String CONTRACT_SLA_TEMPLATE_NAME_FOR_CN									= "合約SLA範本.xls";
	/**
	 * 合约sla模板英文名称
	 */
	public static final String CONTRACT_SLA_TEMPLATE_NAME_FOR_EN									= "contractSlaTemplate.xls";
	/**
	 * 合約sla主鍵Id
	 */
	public static final String PARAM_SLA_ID = "slaId";
	/**
	 * 查询客户ID
	 */
	private String queryCustomerId;
	/**
	 * 查詢合約ID
	 */
	private String queryContractId;
	/**
	 * 查詢區域主檔
	 */
	private String queryLocation;
	/**
	 * 查询案件類別
	 */
	private String queryTicketType;
	/**
	 * 案件類型
	 */
	private String queryImportance;
	/**
	 * 合約SLA設定DTO的List
	 */
	private List<ContractSlaDTO> bimSlaDTOList;
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
	 * 合約SLA設定DTO
	 */
	private ContractSlaDTO contractSlaDTO;

	/**
	 * 上傳文件
	 */
	private MultipartFile uploadFiled;
	/**
	 * 生成的错误文件名
	 */
	private String errorFileName;
	/**
	 * 複製合约編號
	 */
	private String copyContractId;
	/**
	 * 複製客户編號
	 */
	private String copyCustomerId;
	/**
	 * 原始的客户編號
	 */
	private String originalCustomerId;
	/**
	 * 原始的合约編號
	 */
	private String originalContractId;
	
	/**
	 * 案件類別
	 */
	private String ticketType;
	
	/**
	 * 區域主檔
	 */
	private String location;
	
	/**
	 * 案件類型
	 */
	private String ticketMode;
	
	/**
	 * 合約ID
	 */
	private String contractId;
	
	/**
	 * 客戶ID
	 */
	private String customerId;
	
	/**
	 * 合約sla主鍵Id
	 */
	private String slaId;
	/**
	 * 臨時文件路徑
	 */
	private String tempFilePath;
	/**
	 * 上传文件最大值
	 */
	private String uploadFileSize;
	/**
	 * Constructor:無參構造函數
	 */
	public ContractSlaFormDTO() {
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
	 * @return the queryLocation
	 */
	public String getQueryLocation() {
		return queryLocation;
	}

	/**
	 * @param queryLocation the queryLocation to set
	 */
	public void setQueryLocation(String queryLocation) {
		this.queryLocation = queryLocation;
	}

	/**
	 * @return the queryTicketType
	 */
	public String getQueryTicketType() {
		return queryTicketType;
	}

	/**
	 * @param queryTicketType the queryTicketType to set
	 */
	public void setQueryTicketType(String queryTicketType) {
		this.queryTicketType = queryTicketType;
	}

	/**
	 * @return the queryImportance
	 */
	public String getQueryImportance() {
		return queryImportance;
	}

	/**
	 * @param queryImportance the queryImportance to set
	 */
	public void setQueryImportance(String queryImportance) {
		this.queryImportance = queryImportance;
	}

	/**
	 * @return the bimSlaDTOList
	 */
	public List<ContractSlaDTO> getBimSlaDTOList() {
		return bimSlaDTOList;
	}

	/**
	 * @param bimSlaDTOList the bimSlaDTOList to set
	 */
	public void setBimSlaDTOList(List<ContractSlaDTO> bimSlaDTOList) {
		this.bimSlaDTOList = bimSlaDTOList;
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
	 * @return the contractSlaDTO
	 */
	public ContractSlaDTO getContractSlaDTO() {
		return contractSlaDTO;
	}

	/**
	 * @param contractSlaDTO the contractSlaDTO to set
	 */
	public void setContractSlaDTO(ContractSlaDTO contractSlaDTO) {
		this.contractSlaDTO = contractSlaDTO;
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
	 * @return the copyContractId
	 */
	public String getCopyContractId() {
		return copyContractId;
	}

	/**
	 * @param copyContractId the copyContractId to set
	 */
	public void setCopyContractId(String copyContractId) {
		this.copyContractId = copyContractId;
	}

	/**
	 * @return the copyCustomerId
	 */
	public String getCopyCustomerId() {
		return copyCustomerId;
	}

	/**
	 * @param copyCustomerId the copyCustomerId to set
	 */
	public void setCopyCustomerId(String copyCustomerId) {
		this.copyCustomerId = copyCustomerId;
	}

	/**
	 * @return the originalCustomerId
	 */
	public String getOriginalCustomerId() {
		return originalCustomerId;
	}

	/**
	 * @param originalCustomerId the originalCustomerId to set
	 */
	public void setOriginalCustomerId(String originalCustomerId) {
		this.originalCustomerId = originalCustomerId;
	}

	/**
	 * @return the originalContractId
	 */
	public String getOriginalContractId() {
		return originalContractId;
	}

	/**
	 * @param originalContractId the originalContractId to set
	 */
	public void setOriginalContractId(String originalContractId) {
		this.originalContractId = originalContractId;
	}

	/**
	 * @return the ticketType
	 */
	public String getTicketType() {
		return ticketType;
	}

	/**
	 * @param ticketType the ticketType to set
	 */
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the ticketMode
	 */
	public String getTicketMode() {
		return ticketMode;
	}

	/**
	 * @param ticketMode the ticketMode to set
	 */
	public void setTicketMode(String ticketMode) {
		this.ticketMode = ticketMode;
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
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the slaId
	 */
	public String getSlaId() {
		return slaId;
	}

	/**
	 * @param slaId the slaId to set
	 */
	public void setSlaId(String slaId) {
		this.slaId = slaId;
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

	public String getUploadFileSize() {
		return uploadFileSize;
	}

	public void setUploadFileSize(String uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}
	
}
