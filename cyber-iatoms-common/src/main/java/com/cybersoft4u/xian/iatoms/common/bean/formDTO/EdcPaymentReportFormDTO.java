package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
/**
 * Purpose: EDC維護費用付款報表Formdto
 * @author Hermanwang
 * @since  JDK 1.6
 * @date   2017/8/15
 * @MaintenancePersonnel Hermanwang
 */
public class EdcPaymentReportFormDTO  extends AbstractSimpleListFormDTO<SrmCaseHandleInfoDTO> {
	/**
	 * 序列號
	 */
	private static final long serialVersionUID 										= -568679119810699846L;
	/**
	 * 查詢參數客戶id
	 */
	public static final String QUERY_CUSTOMER 									= "queryCustomer";
	/**
	 * 報表名字
	 */
	public static final String EXPORT_JRXML_NAME								= "EDC維護費用付款報表";
	/**
	 * 裝機jrxml Name
	 */
	public static final String EXPORT_JRXML_INSTALL_NAME						= "INSTALL_REPORT";
	/**
	 * 拆機sheetName
	 */
	public static final String EXPORT_JRXML_INSTALL_SHEET_NAME					= "EDC維護費用付款報表-裝機費用";
	/**
	 * 拆機jrxml Name
	 */
	public static final String EXPORT_JRXML_UNINSTALL_NAME						= "UNINSTALL_REPORT";
	/**
	 * 拆機sheetName
	 */
	public static final String EXPORT_JRXML_UNINSTALL_SHEET_NAME				= "EDC維護費用付款報表-拆機費用";
	/**
	 * 其他案件類型jrxml Name
	 */
	public static final String EXPORT_JRXML_OTHER_CASE_NAME						= "OTHER_CASE_REPORT";
	/**
	 * 其他案件類型sheetName
	 */
	public static final String EXPORT_JRXML_OTHER_CASE_SHEET_NAME				= "EDC維護費用付款報表-其他費用";
	/**
	 * 查核jrxml Name
	 */
	public static final String EXPORT_JRXML_CHECK_NAME							= "CHECK_REPORT";
	/**
	 * 查核sheetName
	 */
	public static final String EXPORT_JRXML_CHECK_SHEET_NAME					= "EDC維護費用付款報表-查核費用";
	
	/**
	 * Constructor: 無參構造
	 */
	public EdcPaymentReportFormDTO(){
		super();
	}
	
	/**
	 * 查尋參數，客戶id
	 */
	private String queryCustomer;
	/**
	 * 登錄著公司
	 */
	private String logonUserCompanyId;
	/**
	 * 角色屬性
	 */
	private String roleAttribute;
	/**
	 * 完成日期開始
	 */
	private String queryCompleteDateStart;
	/**
	 * 完成日期結束
	 */
	private String queryCompleteDateEnd;
	/**
	 * 及時表標記位
	 */
	private String isInstant;
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
	 * 案件類別
	 */
	private String caseCategory;
	/**
	 * 裝機dtoList
	 */
	private List<SrmCaseHandleInfoDTO> installSrmCaseHandleInfoDTOs;
	/**
	 * 拆機dtoList
	 */
	private List<SrmCaseHandleInfoDTO> unInstallSrmCaseHandleInfoDTOs;
	/**
	 * 其他DTO LIST
	 */
	private List<SrmCaseHandleInfoDTO> otherSrmCaseHandleInfoDTOs;
	/**
	 * 查核List
	 */
	private List<SrmCaseHandleInfoDTO> checkSrmCaseHandleInfoDTOs;
	private String exportFlag;
	/**
	 * 匯出的查詢條件客戶
	 */
	private String exportQueryCustomer;
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
	 * @return the queryCompleteDateStart
	 */
	public String getQueryCompleteDateStart() {
		return queryCompleteDateStart;
	}

	/**
	 * @param queryCompleteDateStart the queryCompleteDateStart to set
	 */
	public void setQueryCompleteDateStart(String queryCompleteDateStart) {
		this.queryCompleteDateStart = queryCompleteDateStart;
	}

	/**
	 * @return the queryCompleteDateEnd
	 */
	public String getQueryCompleteDateEnd() {
		return queryCompleteDateEnd;
	}

	/**
	 * @param queryCompleteDateEnd the queryCompleteDateEnd to set
	 */
	public void setQueryCompleteDateEnd(String queryCompleteDateEnd) {
		this.queryCompleteDateEnd = queryCompleteDateEnd;
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
	 * @return the caseCategory
	 */
	public String getCaseCategory() {
		return caseCategory;
	}

	/**
	 * @param caseCategory the caseCategory to set
	 */
	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
	}

	/**
	 * @return the installSrmCaseHandleInfoDTOs
	 */
	public List<SrmCaseHandleInfoDTO> getInstallSrmCaseHandleInfoDTOs() {
		return installSrmCaseHandleInfoDTOs;
	}

	/**
	 * @param installSrmCaseHandleInfoDTOs the installSrmCaseHandleInfoDTOs to set
	 */
	public void setInstallSrmCaseHandleInfoDTOs(
			List<SrmCaseHandleInfoDTO> installSrmCaseHandleInfoDTOs) {
		this.installSrmCaseHandleInfoDTOs = installSrmCaseHandleInfoDTOs;
	}

	/**
	 * @return the unInstallSrmCaseHandleInfoDTOs
	 */
	public List<SrmCaseHandleInfoDTO> getUnInstallSrmCaseHandleInfoDTOs() {
		return unInstallSrmCaseHandleInfoDTOs;
	}

	/**
	 * @param unInstallSrmCaseHandleInfoDTOs the unInstallSrmCaseHandleInfoDTOs to set
	 */
	public void setUnInstallSrmCaseHandleInfoDTOs(
			List<SrmCaseHandleInfoDTO> unInstallSrmCaseHandleInfoDTOs) {
		this.unInstallSrmCaseHandleInfoDTOs = unInstallSrmCaseHandleInfoDTOs;
	}

	/**
	 * @return the otherSrmCaseHandleInfoDTOs
	 */
	public List<SrmCaseHandleInfoDTO> getOtherSrmCaseHandleInfoDTOs() {
		return otherSrmCaseHandleInfoDTOs;
	}

	/**
	 * @param otherSrmCaseHandleInfoDTOs the otherSrmCaseHandleInfoDTOs to set
	 */
	public void setOtherSrmCaseHandleInfoDTOs(
			List<SrmCaseHandleInfoDTO> otherSrmCaseHandleInfoDTOs) {
		this.otherSrmCaseHandleInfoDTOs = otherSrmCaseHandleInfoDTOs;
	}

	/**
	 * @return the checkSrmCaseHandleInfoDTOs
	 */
	public List<SrmCaseHandleInfoDTO> getCheckSrmCaseHandleInfoDTOs() {
		return checkSrmCaseHandleInfoDTOs;
	}

	/**
	 * @param checkSrmCaseHandleInfoDTOs the checkSrmCaseHandleInfoDTOs to set
	 */
	public void setCheckSrmCaseHandleInfoDTOs(
			List<SrmCaseHandleInfoDTO> checkSrmCaseHandleInfoDTOs) {
		this.checkSrmCaseHandleInfoDTOs = checkSrmCaseHandleInfoDTOs;
	}

	/**
	 * @return the isInstant
	 */
	public String getIsInstant() {
		return isInstant;
	}

	/**
	 * @param isInstant the isInstant to set
	 */
	public void setIsInstant(String isInstant) {
		this.isInstant = isInstant;
	}

	/**
	 * @return the exportFlag
	 */
	public String getExportFlag() {
		return exportFlag;
	}

	/**
	 * @param exportFlag the exportFlag to set
	 */
	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
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
