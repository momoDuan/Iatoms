package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;

/**
 * Purpose: 程式版本維護FormDTO
 * @author echomou
 * @since  JDK 1.6
 * @date   2016-7-11
 * @MaintenancePersonnel echomou
 */
@SuppressWarnings("rawtypes")
public class ApplicationFormDTO extends AbstractSimpleListFormDTO<ApplicationDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 939121202058703185L;
	public static final String CUSTOMER_INFO_LIST 												= "customerInfoList";
	public static final String GET_ASSET_TYPE_LIST 												= "getAssetTypeList";
	public static final String QUERY_COMPANY_ID  												= "queryCompanyId";
	public static final String QUERY_VERSION													= "queryVersion";
	public static final String QUERY_NAME														= "queryName";
	public static final String QUERY_ASSET_TYPE_ID												= "queryAssetTypeId";
	public static final String REPORT_JRXML_NAME												= "APPLICATION_REPORT";
	public static final String REPORT_FILE_NAME													= "程式版本維護";
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
	 * 主鍵
	 */
	private String applicationId;
	
	/**
	 * 設備類別
	 */
	private String assetCategory;
	/**
	 * 查詢條件：客戶編號
	 */
	private String queryCompanyId;
	
	/**
	 * 查詢條件：版本編號
	 */
	private String queryVersion;
	
	/**
	 * 查詢條件：程式名稱
	 */
	private String queryName;
	
	/**
	 * 查詢條件：通用設備編號
	 */
	private String queryAssetTypeId;
	/**
	 * 程式版本
	 */
	private ApplicationDTO applicationDTO;
	/**
	 * 客戶設備類別列
	 */
	private List<Parameter> assetCategoryList;
	/**
	 * 適用設備列表
	 */
	private List<Parameter> assetTypeList;
	/**
	 * Constructor:
	 */
	public ApplicationFormDTO() {
		super();
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
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
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
	 * @return the queryVersion
	 */
	public String getQueryVersion() {
		return queryVersion;
	}

	/**
	 * @param queryVersion the queryVersion to set
	 */
	public void setQueryVersion(String queryVersion) {
		this.queryVersion = queryVersion;
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
	 * @return the applicationDTO
	 */
	public ApplicationDTO getApplicationDTO() {
		return applicationDTO;
	}

	/**
	 * @param applicationDTO the applicationDTO to set
	 */
	public void setApplicationDTO(ApplicationDTO applicationDTO) {
		this.applicationDTO = applicationDTO;
	}

	/**
	 * @return the assetCategory
	 */
	public String getAssetCategory() {
		return assetCategory;
	}

	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	/**
	 * @return the assetTypeList
	 */
	public List<Parameter> getAssetTypeList() {
		return assetTypeList;
	}

	/**
	 * @param assetTypeList the assetTypeList to set
	 */
	public void setAssetTypeList(List<Parameter> assetTypeList) {
		this.assetTypeList = assetTypeList;
	}
	
	/**
	 * @return the assetCategoryList
	 */
	public List<Parameter> getAssetCategoryList() {
		return assetCategoryList;
	}

	/**
	 * @param assetCategoryList the assetCategoryList to set
	 */
	public void setAssetCategoryList(List<Parameter> assetCategoryList) {
		this.assetCategoryList = assetCategoryList;
	}

	/**
	 * @return the queryAssetTypeId
	 */
	public String getQueryAssetTypeId() {
		return queryAssetTypeId;
	}

	/**
	 * @param queryAssetTypeId the queryAssetTypeId to set
	 */
	public void setQueryAssetTypeId(String queryAssetTypeId) {
		this.queryAssetTypeId = queryAssetTypeId;
	}
}
