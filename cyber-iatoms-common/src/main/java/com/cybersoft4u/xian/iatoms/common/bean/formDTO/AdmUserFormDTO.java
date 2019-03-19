package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cybersoft4u.xian.iatoms.common.bean.dto.PasswordSettingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserRoleDTO;

import cafe.core.bean.Parameter;
import cafe.core.bean.dto.AbstractSimpleListFormDTO;

/**
 * Purpose: 使用者FormDTO
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年3月29日
 * @MaintenancePersonnel CrissZhang
 */
public class AdmUserFormDTO extends AbstractSimpleListFormDTO<AdmUserDTO> {	

	/**
	 * 
	 */
	private static final long serialVersionUID = -876501584695122261L;
	/**
	 * 查詢條件：帳號
	 */
	public static final String QUERY_ACCOUNT = "queryAccount";
	/**
	 * 查詢條件：中文姓名
	 */
	public static final String QUERY_CNAME = "queryCname";
	/**
	 * 查詢條件：公司
	 */
	public static final String QUERY_COMPANY = "queryCompany";
	/**
	 * 查詢條件：角色權限
	 */
	public static final String QUERY_ROLE = "queryRole";
	/**
	 * 查詢條件：狀態
	 */
	public static final String QUERY_STATUS = "queryStatus";
	/**
	 * 查詢條件： 部門code
	 */
	public static final String QUERY_DEPT_CODE = "queryDeptCode";
	/**
	 * 角色列表
	 */
	public static final String PARAM_ROLE_LIST = "roleList";
	/**
	 * 角色代碼列表
	 */
	public static final String PARAM_ROLE_CODE_LIST = "roleCodeList";
	/**
	 * 使用者角色列表
	 */
	public static final String PARAM_USER_ROLE_LIST = "userRoleList";
	/**
	 * 部門列表
	 */
	public static final String PARAM_DEPT_LIST = "deptList";
	/**
	 * 維護廠商列表
	 */
	public static final String PARAM_VENDOR_LIST = "vendorList";
	/**
	 * 公司列表
	 */
	public static final String PARAM_COMPANY_LIST = "companyList";
	/**
	 * 帳號狀態列表
	 */
	public static final String PARAM_ACCOUNT_STATUS_LIST = "accountStatusList";
	/**
	 * 排序字段
	 */
	public static final String PARAM_PAGE_SORT = "account";
	/**
	 * 設備管理--領用借用-排序字段
	 */
	public static final String PARAM_PAGE_SORT_REPOSTORY = "company,deptName,account";
	/**
	 * log的actionId
	 */
	public static final String ACTION_OPEN_LOG_DIALOG = "openLogDialog";
	/**
	 * LOG執行內容的查詢頁面
	 */
	public static final String PARAM_LIST_ADM_USER = "/LOG/LOG00010_01_ListAdmUser";
	/**
	 * LOG執行內容的新增編輯頁面
	 */
	public static final String PARAM_EDIT_ADM_USER = "/LOG/LOG00010_02_EditAdmUser";
	/**
	 * 仓库据点列表
	 */
	public static final String PARAM_WAREHOUSE_LIST = "warehouseList";
	
	/**
	 * 使用者控管资料列表
	 */
	public static final String PARAM_USER_WAREHOUSE_LIST = "userWarehouseList";

	/**
	 * 使用者報表文件名稱
	 */
	public static final String PROJECT_REPORT_JRXML_NAME = "ADM_USER_REPORT";
	
	/**
	 * 使用者報表匯出名稱
	 */
	public static final String PROJECT_REPORT_FILE_NAME = "使用者帳號清單";
	
	/**
	 * 登入者信息
	 */
	public static final String PARAM_USER_LOGON_USER = "logonUser";
	
	/**
	 * 功能編號
	 */
	public static final String PARAM_USER_UC_NO = "ucNo";
	
	/**
	 * 行號
	 */
	public static final String PARAM_ROW_INDEX = "rowIndex";
	
	/**
	 * 日誌內容
	 */
	public static final String PARAM_LOG_CONTENT = "logContent";
	
	/**
	 * 上传文档
	 */
	private MultipartFile uploadFiled;
	
	/**
	 * 查詢參數,使用者編號
	 */
	private String queryAccount;
	/**
	 * 查詢參數, 中文名稱
	 */
	private String queryCname;
	/**
	 * 查詢參數, 公司編號
	 */
	private String queryCompany;
	/**
	 * 查詢參數, 角色編號
	 */
	private String queryRole;
	/**
	 * 查詢參數, 帳戶狀態編碼
	 */
	private String queryStatus;
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
	 * 使用者編號
	 */
	private String userId;
	/**
	 * 使用者帐号
	 */
	private String account;
	/**
	 * 使用者DTO,新增修改畫面使用
	 */
	private AdmUserDTO admUserDTO;
	/**
	 * 用戶擁有的角色列表
	 */
	private List<Parameter> roleList;
	/**
	 * 用戶角色權限列表
	 */
	private List<AdmUserRoleDTO> roleAdmUserRoleDTOs;
	/**
	 * 保存時,新增修改操作ActionId
	 */
	private String opActionId;
	/**
	 * 公司編號
	 */
	private String companyId;
	/**
	 * 查詢結果
	 */
	private String queryResultJson;
	/**
	 * 查詢公司名稱
	 */
	private String queryCompanyName;
	/**
	 * 查詢角色權限名稱
	 */
	private String queryRoleName;
	/**
	 * 查詢帳號狀態名稱
	 */
	private String queryStatusName;
	/**
	 * 刪除的行號
	 */
	private String rowIndex;
	/**
	 * 日誌數據
	 */
	private String logContent;
	/**
	 * 查詢參數,部門code
	 */
	private String queryDeptCode;
	/**
	 * 密碼設定
	 */
	private PasswordSettingDTO passwordSettingDTO;
	/**
	 * 是否爲維護工程師查詢
	 */
	private String maintenanceUserFlag;
	/**
	 * 臨時記錄true/false
	 */
	private boolean isTemp;
	/**
	 * Constructor:無參構造函數
	 */
	public AdmUserFormDTO() {
		
	}
	/**
	 * @return the queryAccount
	 */
	public String getQueryAccount() {
		return queryAccount;
	}

	/**
	 * @param queryAccount the queryAccount to set
	 */
	public void setQueryAccount(String queryAccount) {
		this.queryAccount = queryAccount;
	}

	/**
	 * @return the queryCname
	 */
	public String getQueryCname() {
		return queryCname;
	}

	/**
	 * @param queryCname the queryCname to set
	 */
	public void setQueryCname(String queryCname) {
		this.queryCname = queryCname;
	}

	/**
	 * @return the queryCompany
	 */
	public String getQueryCompany() {
		return queryCompany;
	}

	/**
	 * @param queryCompany the queryCompany to set
	 */
	public void setQueryCompany(String queryCompany) {
		this.queryCompany = queryCompany;
	}

	/**
	 * @return the queryRole
	 */
	public String getQueryRole() {
		return queryRole;
	}

	/**
	 * @param queryRole the queryRole to set
	 */
	public void setQueryRole(String queryRole) {
		this.queryRole = queryRole;
	}

	/**
	 * @return the queryStatus
	 */
	public String getQueryStatus() {
		return queryStatus;
	}

	/**
	 * @param queryStatus the queryStatus to set
	 */
	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
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
	 * @return the admUserDTO
	 */
	public AdmUserDTO getAdmUserDTO() {
		return admUserDTO;
	}

	/**
	 * @param admUserDTO the admUserDTO to set
	 */
	public void setAdmUserDTO(AdmUserDTO admUserDTO) {
		this.admUserDTO = admUserDTO;
	}

	/**
	 * @return the roleList
	 */
	public List<Parameter> getRoleList() {
		return roleList;
	}

	/**
	 * @param roleList the roleList to set
	 */
	public void setRoleList(List<Parameter> roleList) {
		this.roleList = roleList;
	}

	/**
	 * @return the roleAdmUserRoleDTOs
	 */
	public List<AdmUserRoleDTO> getRoleAdmUserRoleDTOs() {
		return roleAdmUserRoleDTOs;
	}

	/**
	 * @param roleAdmUserRoleDTOs the roleAdmUserRoleDTOs to set
	 */
	public void setRoleAdmUserRoleDTOs(List<AdmUserRoleDTO> roleAdmUserRoleDTOs) {
		this.roleAdmUserRoleDTOs = roleAdmUserRoleDTOs;
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
	 * @return the opActionId
	 */
	public String getOpActionId() {
		return opActionId;
	}
	/**
	 * @param opActionId the opActionId to set
	 */
	public void setOpActionId(String opActionId) {
		this.opActionId = opActionId;
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
	/**
	 * @return the passwordSettingDTO
	 */
	public PasswordSettingDTO getPasswordSettingDTO() {
		return passwordSettingDTO;
	}
	/**
	 * @param passwordSettingDTO the passwordSettingDTO to set
	 */
	public void setPasswordSettingDTO(PasswordSettingDTO passwordSettingDTO) {
		this.passwordSettingDTO = passwordSettingDTO;
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
	 * @return the queryResultJson
	 */
	public String getQueryResultJson() {
		return queryResultJson;
	}
	/**
	 * @param queryResultJson the queryResultJson to set
	 */
	public void setQueryResultJson(String queryResultJson) {
		this.queryResultJson = queryResultJson;
	}
	/**
	 * @return the queryCompanyName
	 */
	public String getQueryCompanyName() {
		return queryCompanyName;
	}
	/**
	 * @param queryCompanyName the queryCompanyName to set
	 */
	public void setQueryCompanyName(String queryCompanyName) {
		this.queryCompanyName = queryCompanyName;
	}
	/**
	 * @return the queryRoleName
	 */
	public String getQueryRoleName() {
		return queryRoleName;
	}
	/**
	 * @param queryRoleName the queryRoleName to set
	 */
	public void setQueryRoleName(String queryRoleName) {
		this.queryRoleName = queryRoleName;
	}
	/**
	 * @return the queryStatusName
	 */
	public String getQueryStatusName() {
		return queryStatusName;
	}
	/**
	 * @param queryStatusName the queryStatusName to set
	 */
	public void setQueryStatusName(String queryStatusName) {
		this.queryStatusName = queryStatusName;
	}
	/**
	 * @return the rowIndex
	 */
	public String getRowIndex() {
		return rowIndex;
	}
	/**
	 * @param rowIndex the rowIndex to set
	 */
	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}
	/**
	 * @return the logContent
	 */
	public String getLogContent() {
		return logContent;
	}
	/**
	 * @param logContent the logContent to set
	 */
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	/**
	 * @return the queryDeptCode
	 */
	public String getQueryDeptCode() {
		return queryDeptCode;
	}
	/**
	 * @param queryDeptCode the queryDeptCode to set
	 */
	public void setQueryDeptCode(String queryDeptCode) {
		this.queryDeptCode = queryDeptCode;
	}
	/**
	 * @return the maintenanceUserFlag
	 */
	public String getMaintenanceUserFlag() {
		return maintenanceUserFlag;
	}
	/**
	 * @param maintenanceUserFlag the maintenanceUserFlag to set
	 */
	public void setMaintenanceUserFlag(String maintenanceUserFlag) {
		this.maintenanceUserFlag = maintenanceUserFlag;
	}
	/**
	 * @return the isTemp
	 */
	public boolean isTemp() {
		return isTemp;
	}
	/**
	 * @param isTemp the isTemp to set
	 */
	public void setTemp(boolean isTemp) {
		this.isTemp = isTemp;
	}
	
}
