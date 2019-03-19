package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.EdcParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseNewTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO;

/**
 * Purpose: EDC交易參數查詢FormDTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/9/22
 * @MaintenancePersonnel CrissZhang
 */
public class EdcParameterFormDTO extends AbstractSimpleListFormDTO<SrmCaseHandleInfoDTO>{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -6245041879559459320L;
	
	/**
	 * 查詢參數,客戶
	 */
	public static final String QUERY_CUSTOMER_ID								= "queryCustomerId";
	
	/**
	 * 查詢參數,特店代號
	 */
	public static final String QUERY_MERCHANT_CODE								= "queryMerchantCode";
	
	/**
	 * 查詢參數,特點名稱
	 */
	public static final String QUERY_MERCHANT_NAME								= "queryMerchantName";
	
	/**
	 * 查詢參數,表頭（同對外名稱）
	 */
	public static final String QUERY_MERANNOUNCED_NAME							= "queryMerAnnouncedName";
	
	/**
	 * 查詢參數,TID
	 */
	public static final String QUERY_TID										= "queryTid";
	
	/**
	 * 查詢參數,DTID
	 */
	public static final String QUERY_DTID										= "queryDtid";
	
	/**
	 * 查詢參數,EDC 機型
	 */
	public static final String QUERY_EDC_TYPE									= "queryEdcType";
	
	/**
	 * 查詢參數,週邊設備機型
	 */
	public static final String QUERY_PERIPHERAL_EQUIPMENT						= "queryPeripheralEquipment";
	
	/**
	 * 查詢參數,設備支援功能
	 */
	public static final String QUERY_ASSET_SUPPORTED_FUNCTION					= "queryAssetSupportedFunction";
	
	/**
	 * 查詢參數,設備開啟模式
	 */
	public static final String QUERY_ASSET_OPEN_MODE							= "queryAssetOpenMode";
	
	/**
	 * 查詢參數,設備狀態
	 */
	public static final String QUERY_ASSET_STATUS								= "queryAssetStatus";
	
	/**
	 * 查詢參數,已開放交易
	 */
	public static final String QUERY_OPEN_TRANSACTION							= "queryOpenTransaction";
	
	/**
	 * 交易類別父項id
	 */
	public static final String TRANS_CATEGORY_PARENT_ITEM_ID					= "TICKET_TYPE_01";
	
	/**
	 * 主報表名稱
	 */
	public static final String PROJECT_REPORT_JRXML_NAME						= "EDC_PARAMETER_REPORT";
	
	/**
	 * 子報表名稱
	 */
	public static final String PROJECT_SUB_REPORT_JRXML_NAME					= "EDC_PARAMETER_REPORT_SUB";
	
	/**
	 * 子報表定義
	 */
	public static final String SUBREPORT_DIR									= "SUBREPORT_DIR";
	
	/**
	 * 匯出報表名稱
	 */
	public static final String PROJECT_REPORT_FILE_NAME							= "EDC交易參數查詢範本";
	
	/**
	 * edc列表
	 */
	public static final String PARAM_EDC_ASSET_LIST								= "edcAssetList";
	
	/**
	 * 周邊設備列表
	 */
	public static final String PARAM_CATEGORY_RODUND_ASSET_LIST					= "categoryRodundAssetList";
	
	/**
	 * 設備開啟模式列表
	 */
	public static final String PARAM_OPEN_MODE_LIST								= "openModeList";
	
	/**
	 * 是否有角色權限
	 */
	public static final String PARAM_IS_NO_ROLES								= "isNoRoles";
	
	/**
	 * 角色屬性
	 */
	public static final String PARAM_ROLE_ATTRIBUTE								= "roleAttribute";
	
	/**
	 * 內建
	 */
	public static final String PARAM_BUILT_IN									= "roleAttribute";
	
	/**
	 * 內建
	 */
	public static final String PARAM_EXTERNAL									= "roleAttribute";
	/**
	 * 默認排序
	 */
	public static final String PARAM_PAGE_SORT = "companyName,merchantCode";
	/**
	 * Constructor:無參構造函數
	 */
	public EdcParameterFormDTO(){
	}
	/**
	 * 查詢參數,客戶
	 */
	private String queryCustomerId;
	/**
	 * 查詢參數,特店代號
	 */
	private String queryMerchantCode;
	/**
	 * 查詢參數,特點名稱
	 */
	private String queryMerchantName;
	/**
	 * 查詢參數,表頭（同對外名稱）
	 */
	private String queryMerAnnouncedName;
	/**
	 * 查詢參數,TID
	 */
	private String queryTid;
	/**
	 * 查詢參數,DTID
	 */
	private String queryDtid;
	/**
	 * 查詢參數,EDC 機型
	 */
	private String queryEdcType;
	/**
	 * 查詢參數,週邊設備機型
	 */
	private String queryPeripheralEquipment;
	/**
	 * 查詢參數,設備支援功能
	 */
	private String queryAssetSupportedFunction;
	/**
	 * 查詢參數,設備開啟模式
	 */
	private String queryAssetOpenMode;
	/**
	 * 查詢參數,設備狀態
	 */
	private String queryAssetStatus;
	/**
	 * 查詢參數,已開放交易
	 */
	private String queryOpenTransaction;
	/**
	 * 查詢參數,每頁顯示條數
	 */
	private Integer rows;
	/**
	 * 查詢參數,當前頁碼
	 */
	private Integer page;
	/**
	 * 查詢參數,排序方式
	 */
	private String sort;
	/**
	 * 查詢參數,排序字段
	 */
	private String order;
	/**
	 * EDC交易參數DTO對象
	 */
	private EdcParameterDTO edcParameterDTO;
	/**
	 * 交易參數DTO對象
	 */
	private SrmCaseNewTransactionParameterDTO transactionParameterDTO;
	/**
	 * 交易參數DTO對象列表
	 */
	private List<SrmCaseNewTransactionParameterDTO> transactionParameterDTOs;
	/**
	 * 案件編號
	 */
	private String caseId;
	/**
	 * 當前登入者角色屬性
	 */
	private String roleAttribute;
	
	/**
	 * 公司編號列表
	 */
	private List<String> companyIdList;
	/**
	 * 當前登入者為客戶屬性對應之公司
	 */
	private String logonUserCompanyId;
	/**
	 * 沒有角色權限
	 */
	private Boolean isNoRoles;
	
	/**
	 * 查詢參數,內建
	 */
	private Boolean queryBuiltIn;
	/**
	 * 查詢參數,外接
	 */
	private Boolean queryExternal;
	/**
	 * dtid
	 */
	private String dtid;
	/**
	 * 案件处理DTO集合
	 */
	private List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs;
	/**
	 * 交易參數集合
	 */
	private List<SrmTransactionParameterItemDTO> transactionParameterItemDTOs;
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
	 * @return the queryMerAnnouncedName
	 */
	public String getQueryMerAnnouncedName() {
		return queryMerAnnouncedName;
	}
	/**
	 * @param queryMerAnnouncedName the queryMerAnnouncedName to set
	 */
	public void setQueryMerAnnouncedName(String queryMerAnnouncedName) {
		this.queryMerAnnouncedName = queryMerAnnouncedName;
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
	 * @return the queryEdcType
	 */
	public String getQueryEdcType() {
		return queryEdcType;
	}
	/**
	 * @param queryEdcType the queryEdcType to set
	 */
	public void setQueryEdcType(String queryEdcType) {
		this.queryEdcType = queryEdcType;
	}
	/**
	 * @return the queryPeripheralEquipment
	 */
	public String getQueryPeripheralEquipment() {
		return queryPeripheralEquipment;
	}
	/**
	 * @param queryPeripheralEquipment the queryPeripheralEquipment to set
	 */
	public void setQueryPeripheralEquipment(String queryPeripheralEquipment) {
		this.queryPeripheralEquipment = queryPeripheralEquipment;
	}
	/**
	 * @return the queryAssetSupportedFunction
	 */
	public String getQueryAssetSupportedFunction() {
		return queryAssetSupportedFunction;
	}
	/**
	 * @param queryAssetSupportedFunction the queryAssetSupportedFunction to set
	 */
	public void setQueryAssetSupportedFunction(String queryAssetSupportedFunction) {
		this.queryAssetSupportedFunction = queryAssetSupportedFunction;
	}
	/**
	 * @return the queryAssetOpenMode
	 */
	public String getQueryAssetOpenMode() {
		return queryAssetOpenMode;
	}
	/**
	 * @param queryAssetOpenMode the queryAssetOpenMode to set
	 */
	public void setQueryAssetOpenMode(String queryAssetOpenMode) {
		this.queryAssetOpenMode = queryAssetOpenMode;
	}
	/**
	 * @return the queryAssetStatus
	 */
	public String getQueryAssetStatus() {
		return queryAssetStatus;
	}
	/**
	 * @param queryAssetStatus the queryAssetStatus to set
	 */
	public void setQueryAssetStatus(String queryAssetStatus) {
		this.queryAssetStatus = queryAssetStatus;
	}
	/**
	 * @return the queryOpenTransaction
	 */
	public String getQueryOpenTransaction() {
		return queryOpenTransaction;
	}
	/**
	 * @param queryOpenTransaction the queryOpenTransaction to set
	 */
	public void setQueryOpenTransaction(String queryOpenTransaction) {
		this.queryOpenTransaction = queryOpenTransaction;
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
	 * @return the edcParameterDTO
	 */
	public EdcParameterDTO getEdcParameterDTO() {
		return edcParameterDTO;
	}
	/**
	 * @param edcParameterDTO the edcParameterDTO to set
	 */
	public void setEdcParameterDTO(EdcParameterDTO edcParameterDTO) {
		this.edcParameterDTO = edcParameterDTO;
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
	/**
	 * @return the companyIdList
	 */
	public List<String> getCompanyIdList() {
		return companyIdList;
	}
	/**
	 * @param companyIdList the companyIdList to set
	 */
	public void setCompanyIdList(List<String> companyIdList) {
		this.companyIdList = companyIdList;
	}
	/**
	 * @return the logonUserCompanyId
	 */
	public String getLogonUserCompanyId() {
		return logonUserCompanyId;
	}
	/**
	 * @param logonUserCompanyId the logonUserCompanyId to set
	 */
	public void setLogonUserCompanyId(String logonUserCompanyId) {
		this.logonUserCompanyId = logonUserCompanyId;
	}
	/**
	 * @return the isNoRoles
	 */
	public Boolean getIsNoRoles() {
		return isNoRoles;
	}
	/**
	 * @param isNoRoles the isNoRoles to set
	 */
	public void setIsNoRoles(Boolean isNoRoles) {
		this.isNoRoles = isNoRoles;
	}

	/**
	 * @return the queryBuiltIn
	 */
	public Boolean getQueryBuiltIn() {
		return queryBuiltIn;
	}
	/**
	 * @param queryBuiltIn the queryBuiltIn to set
	 */
	public void setQueryBuiltIn(Boolean queryBuiltIn) {
		this.queryBuiltIn = queryBuiltIn;
	}
	/**
	 * @return the queryExternal
	 */
	public Boolean getQueryExternal() {
		return queryExternal;
	}
	/**
	 * @param queryExternal the queryExternal to set
	 */
	public void setQueryExternal(Boolean queryExternal) {
		this.queryExternal = queryExternal;
	}
	/**
	 * @return the transactionParameterDTO
	 */
	public SrmCaseNewTransactionParameterDTO getTransactionParameterDTO() {
		return transactionParameterDTO;
	}
	/**
	 * @param transactionParameterDTO the transactionParameterDTO to set
	 */
	public void setTransactionParameterDTO(
			SrmCaseNewTransactionParameterDTO transactionParameterDTO) {
		this.transactionParameterDTO = transactionParameterDTO;
	}
	/**
	 * @return the transactionParameterDTOs
	 */
	public List<SrmCaseNewTransactionParameterDTO> getTransactionParameterDTOs() {
		return transactionParameterDTOs;
	}
	/**
	 * @param transactionParameterDTOs the transactionParameterDTOs to set
	 */
	public void setTransactionParameterDTOs(
			List<SrmCaseNewTransactionParameterDTO> transactionParameterDTOs) {
		this.transactionParameterDTOs = transactionParameterDTOs;
	}
	/**
	 * @return the dtid
	 */
	public String getDtid() {
		return dtid;
	}
	/**
	 * @param dtid the dtid to set
	 */
	public void setDtid(String dtid) {
		this.dtid = dtid;
	}
	/**
	 * @return the transactionParameterItemDTOs
	 */
	public List<SrmTransactionParameterItemDTO> getTransactionParameterItemDTOs() {
		return transactionParameterItemDTOs;
	}
	/**
	 * @param transactionParameterItemDTOs the transactionParameterItemDTOs to set
	 */
	public void setTransactionParameterItemDTOs(
			List<SrmTransactionParameterItemDTO> transactionParameterItemDTOs) {
		this.transactionParameterItemDTOs = transactionParameterItemDTOs;
	}
	/**
	 * @return the srmCaseHandleInfoDTOs
	 */
	public List<SrmCaseHandleInfoDTO> getSrmCaseHandleInfoDTOs() {
		return srmCaseHandleInfoDTOs;
	}
	/**
	 * @param srmCaseHandleInfoDTOs the srmCaseHandleInfoDTOs to set
	 */
	public void setSrmCaseHandleInfoDTOs(
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs) {
		this.srmCaseHandleInfoDTOs = srmCaseHandleInfoDTOs;
	}
}
