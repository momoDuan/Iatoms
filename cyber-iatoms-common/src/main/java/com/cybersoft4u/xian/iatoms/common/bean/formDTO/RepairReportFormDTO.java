package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.RepairReportDTO;
/**
 * Purpose: 報修問題分析報表FormDTO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016/11/14
 * @MaintenancePersonnel echomou
 */
public class RepairReportFormDTO extends AbstractSimpleListFormDTO<RepairReportDTO> {
	
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -531485480458611915L;
	public static final String SUBREPORT_DIR														= "SUBREPORT_DIR";
	public static final String REPAIR_REPORT_SUBREPORT									        	= "REPAIR_REPORT_subreport";
	public static final String PROJECT_REPORT_JRXML_NAME								            = "REPAIR_REPORT";
	public static final String REPORT_FILE_NAME								                        = "報修問題分析報表";
	
	//查找公司名稱
	public static final String PARAM_COMPANY_LIST = "getCompanyParameterList";
	public static final String PARAM_ALL_COMPANY_LIST = "getAllCompanyList";
	/**
	 * 查詢條件參數
	 */
	public static final String QUERY_CUSTOMER = "queryCustomer";
	public static final String CLOSED_TIME_START = "closedTimeStart";
	public static final String CLOSED_TIME_END = "closedTimeEnd";
	public static final String QUERY_MERCHANT_CODE = "queryMerchantCode";
	public static final String QUERY_EDC_TYPE = "queryEdcType";
	/**
	 * 查詢條件
	 */
	//客戶
	private String queryCustomer;
	//結案時間起
	private String closedTimeStart;
	//結案時間迄
	private String closedTimeEnd;
	//特店代號
	private String queryMerchantCode;
	//刷卡機形
	private String queryEdcType;
	/**
	 * 當前登入者角色屬性
	 */
	private String roleAttribute;
	/**
	 * 當前登入者為客戶屬性對應之公司
	 */
	private String logonUserCompanyId;
	/**
	 * 數據總筆數
	 */
	private Integer totalSize;

	/**
	 * 當前頁碼
	 */
	private Integer page;

	/**
	 * 一頁顯示多少筆
	 */
	private Integer rows;

	/**
	 * 按此字段進行排序
	 */
	private String sort;

	/**
	 * 排序方式
	 */
	private String order;
	/**
	 * 客戶ID集合
	 */
	private List<String> customerIdList;

	/**
	 * @return the queryCustomer
	 */
	public String getQueryCustomer() {
		return queryCustomer;
	}
	/**
	 * @param queryCustomer the queryCustomer to set
	 */
	public void setQueryCustomer(String queryCustomer) {
		this.queryCustomer = queryCustomer;
	}
	/**
	 * @return the closedTimeStart
	 */
	public String getClosedTimeStart() {
		return closedTimeStart;
	}
	/**
	 * @param closedTimeStart the closedTimeStart to set
	 */
	public void setClosedTimeStart(String closedTimeStart) {
		this.closedTimeStart = closedTimeStart;
	}
	/**
	 * @return the closedTimeEnd
	 */
	public String getClosedTimeEnd() {
		return closedTimeEnd;
	}
	/**
	 * @param closedTimeEnd the closedTimeEnd to set
	 */
	public void setClosedTimeEnd(String closedTimeEnd) {
		this.closedTimeEnd = closedTimeEnd;
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
	 * @return the customerIdList
	 */
	public List<String> getCustomerIdList() {
		return customerIdList;
	}
	/**
	 * @param customerIdList the customerIdList to set
	 */
	public void setCustomerIdList(List<String> customerIdList) {
		this.customerIdList = customerIdList;
	}
}
