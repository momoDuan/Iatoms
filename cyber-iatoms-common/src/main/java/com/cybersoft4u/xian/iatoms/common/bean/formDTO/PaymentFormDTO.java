package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
/**
 * Purpose: 求償作業FORMDTO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/6
 * @MaintenancePersonnel CarrieDuan
 */
public class PaymentFormDTO extends AbstractSimpleListFormDTO<SrmPaymentInfoDTO>{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -531485480458611915L;
	/**
	 * 客戶下拉選單
	 */
	public static final String PARAM_COMPANY_LIST 						= "getAllCompanyList";
	/**
	 * 查詢條件-客戶
	 */
	public static final String PARAM_QUERY_COMPANY_ID					= "queryCompanyId";
	/**
	 * 查詢條件-資料狀態
	 */
	public static final String PARAM_QUERY_DATA_STATUS					= "queryDataStatus";
	/**
	 * 查詢條件-賠償方式
	 */
	public static final String PARAM_QUERY_COMPENSATION_METHOD			= "queryCompensationMethod";
	/**
	 * 查詢條件-建安日期起
	 */
	public static final String PARAM_QUERY_CREATE_CASE_DATESTART		= "queryCreateCaseDateStart";
	/**
	 * 查詢條件-建安日期止
	 */
	public static final String PARAM_QUERY_CREATE_CASE_DATE_END			= "queryCreateCaseDateEnd";
	/**
	 * 查詢條件-特店代號
	 */	
	public static final String PARAM_QUERY_MERCHANT_CODE				= "queryMerchantCode";
	/**
	 * 查詢條件-DTID
	 */
	public static final String PARAM_QUERY_DTID							= "queryDtid";
	/**
	 * 查詢條件-TID
	 */
	public static final String PARAM_QUERY_TID							= "queryTid";
	/**
	 * 查詢條件-通報遺失日起
	 */
	public static final String PARAM_QUERY_LOST_DAY_START				= "queryLostDayStart";
	/**
	 * 查詢條件-通報遺失日止
	 */
	public static final String PARAM_QUERY_LOST_DAY_END					= "queryLostDayEnd";
	/**
	 * 查詢條件-設備序號
	 */
	public static final String PARAM_QUERY_SERIAL_NUMBER				= "querySerialNumber";
	/**
	 * 耗材分類下拉列表
	 */
	public static final String PARAM_SUPPLIES_TYPE_STRING				= "suppliesTypeString";
	/**
	 * 求償方式下拉列表JSON格式
	 */
	public static final String PARAM_PAY_TYPE_STRING					= "payTypeString";
	
	/**
	 * 排序列
	 */
	public static final String PARAM_SORT								= "paymentId, item.PAYMENT_ITEM, type.ASSET_CATEGORY, itemName";
	/**
	 * 
	 */
	public static final String PARAM_DTID_SORT							= "DTID, caseId";
	/**
	 * 查詢條件-獲取DTID--案件編號
	 */
	public static final String PARAM_DTID_QUERY_CASE_ID					= "dtidQueryCaseId";
	/**
	 * 查詢條件-獲取DTID--DTID
	 */
	public static final String PARAM_DTID_QUERY_DTID					= "dtidQueryDtid";
	/**
	 * 查詢條件-獲取DTID--TID
	 */
	public static final String PARAM_DTID_QUERY_TID						= "dtidQueryTid";
	/**
	 * 查詢條件-獲取DTID-設備序號
	 */
	public static final String PARAM_DTID_QUERY_SERIAL_NUMBER			= "dtidQuerySerialNumber";
	/**
	 * 查詢條件-獲取DTID-特店代號
	 */	
	public static final String PARAM_DTID_QUERY_MERCHANT_CODE			= "dtidQueryMerchantCode";
	/**
	 * 查詢條件-獲取DTID--客戶
	 */
	public static final String PARAM_DTID_QUERY_COMPANY_ID				= "dtidQueryCompanyId";
	/**
	 * 下拉列表--耗材品項維護
	 */
	public static final String PARAM_SUPPLIES_TYPE_NAME_LIST			= "suppliesTypeNameList";
	/**
	 * 
	 */
	public static final String PARAM_SEND_SUPPLIES_LIST					= "suppliesList";
	/**
	 * 郵件主題模板
	 */
	public static final String PARAM_PAYMENT_MAIL_THEME					= "paymentMailTheme.txt";
	/**
	 * 郵件內容模板
	 */
	public static final String PARAM_PAYMENT_MAIL_CONTEXT				= "paymentMailContext.html";
	/**
	 * 求償報表文件名稱
	 */
	public static final String PROJECT_REPORT_JRXML_NAME				= "CLAIM_WORK_REPORT";
	/**
	 * 求償報表匯出名稱
	 */
	public static final String PROJECT_REPORT_FILE_NAME					= "求償作業";
	/**
	 * 客戶
	 */
	private String queryCompanyId;
	/**
	 * 資料狀態
	 */
	private String queryDataStatus;
	/**
	 * 建安日期止
	 */
	private String queryCreateCaseDateEnd;
	/**
	 * 建安日期起
	 */
	private String queryCreateCaseDateStart;
	/**
	 * 賠償方式
	 */
	private String queryCompensationMethod;
	/**
	 * 特店代號
	 */
	private String queryMerchantCode;
	/**
	 * DTID
	 */
	private String queryDtid;
	/**
	 * TID
	 */
	private String queryTid;
	/**
	 * 設備序號
	 */
	private String querySerialNumber;
	/**
	 * 通報遺失日起
	 */
	private String queryLostDayStart;
	/**
	 * 通報遺失日止
	 */
	private String queryLostDayEnd;
	/**
	 * 排序列
	 */
	private String sort;
	/**
	 * 排序方式
	 */
	private String order;
	/**
	 * 每頁顯示條數
	 */
	private Integer rows;
	
	/**
	 * 當前頁碼
	 */
	private Integer page;
	/**
	 * 查詢條件-獲取DTID--案件編號
	 */
	private String dtidQueryCaseId;
	/**
	 *  查詢條件-獲取DTID--DTID
	 */
	private String dtidQueryDtid;
	/**
	 * 查詢條件-獲取DTID--TID
	 */
	private String dtidQueryTid;
	/**
	 * 查詢條件-設備序號
	 */
	private String dtidQuerySerialNumber;
	/**
	 * 查詢條件-獲取DTID-特店代號
	 */
	private String dtidQueryMerchantCode;
	/**
	 * 查詢條件-獲取DTID--客戶
	 */
	private String dtidQueryCompanyId;
	/**
	 * id
	 */
	private String historyId;
	/**
	 * 設備清單-String 
	 */
	private String assetTableValue;
	/**
	 * 設備清單-集合
	 */
	private List<SrmPaymentItemDTO> assetItemDTOs;
	/**
	 * 設備清單-集合
	 */
	private List<SrmPaymentInfoDTO> assetItemInfoDTOs;
	/**
	 * 耗材清單-String
	 */
	private String peripheralSuppliesTableValue;
	/**
	 * 耗材清單-列表
	 */
	private List<SrmPaymentItemDTO> peripheralSuppliesList;
	/**
	 * 耗材清單-列表
	 */
	private List<SrmPaymentInfoDTO> peripheralSuppliesInfoList;
	/**
	 * 求償DTO
	 */
	private SrmPaymentInfoDTO paymentInfoDTO;
	/**
	 * 當前登錄者角色
	 */
	private List<String> roleUsers;
	/**
	 * 需要操作的資料ID
	 */
	private String updateId;
	/**
	 * 求償編號
	 */
	private String paymentId;
	/**
	 * 修改案件類型所處狀態
	 */
	private String saveStatus;
	/**
	 * 修改時間
	 */
	private Long oldUpdatedDate;
	/**
	 * 是否微型商戶
	 */
	private boolean isMicro;
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
	 * @return the queryDataStatus
	 */
	public String getQueryDataStatus() {
		return queryDataStatus;
	}
	/**
	 * @param queryDataStatus the queryDataStatus to set
	 */
	public void setQueryDataStatus(String queryDataStatus) {
		this.queryDataStatus = queryDataStatus;
	}
	/**
	 * @return the queryCreateCaseDateEnd
	 */
	public String getQueryCreateCaseDateEnd() {
		return queryCreateCaseDateEnd;
	}
	/**
	 * @param queryCreateCaseDateEnd the queryCreateCaseDateEnd to set
	 */
	public void setQueryCreateCaseDateEnd(String queryCreateCaseDateEnd) {
		this.queryCreateCaseDateEnd = queryCreateCaseDateEnd;
	}
	/**
	 * @return the queryCreateCaseDateStart
	 */
	public String getQueryCreateCaseDateStart() {
		return queryCreateCaseDateStart;
	}
	/**
	 * @param queryCreateCaseDateStart the queryCreateCaseDateStart to set
	 */
	public void setQueryCreateCaseDateStart(String queryCreateCaseDateStart) {
		this.queryCreateCaseDateStart = queryCreateCaseDateStart;
	}
	/**
	 * @return the queryCompensationMethod
	 */
	public String getQueryCompensationMethod() {
		return queryCompensationMethod;
	}
	/**
	 * @param queryCompensationMethod the queryCompensationMethod to set
	 */
	public void setQueryCompensationMethod(String queryCompensationMethod) {
		this.queryCompensationMethod = queryCompensationMethod;
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
	 * @return the querySerialNumber
	 */
	public String getQuerySerialNumber() {
		return querySerialNumber;
	}
	/**
	 * @param querySerialNumber the querySerialNumber to set
	 */
	public void setQuerySerialNumber(String querySerialNumber) {
		this.querySerialNumber = querySerialNumber;
	}
	/**
	 * @return the queryLostDayStart
	 */
	public String getQueryLostDayStart() {
		return queryLostDayStart;
	}
	/**
	 * @param queryLostDayStart the queryLostDayStart to set
	 */
	public void setQueryLostDayStart(String queryLostDayStart) {
		this.queryLostDayStart = queryLostDayStart;
	}
	/**
	 * @return the queryLostDayEnd
	 */
	public String getQueryLostDayEnd() {
		return queryLostDayEnd;
	}
	/**
	 * @param queryLostDayEnd the queryLostDayEnd to set
	 */
	public void setQueryLostDayEnd(String queryLostDayEnd) {
		this.queryLostDayEnd = queryLostDayEnd;
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
	 * @return the dtidQueryCaseId
	 */
	public String getDtidQueryCaseId() {
		return dtidQueryCaseId;
	}
	/**
	 * @param dtidQueryCaseId the dtidQueryCaseId to set
	 */
	public void setDtidQueryCaseId(String dtidQueryCaseId) {
		this.dtidQueryCaseId = dtidQueryCaseId;
	}
	/**
	 * @return the dtidQueryDtid
	 */
	public String getDtidQueryDtid() {
		return dtidQueryDtid;
	}
	/**
	 * @param dtidQueryDtid the dtidQueryDtid to set
	 */
	public void setDtidQueryDtid(String dtidQueryDtid) {
		this.dtidQueryDtid = dtidQueryDtid;
	}
	/**
	 * @return the dtidQueryTid
	 */
	public String getDtidQueryTid() {
		return dtidQueryTid;
	}
	/**
	 * @param dtidQueryTid the dtidQueryTid to set
	 */
	public void setDtidQueryTid(String dtidQueryTid) {
		this.dtidQueryTid = dtidQueryTid;
	}
	/**
	 * @return the dtidQuerySerialNumber
	 */
	public String getDtidQuerySerialNumber() {
		return dtidQuerySerialNumber;
	}
	/**
	 * @param dtidQuerySerialNumber the dtidQuerySerialNumber to set
	 */
	public void setDtidQuerySerialNumber(String dtidQuerySerialNumber) {
		this.dtidQuerySerialNumber = dtidQuerySerialNumber;
	}
	/**
	 * @return the dtidQueryMerchantCode
	 */
	public String getDtidQueryMerchantCode() {
		return dtidQueryMerchantCode;
	}
	/**
	 * @param dtidQueryMerchantCode the dtidQueryMerchantCode to set
	 */
	public void setDtidQueryMerchantCode(String dtidQueryMerchantCode) {
		this.dtidQueryMerchantCode = dtidQueryMerchantCode;
	}
	/**
	 * @return the dtidQueryCompanyId
	 */
	public String getDtidQueryCompanyId() {
		return dtidQueryCompanyId;
	}
	/**
	 * @param dtidQueryCompanyId the dtidQueryCompanyId to set
	 */
	public void setDtidQueryCompanyId(String dtidQueryCompanyId) {
		this.dtidQueryCompanyId = dtidQueryCompanyId;
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
	 * @return the historyId
	 */
	public String getHistoryId() {
		return historyId;
	}
	/**
	 * @param historyId the historyId to set
	 */
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
	/**
	 * @return the assetTableValue
	 */
	public String getAssetTableValue() {
		return assetTableValue;
	}
	/**
	 * @param assetTableValue the assetTableValue to set
	 */
	public void setAssetTableValue(String assetTableValue) {
		this.assetTableValue = assetTableValue;
	}
	/**
	 * @return the assetItemDTOs
	 */
	public List<SrmPaymentItemDTO> getAssetItemDTOs() {
		return assetItemDTOs;
	}
	/**
	 * @param assetItemDTOs the assetItemDTOs to set
	 */
	public void setAssetItemDTOs(List<SrmPaymentItemDTO> assetItemDTOs) {
		this.assetItemDTOs = assetItemDTOs;
	}
	/**
	 * @return the peripheralSuppliesTableValue
	 */
	public String getPeripheralSuppliesTableValue() {
		return peripheralSuppliesTableValue;
	}
	/**
	 * @param peripheralSuppliesTableValue the peripheralSuppliesTableValue to set
	 */
	public void setPeripheralSuppliesTableValue(String peripheralSuppliesTableValue) {
		this.peripheralSuppliesTableValue = peripheralSuppliesTableValue;
	}
	/**
	 * @return the peripheralSuppliesList
	 */
	public List<SrmPaymentItemDTO> getPeripheralSuppliesList() {
		return peripheralSuppliesList;
	}
	/**
	 * @param peripheralSuppliesList the peripheralSuppliesList to set
	 */
	public void setPeripheralSuppliesList(
			List<SrmPaymentItemDTO> peripheralSuppliesList) {
		this.peripheralSuppliesList = peripheralSuppliesList;
	}
	/**
	 * @return the paymentInfoDTO
	 */
	public SrmPaymentInfoDTO getPaymentInfoDTO() {
		return paymentInfoDTO;
	}
	/**
	 * @param paymentInfoDTO the paymentInfoDTO to set
	 */
	public void setPaymentInfoDTO(SrmPaymentInfoDTO paymentInfoDTO) {
		this.paymentInfoDTO = paymentInfoDTO;
	}
	/**
	 * @return the roleUsers
	 */
	public List<String> getRoleUsers() {
		return roleUsers;
	}
	/**
	 * @param roleUsers the roleUsers to set
	 */
	public void setRoleUsers(List<String> roleUsers) {
		this.roleUsers = roleUsers;
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
	 * @return the paymentId
	 */
	public String getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	/**
	 * @return the assetItemInfoDTOs
	 */
	public List<SrmPaymentInfoDTO> getAssetItemInfoDTOs() {
		return assetItemInfoDTOs;
	}
	/**
	 * @param assetItemInfoDTOs the assetItemInfoDTOs to set
	 */
	public void setAssetItemInfoDTOs(List<SrmPaymentInfoDTO> assetItemInfoDTOs) {
		this.assetItemInfoDTOs = assetItemInfoDTOs;
	}
	/**
	 * @return the peripheralSuppliesInfoList
	 */
	public List<SrmPaymentInfoDTO> getPeripheralSuppliesInfoList() {
		return peripheralSuppliesInfoList;
	}
	/**
	 * @param peripheralSuppliesInfoList the peripheralSuppliesInfoList to set
	 */
	public void setPeripheralSuppliesInfoList(
			List<SrmPaymentInfoDTO> peripheralSuppliesInfoList) {
		this.peripheralSuppliesInfoList = peripheralSuppliesInfoList;
	}
	/**
	 * @return the saveStatus
	 */
	public String getSaveStatus() {
		return saveStatus;
	}
	/**
	 * @param saveStatus the saveStatus to set
	 */
	public void setSaveStatus(String saveStatus) {
		this.saveStatus = saveStatus;
	}
	/**
	 * @return the updatedDate
	 */
	public Long getOldUpdatedDate() {
		return oldUpdatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setOldUpdatedDate(Long oldUpdatedDate) {
		this.oldUpdatedDate = oldUpdatedDate;
	}
	/**
	 * @return the isMicro
	 */
	public boolean getIsMicro() {
		return isMicro;
	}

	/**
	 * @param isMicro the isMicro to set
	 */
	public void setIsMicro(boolean isMicro) {
		this.isMicro = isMicro;
	}
	
}
