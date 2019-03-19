package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;

/**
 * 
 * Purpose: 客戶設備總表Controller
 * @author HermanWang
 * @since  JDK 1.7
 * @date   2016年8月2日
 * @MaintenancePersonnel HermanWang
 */
public class CustomerRepoFormDTO extends AbstractSimpleListFormDTO<RepoStatusReportDTO> {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 8564377969138368795L;
	/**
	 * 報表名
	 */
	public static final String EXPORT_JRXML_NAME													= "CUSTOMER_REPOSITORY";
	/**
	 * 子報表名
	 */
	public static final String CUSTOMER_REPOSITORY_SUBREPORT										= "CUSTOMER_REPOSITORY_subreport1";
	/**
	 * 交叉報表
	 */
	public static final String SUBREPORT_DIR														= "SUBREPORT_DIR";
	/**
	 * jrxml名
	 */
	public static final String EXPORT_FILE_NAME														= "jrxml.name.REPORT_NAME_DMM03110";
	/**
	 * 查詢條件客戶
	 */
	public static final String QUERY_PAGE_PARAM_CUSTOMER											= "queryCustomer";
	/**
	 * 查詢條件日期
	 */
	public static final String QUERY_PAGE_PARAM_DATE												= "queryDate";
	/**
	 * 查詢條件類別
	 */
	public static final String QUERY_PAGE_PARAM_MA_TYPES											= "queryMaType";
	/**
	 * EMPTY
	 */
	public static final String QUERY_PAGE_FLAG_EMPTY												= "EMPTY";
	/**
	 * 表名
	 */
	public static final String QUERY_PAGE_TABLE_NAME												= "tableName";
	/**
	 * 排序列
	 */
	public static final String QUERY_SORT															= "shortName, assetTypeName";
	/**
	 * 查詢條件客戶
	 */
	private String queryCustomer;
	/**
	 * 查詢條件日期
	 */
	private String queryDate;
	/**
	 * 年
	 */
	private String yyyyMM;
	/**
	 * 查詢條件類別
	 */
	private String queryMaType;
	/**
	 * 查詢flag
	 */
	private String queryFlag;
	/**
	 * 狀態
	 */
	private Map<String, String> assetStatus;
	/**
	 * jsons數據
	 */
	private String jsonData;
	/**
	 * 登錄著公司
	 */
	private String logonUserCompanyId;
	/**
	 * 角色屬性
	 */
	private String roleAttribute;
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
	 * 當前時間
	 */
	private Timestamp currentDate;
	/**
	 * 設備類別list
	 */
	private List<String>  assetCategoryKeyList;
	/**
	 * 設備名list
	 */
	private List<String> assetList;
	/**
	 * edc設備list
	 */
	private List<String> assetEDCList;
	/**
	 *交叉報表list
	 */
	private List<CrossTabReportDTO> crossTabReportDTOList;
	/**
	 * jrxml標題map
	 */
	private Map<String, Map<String, List<RepoStatusReportDTO>>> shortNameMap;
	/**
	 * 匯出的客戶id
	 */
	private String exportQueryCustomer;
	/**
	 * @return the crossTabReportDTOList
	 */
	public List<CrossTabReportDTO> getCrossTabReportDTOList() {
		return crossTabReportDTOList;
	}
	/**
	 * @param crossTabReportDTOList the crossTabReportDTOList to set
	 */
	public void setCrossTabReportDTOList(
			List<CrossTabReportDTO> crossTabReportDTOList) {
		this.crossTabReportDTOList = crossTabReportDTOList;
	}
	/**
	 * Constructor: 無參構造
	 */
	public CustomerRepoFormDTO() {

	}
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
	 * @return the queryDate
	 */
	public String getQueryDate() {
		return queryDate;
	}
	/**
	 * @param queryDate the queryDate to set
	 */
	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}
	/**
	 * @return the yyyyMM
	 */
	public String getYyyyMM() {
		return yyyyMM;
	}
	/**
	 * @param yyyyMM the yyyyMM to set
	 */
	public void setYyyyMM(String yyyyMM) {
		this.yyyyMM = yyyyMM;
	}
	/**
	 * @return the queryMaType
	 */
	public String getQueryMaType() {
		return queryMaType;
	}
	/**
	 * @param queryMaType the queryMaType to set
	 */
	public void setQueryMaType(String queryMaType) {
		this.queryMaType = queryMaType;
	}
	/**
	 * @return the queryFlag
	 */
	public String getQueryFlag() {
		return queryFlag;
	}
	/**
	 * @param queryFlag the queryFlag to set
	 */
	public void setQueryFlag(String queryFlag) {
		this.queryFlag = queryFlag;
	}
	/**
	 * @return the assetStatus
	 */
	public Map<String, String> getAssetStatus() {
		return assetStatus;
	}
	/**
	 * @param assetStatus the assetStatus to set
	 */
	public void setAssetStatus(Map<String, String> assetStatus) {
		this.assetStatus = assetStatus;
	}
	/**
	 * @return the jsonData
	 */
	public String getJsonData() {
		return jsonData;
	}
	/**
	 * @param jsonData the jsonData to set
	 */
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
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
	 * @return the currentDate
	 */
	public Timestamp getCurrentDate() {
		return currentDate;
	}
	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(Timestamp currentDate) {
		this.currentDate = currentDate;
	}
	/**
	 * @return the shortNameMap
	 */
	public Map<String, Map<String, List<RepoStatusReportDTO>>> getShortNameMap() {
		return shortNameMap;
	}
	/**
	 * @param shortNameMap the shortNameMap to set
	 */
	public void setShortNameMap(
			Map<String, Map<String, List<RepoStatusReportDTO>>> shortNameMap) {
		this.shortNameMap = shortNameMap;
	}
	/**
	 * @return the assetCategoryKeyList
	 */
	public List<String> getAssetCategoryKeyList() {
		return assetCategoryKeyList;
	}
	/**
	 * @param assetCategoryKeyList the assetCategoryKeyList to set
	 */
	public void setAssetCategoryKeyList(List<String> assetCategoryKeyList) {
		this.assetCategoryKeyList = assetCategoryKeyList;
	}
	/**
	 * @return the assetList
	 */
	public List<String> getAssetList() {
		return assetList;
	}
	/**
	 * @param assetList the assetList to set
	 */
	public void setAssetList(List<String> assetList) {
		this.assetList = assetList;
	}
	/**
	 * @return the assetEDCList
	 */
	public List<String> getAssetEDCList() {
		return assetEDCList;
	}
	/**
	 * @param assetEDCList the assetEDCList to set
	 */
	public void setAssetEDCList(List<String> assetEDCList) {
		this.assetEDCList = assetEDCList;
	}
	/**
	 * @return the exportQueryCustomer
	 */
	public String getExportQueryCustomer() {
		return exportQueryCustomer;
	}
	/**
	 * @param exportQueryCustomer the exportQueryCustomer to set
	 */
	public void setExportQueryCustomer(String exportQueryCustomer) {
		this.exportQueryCustomer = exportQueryCustomer;
	}
	
	
}
