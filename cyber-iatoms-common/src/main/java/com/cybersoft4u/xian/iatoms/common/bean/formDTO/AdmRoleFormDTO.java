package com.cybersoft4u.xian.iatoms.common.bean.formDTO;

import java.util.List;
import java.util.Map;

import cafe.core.bean.dto.AbstractSimpleListFormDTO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmFunctionTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PermissionDTO;

/**
 * Purpose: 系統角色FormDTO
 * @author HaimingWang
 * @since  JDK 1.6
 * @date   2016/6/27
 * @MaintenancePersonnel HaimingWang
 */
public class AdmRoleFormDTO extends AbstractSimpleListFormDTO<AdmRoleDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4991913053305150579L;
	
	/**
	 * 功能權限
	 */
	public static final String PARAM_PERMISSION_LIST = "permissionList";
	public static final String PARAM_LOG_CONTENT = "logContent";
	public static final String PARAM_USER_LOGON_USER = "logonUser";
	public static final String PARAM_USER_UC_NO = "ucNo";
	public static final String PARAM_QUERY_AND_DELETE_LOG = "/LOG/LOG00020_01_AdmRoleQueryAndDelete";
	public static final String PARAM_EDIT_AND_SAVE_LOG = "/LOG/LOG00020_02_AdmUserEditAndSave";
	public static final String PARAM_LIST_ADM_ROLE = "/LOG/LOG00020_01_ListAdmRole";
	public static final String PARAM_DETAIL_ADM_ROLE= "/LOG/LOG00020_02_DetailAdmRole";
	
	

	/**
	 * 靜態常量-查詢參數，角色代號
	 */
	public static final String QUERY_ROLE_CODE = "queryRoleCode";
	
	/**
	 * 靜態常量-查詢參數，角色名稱
	 */
	public static final String QUERY_ROLE_NAME = "queryRoleName";
	
	/**
	 * 靜態常量-系統角色列表
	 */
	public static final String PARAM_ADM_ROLE_CODE_LIST = "admRoleCodeList";

	/**
	 * 靜態常量-系統角色屬性列表
	 */
	public static final String PARAM_ADM_ROLE_ATTRIBUTE_LIST = "admRoleAttributeList";

	
	/**
	 * 靜態常量-功能權限列表
	 */
	public static final String PARAM_ACCESS_RIGHT_LIST = "accessRightList";

	/**
	 * 靜態常量-系統項參數排序-查詢語句中使用
	 */
	public static final String PARAM_SYSTEM_ITEM_ORDER_BY = "SORT";

	/**
	 * 靜態常量-明細頁面加載數據
	 */
	public static final String ACTION_LOAD_DATA = "loadDlgData";
	
	/**
	 * 靜態常量-頁面字段參數
	 */
	public static final String PARAM_PAGE_SORT = "roleCode";	
	
	/**
	 * 靜態常量-模組列表
	 */
	public static final String PARAM_PARENT_FUNCTION_LIST = "parentFunctionList";
	
	
	/**
	 * 查詢參數-角色代號
	 */
	private String queryRoleCode;
	
	/**
	 * 查詢參數-角色名稱
	 */
	private String queryRoleName;
	
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
	 * 角色DTO,新增修改使用
	 */
	private AdmRoleDTO admRoleDTO;
	
	/**
	 * 角色編號
	 */
	private String roleId;
	
	/**
	 * 角色代號，驗證使用
	 */
	private String roleCode;
	
	/**
	 * 角色名稱，驗證使用
	 */
	private String roleName;
	
	/**
	 * 保存时，存储角色的字符串
	 */
	private String role;
	
	/**
	 * 父功能編號
	 */
	private String parentFunctionId;
	
	/**
	 * 功能編號
	 */
	private String functionId;		
	/**
	 * 功能類型dto list
	 */
	private List<AdmFunctionTypeDTO> functionTypeDTOs;
	
	/**
	 * 功能權限DTO list
	 */
	private List<PermissionDTO> permissionDTOs;
	
	/**
	 * 查詢角色代碼名稱
	 */
	private String queryRoleCodeName;
	/**
	 * log詳細內容
	 */
	private String logContent;
	
	/**
	 * 功能權限
	 */
	private Map<String, List<String>> functionPermissions;
	
	/**
	 * Constructor:無參構造
	 */
	public AdmRoleFormDTO() {
	}	

	/**
	 * @return the functionTypeDTOs
	 */
	public List<AdmFunctionTypeDTO> getFunctionTypeDTOs() {
		return functionTypeDTOs;
	}

	/**
	 * @param functionTypeDTOs the functionTypeDTOs to set
	 */
	public void setFunctionTypeDTOs(List<AdmFunctionTypeDTO> functionTypeDTOs) {
		this.functionTypeDTOs = functionTypeDTOs;
	}

	/**
	 * @return the queryRoleCode
	 */
	public String getQueryRoleCode() {
		return queryRoleCode;
	}


	/**
	 * @param queryRoleCode the queryRoleCode to set
	 */
	public void setQueryRoleCode(String queryRoleCode) {
		this.queryRoleCode = queryRoleCode;
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
	 * @return the admRoleDTO
	 */
	public AdmRoleDTO getAdmRoleDTO() {
		return admRoleDTO;
	}

	/**
	 * @param admRoleDTO the admRoleDTO to set
	 */
	public void setAdmRoleDTO(AdmRoleDTO admRoleDTO) {
		this.admRoleDTO = admRoleDTO;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the roleCode
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleCode the roleCode to set
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the parentFunctionId
	 */
	public String getParentFunctionId() {
		return parentFunctionId;
	}

	/**
	 * @param parentFunctionId the parentFunctionId to set
	 */
	public void setParentFunctionId(String parentFunctionId) {
		this.parentFunctionId = parentFunctionId;
	}

	/**
	 * @return the functionId
	 */
	public String getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/**
	 * @return the permissionDTOs
	 */
	public List<PermissionDTO> getPermissionDTOs() {
		return permissionDTOs;
	}

	/**
	 * @param permissionDTOs the permissionDTOs to set
	 */
	public void setPermissionDTOs(List<PermissionDTO> permissionDTOs) {
		this.permissionDTOs = permissionDTOs;
	}

	/**
	 * @return the queryRoleCodeName
	 */
	public String getQueryRoleCodeName() {
		return queryRoleCodeName;
	}

	/**
	 * @param queryRoleCodeName the queryRoleCodeName to set
	 */
	public void setQueryRoleCodeName(String queryRoleCodeName) {
		this.queryRoleCodeName = queryRoleCodeName;
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
	 * @return the functionPermissions
	 */
	public Map<String, List<String>> getFunctionPermissions() {
		return functionPermissions;
	}

	/**
	 * @param functionPermissions the functionPermissions to set
	 */
	public void setFunctionPermissions(Map<String, List<String>> functionPermissions) {
		this.functionPermissions = functionPermissions;
	}
	
	
}
