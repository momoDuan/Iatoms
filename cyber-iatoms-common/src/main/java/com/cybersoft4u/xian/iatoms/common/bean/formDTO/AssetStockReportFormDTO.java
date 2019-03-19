package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.sql.Timestamp;
import java.util.List;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetStockReportDTO;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;

/**
 * Purpose:設備庫存表FormDTO 
 * @author CrissZhang
 * @since  JDK 1.7
 * @date   2016-7-28
 * @MaintenancePersonnel CrissZhang
 */
public class AssetStockReportFormDTO extends AbstractSimpleListFormDTO<AssetStockReportDTO> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID = -1211105735227185446L;
	
	/**
	 * 查询条件客户
	 */
	public static final String QUERY_CUSTOMSER_ID = "queryCustomerId"; 

	/**
	 * 查询条件維護模式
	 */
	public static final String QUERY_MAINTAIN_MODE = "queryMaintainMode";
	
	/**
	 * 查询条件查询月份
	 */
	public static final String QUERY_MONTH = "queryMonth"; 
	
	/**
	 * 查询条件查詢報表種類
	 */
	public static final String QUERY_REPORT_TYPE = "queryReportType";
	
	/**
	 * 查询条件是否有角色權限
	 */
	public static final String PARAM_IS_NO_ROLES = "isNoRoles";
	
	/**
	 * 查询条件角色屬性
	 */
	public static final String PARAM_ROLE_ATTRIBUTE = "roleAttribute";
	
	/**
	 * 報表jrxml文件名稱
	 */
	public static final String PROJECT_REPORT_JRXML_DEVICE_STOCK_TABLE_NAME = "DMM_DEVICE_STOCK_TABLE_REPORT";
	
	/**
	 * 生成報表文件名稱
	 */
	public static final String PROJECT_REPORT_FILE_DEVICE_STOCK_TABLE_NAME = "設備庫存表";
	
	/**
	 * 報表表格名稱
	 */
	public static final String PROJECT_REPORT_FILE_STOCK_TABLE_NAME = "庫存表";
	
	/**
	 * 查詢條件--客戶
	 */
	private String queryCustomerId;
	
	/**
	 * 查詢條件--查詢維護模式
	 */
	private String queryMaintainMode;
	
	/**
	 * 查詢條件--查詢月份
	 */
	private String queryMonth;
	
	/**
	 * 查詢條件--查詢報表類型
	 */
	private String queryReportType;
	
	/**
	 * 查詢條件--排序欄位
	 */
	private String sort;
	
	/**
	 * 查詢條件--排序方式
	 */
	private String order;
	
	/**
	 * 當前年月
	 */
	private String currentDate;
	
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
	 * 設備庫存DTO對象
	 */
	private AssetStockReportDTO assetStockReportDTO;
	
	/**
	 * 設備庫存DTO列表對象
	 */
	private List<AssetStockReportDTO> assetStockReportDTOs;
	
	/**
	 * 設備狀態集合
	 */
	private List<Parameter> listAssetCategory;
	/**
	 * Constructor: 無參構造
	 */
	public AssetStockReportFormDTO(){
	}

	/**
	 * @return the listAssetCategory
	 */
	public List<Parameter> getListAssetCategory() {
		return listAssetCategory;
	}

	/**
	 * @param listAssetCategory the listAssetCategory to set
	 */
	public void setListAssetCategory(List<Parameter> listAssetCategory) {
		this.listAssetCategory = listAssetCategory;
	}

	/**
	 * @return the assetStockReportDTO
	 */
	public AssetStockReportDTO getAssetStockReportDTO() {
		return assetStockReportDTO;
	}

	/**
	 * @param assetStockReportDTO the assetStockReportDTO to set
	 */
	public void setAssetStockReportDTO(AssetStockReportDTO assetStockReportDTO) {
		this.assetStockReportDTO = assetStockReportDTO;
	}


	/**
	 * @return the assetStockReportDTOs
	 */
	public List<AssetStockReportDTO> getAssetStockReportDTOs() {
		return assetStockReportDTOs;
	}


	/**
	 * @param assetStockReportDTOs the assetStockReportDTOs to set
	 */
	public void setAssetStockReportDTOs(List<AssetStockReportDTO> assetStockReportDTOs) {
		this.assetStockReportDTOs = assetStockReportDTOs;
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
	 * @return the queryCustomerId
	 */
	public String getQueryCustomerId() {
		return queryCustomerId;
	}


	/**
	 * @return the queryMaintainMode
	 */
	public String getQueryMaintainMode() {
		return queryMaintainMode;
	}


	/**
	 * @return the queryMonth
	 */
	public String getQueryMonth() {
		return queryMonth;
	}


	/**
	 * @return the queryReportType
	 */
	public String getQueryReportType() {
		return queryReportType;
	}


	/**
	 * @param queryCustomerId the queryCustomerId to set
	 */
	public void setQueryCustomerId(String queryCustomerId) {
		this.queryCustomerId = queryCustomerId;
	}


	/**
	 * @param queryMaintainMode the queryMaintainMode to set
	 */
	public void setQueryMaintainMode(String queryMaintainMode) {
		this.queryMaintainMode = queryMaintainMode;
	}


	/**
	 * @param queryMonth the queryMonth to set
	 */
	public void setQueryMonth(String queryMonth) {
		this.queryMonth = queryMonth;
	}


	/**
	 * @param queryReportType the queryReportType to set
	 */
	public void setQueryReportType(String queryReportType) {
		this.queryReportType = queryReportType;
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
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
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

}
