package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;


/**
 * Purpose:公司基本訊息維護FormDTO 
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2016年6月16日
 * @MaintenancePersonnel ElvaHe
 */
public class CompanyFormDTO extends AbstractSimpleListFormDTO<CompanyDTO>{

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 7248277857900131394L;
	
	/**
	 * 查詢條件 -- 公司類型 
	 */
	public static final String QUERY_COMPANY_TYPE 				= "queryCompanyType";
	/**
	 * 查詢條件 -- 公司簡稱
	 */
	public static final String QUERY_SHORT_NAME 				= "queryShortName";
	/**
	 * 匯出報表使用的jrxml名稱
	 */
	public static final String REPORT_JRXML_NAME				= "COMPANY_REPORT";
	/**
	 * 查詢結果的排序字段
	 */
	public static final String PARAM_SORT_NAME					= " companyTypeName,companyCode";
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
	 * 總記錄數
	 */
	private Integer totalSize;
	/**
	 * 主鍵
	 */
	private String companyId;
	
	/**
	 * 查詢條件：公司類型
	 */
	private String queryCompanyType;
	/**
	 * 使用者編號
	 */
	private String userId;
	/**
	 * 查詢條件：廠商名稱
	 */
	private String queryShortName;

	/**
	 * 公司基本訊息
	 */
	private CompanyDTO companyDTO;
	/**
	 * 公司類型的集合
	 */
	private List<String> companyTypes;
	/**
	 * 
	 */
	private String account;

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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the queryCompanyType
	 */
	public String getQueryCompanyType() {
		return queryCompanyType;
	}

	/**
	 * @param queryCompanyType the queryCompanyType to set
	 */
	public void setQueryCompanyType(String queryCompanyType) {
		this.queryCompanyType = queryCompanyType;
	}

	/**
	 * @return the queryShortName
	 */
	public String getQueryShortName() {
		return queryShortName;
	}

	/**
	 * @param queryShortName the queryShortName to set
	 */
	public void setQueryShortName(String queryShortName) {
		this.queryShortName = queryShortName;
	}

	/**
	 * @return the companyTypes
	 */
	public List<String> getCompanyTypes() {
		return companyTypes;
	}

	/**
	 * @param companyTypes the companyTypes to set
	 */
	public void setCompanyTypes(List<String> companyTypes) {
		this.companyTypes = companyTypes;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	
}