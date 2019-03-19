package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterPostCodeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;

/**
 * Purpose: 特店表頭維護FormDTO
 * @author echomou
 * @since JDK 1.6
 * @date 2016/6/8
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("rawtypes")
public class MerchantHeaderFormDTO extends AbstractSimpleListFormDTO<BimMerchantHeaderDTO> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -531485480458611915L;
	//顧客下拉框
	public static final String CUSTOMER_INFO_LIST = "customerInfoList";
	//查詢參數客戶id
	public static final String QUERY_CUSTOMER_ID = "queryCustomerId";
	public static final String QUERY_MERCHANT_CODE = "queryMerchantCode";
	public static final String QUERY_STAGES_MERCHANT_CODE = "queryStagesMerchantCode";
	public static final String QUERY_HEADER_NAME = "queryHeaderName";
	//查詢參數特店名稱
	public static final String QUERY_NAME = "queryName";
	//查詢參數Vip
	public static final String QUERY_IS_VIP			= "queryIsVip";
	//查找公司名稱
	public static final String PARAM_COMPANY_LIST = "getCompanyParameterList";
	
	//查找MId的條件
	public static final String QUERY_MID_CUSTOMER_ID = "queryMIdCustomerId";
	public static final String QUERY_MID_REGISTERED_NAME = "queryMIdRegisteredName";
	public static final String QUERY_MID_ADDRESS = "queryMIdAddress";
	
	//
	public static final String QUERY_MERCODE_CUSTOMER_ID = "queryMerCodeCustomerId";
	public static final String QUERY_MERCODE_REGISTERED_NAME = "queryMerCodeRegisteredName";
	
	public String queryMerCodeCustomerId;
	public String queryMerCodeRegisteredName;
	
	/**
	 * 找MId的條件：客戶
	 */
	public String queryMIdCustomerId;
	/**
	 * 找MId的條件：註冊名稱
	 */
	public String queryMIdRegisteredName;
	/**
	 * 找MId的條件：特店地址
	 */
	public String queryMIdAddress;
	/**
	 * 
	 */
	public String queryMId;
	/**
	 * 數據總筆數
	 */
	private int totalSize;

	/**
	 * 當前頁碼
	 */
	private int page;

	/**
	 * 一頁顯示多少筆
	 */
	private int rows;

	/**
	 * 按此字段進行排序
	 */
	private String sort;

	/**
	 * 排序方式
	 */
	private String order;

	/**
	 * 主鍵
	 */
	private String merchantHeaderId;
	
	/**
	 * 客戶特店編號
	 */
	private String merchantId;

	/**
	 * 查詢條件：客戶
	 */
	private String queryCustomerId;
	private String queryMerchantCode;
	private String queryStagesMerchantCode;
	private String queryHeaderName;
	/**
	 * Task #3583 當前登入者角色屬性
	 */
	private String roleAttribute;
	
	public String getQueryStagesMerchantCode() {
		return queryStagesMerchantCode;
	}

	public void setQueryStagesMerchantCode(String queryStagesMerchantCode) {
		this.queryStagesMerchantCode = queryStagesMerchantCode;
	}

	/**
	 * 查詢條件：VIP
	 */
	private String queryIsVip;

	/**
	 * 查詢條件：特店名稱
	 */
	private String queryName;
	/**
	 * 特店編號
	 */
	private String merchantCode;
	/**
	 * 縣市id
	 */
	private String cityId;
    /**
     * 
     */
	private String customerId;
	private String registeredName;
	private String headerName;
	/**
	 * 特店表頭維護DTO
	 */
	private BimMerchantHeaderDTO merchantHeaderDTO;

	/**
	 * 特店表頭維護DTO集合，供查詢數據時使用
	 */
	private List<BimMerchantHeaderDTO> merchantHeaderList;
	
	/**
	 * 特店表頭維護DTO集合，供查詢數據時使用
	 */
	private List<MerchantDTO> merchantList;
	/**
	 * 客戶信息列表
	 */
	private List<Parameter> customerInfoList;
	/**
	 * 郵遞區號DTO集合，供查詢數據時使用
	 */
	private List<BaseParameterPostCodeDTO> baseParameterPostCodeList;

	/**
	 * Constructor:無參構造
	 */
	public MerchantHeaderFormDTO() {
		super();
	}

	/**
	 * Constructor:
	 */
	public MerchantHeaderFormDTO(Integer totalSize, Integer page, Integer rows,
			String sort, String order, String merchantHeaderId,
			String merchantId, String queryCustomerId, String queryIsVip,
			String queryRegisteredName, String mid,
			BimMerchantHeaderDTO merchantHeaderDTO,
			List<BimMerchantHeaderDTO> merchantHeaderList,
			List<Parameter> customerInfoList,
			String cityId, List<BaseParameterPostCodeDTO> baseParameterPostCodeList) {
		this.totalSize = totalSize;
		this.page = page;
		this.rows = rows;
		this.sort = sort;
		this.order = order;
		this.merchantHeaderId = merchantHeaderId;
		this.merchantId = merchantId;
		this.queryCustomerId = queryCustomerId;
		this.queryIsVip = queryIsVip;
		this.queryName = queryRegisteredName;
		this.merchantCode = mid;
		this.merchantHeaderDTO = merchantHeaderDTO;
		this.merchantHeaderList = merchantHeaderList;
		this.customerInfoList = customerInfoList;
		this.cityId = cityId;
		this.baseParameterPostCodeList = baseParameterPostCodeList;
	}
	
	/**
	 * @return the queryMId
	 */
	public String getQueryMId() {
		return queryMId;
	}

	/**
	 * @param queryMId the queryMId to set
	 */
	public void setQueryMId(String queryMId) {
		this.queryMId = queryMId;
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
	 * @return the merchantHeaderId
	 */
	public String getMerchantHeaderId() {
		return merchantHeaderId;
	}

	/**
	 * @param merchantHeaderId the merchantHeaderId to set
	 */
	public void setMerchantHeaderId(String merchantHeaderId) {
		this.merchantHeaderId = merchantHeaderId;
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
	 * @return the queryIsVip
	 */
	public String getQueryIsVip() {
		return queryIsVip;
	}

	/**
	 * @param queryIsVip the queryIsVip to set
	 */
	public void setQueryIsVip(String queryIsVip) {
		this.queryIsVip = queryIsVip;
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
	 * @return the merchantCode
	 */
	public String getMerchantCode() {
		return merchantCode;
	}

	/**
	 * @param merchantCode the merchantCode to set
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
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
	 * @return the registeredName
	 */
	public String getRegisteredName() {
		return registeredName;
	}

	/**
	 * @param registeredName the registeredName to set
	 */
	public void setRegisteredName(String registeredName) {
		this.registeredName = registeredName;
	}

	/**
	 * @return the headerName
	 */
	public String getHeaderName() {
		return headerName;
	}

	/**
	 * @param headerName the headerName to set
	 */
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	/**
	 * @return the merchantHeaderDTO
	 */
	public BimMerchantHeaderDTO getMerchantHeaderDTO() {
		return merchantHeaderDTO;
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
	 * @return the queryHeaderName
	 */
	public String getQueryHeaderName() {
		return queryHeaderName;
	}

	/**
	 * @param queryHeaderName the queryHeaderName to set
	 */
	public void setQueryHeaderName(String queryHeaderName) {
		this.queryHeaderName = queryHeaderName;
	}

	/**
	 * @param merchantHeaderDTO the merchantHeaderDTO to set
	 */
	public void setMerchantHeaderDTO(BimMerchantHeaderDTO merchantHeaderDTO) {
		this.merchantHeaderDTO = merchantHeaderDTO;
	}

	
	/**
	 * @return the queryMIdCustomerId
	 */
	public String getQueryMIdCustomerId() {
		return queryMIdCustomerId;
	}

	/**
	 * @param queryMIdCustomerId the queryMIdCustomerId to set
	 */
	public void setQueryMIdCustomerId(String queryMIdCustomerId) {
		this.queryMIdCustomerId = queryMIdCustomerId;
	}

	/**
	 * @return the queryMIdRegisteredName
	 */
	public String getQueryMIdRegisteredName() {
		return queryMIdRegisteredName;
	}

	/**
	 * @param queryMIdRegisteredName the queryMIdRegisteredName to set
	 */
	public void setQueryMIdRegisteredName(String queryMIdRegisteredName) {
		this.queryMIdRegisteredName = queryMIdRegisteredName;
	}

	/**
	 * @return the queryMIdAddress
	 */
	public String getQueryMIdAddress() {
		return queryMIdAddress;
	}

	/**
	 * @param queryMIdAddress the queryMIdAddress to set
	 */
	public void setQueryMIdAddress(String queryMIdAddress) {
		this.queryMIdAddress = queryMIdAddress;
	}

	/**
	 * @return the merchantHeaderList
	 */
	public List<BimMerchantHeaderDTO> getMerchantHeaderList() {
		return merchantHeaderList;
	}

	/**
	 * @param merchantHeaderList the merchantHeaderList to set
	 */
	public void setMerchantHeaderList(List<BimMerchantHeaderDTO> merchantHeaderList) {
		this.merchantHeaderList = merchantHeaderList;
	}

	/**
	 * @return the customerInfoList
	 */
	public List<Parameter> getCustomerInfoList() {
		return customerInfoList;
	}

	/**
	 * @param customerInfoList the customerInfoList to set
	 */
	public void setCustomerInfoList(List<Parameter> customerInfoList) {
		this.customerInfoList = customerInfoList;
	}


	public List<MerchantDTO> getMerchantList() {
		return merchantList;
	}

	public void setMerchantList(List<MerchantDTO> merchantList) {
		this.merchantList = merchantList;
	}

	/**
	 * @return the queryMerCodeCustomerId
	 */
	public String getQueryMerCodeCustomerId() {
		return queryMerCodeCustomerId;
	}

	/**
	 * @param queryMerCodeCustomerId the queryMerCodeCustomerId to set
	 */
	public void setQueryMerCodeCustomerId(String queryMerCodeCustomerId) {
		this.queryMerCodeCustomerId = queryMerCodeCustomerId;
	}

	/**
	 * @return the queryMerCodeRegisteredName
	 */
	public String getQueryMerCodeRegisteredName() {
		return queryMerCodeRegisteredName;
	}

	/**
	 * @param queryMerCodeRegisteredName the queryMerCodeRegisteredName to set
	 */
	public void setQueryMerCodeRegisteredName(String queryMerCodeRegisteredName) {
		this.queryMerCodeRegisteredName = queryMerCodeRegisteredName;
	}
	/**
	 * @return the totalSize
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<BaseParameterPostCodeDTO> getBaseParameterPostCodeList() {
		return baseParameterPostCodeList;
	}

	public void setBaseParameterPostCodeList(
			List<BaseParameterPostCodeDTO> baseParameterPostCodeList) {
		this.baseParameterPostCodeList = baseParameterPostCodeList;
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
