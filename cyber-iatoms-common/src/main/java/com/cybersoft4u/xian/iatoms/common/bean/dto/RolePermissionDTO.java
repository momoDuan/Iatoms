package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;
/**
 * 
 * Purpose: ADM_角色權限檔DTO
 * @author carrieDuan
 * @since  JDK 1.7
 * @date   2016/4/13
 * @MaintenancePersonnel carrieDuan
 */
public class RolePermissionDTO extends DataTransferObject<RolePermissionIdDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5567275260073363427L;

	/**
	 * Purpose: 枚举类
	 * @author Eric_Du
	 * @since  JDK 1.6
	 * @date   2015/8/27
	 * @MaintenancePersonnel Eric_Du
	 */
	public static enum ATTRIBUTE {
		//类别
//		TYPE("type"),
		//项目名称
//		STATUS("status"),
		//客户别
		PERMISSION_ID("permissionId"),
		FUNCTION_ID("functionId"),
		//系统别
		ROLE_ID("roleId"),
		//功能類別
		FUNCTION_CATEGORY("functionCategory");
		private String value;
		ATTRIBUTE(String value) {
			this.value = value;
		};
		public String getValue() {
			return this.value;
		}
	};
	/**
	 * 權限類型
	 */
//	private String type;
	/**
	 * 狀態
	 */
//	private String status;
	/**
	 * 功能權限ID
	 */
	private String permissionId;
	/**角色ID
	 * 
	 */
	private String roleId;
	/**
	 * 功能ID
	 */
	private String functionId;
	/**
	 * 功能類別
	 */
	private String functionCategory;

	/**
	 * Constructor:無參構造函數
	 */
	public RolePermissionDTO() {
	}
	/**
	 * Constructor:有參構造函數
	 */
/*	public RolePermissionDTO(String type, String status, String permissionId,
			String roleId, String functionId) {
		super();
		this.type = type;
		this.status = status;
		this.permissionId = permissionId;
		this.roleId = roleId;
		this.functionId = functionId;
	}*/
	public RolePermissionDTO(String permissionId,
			String roleId, String functionId) {
		this.permissionId = permissionId;
		this.roleId = roleId;
		this.functionId = functionId;
	}
//	/**
//	 * @return the type
//	 */
//	public String getType() {
//		return type;
//	}
//
//	/**
//	 * @param type the type to set
//	 */
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	/**
//	 * @return the status
//	 */
//	public String getStatus() {
//		return status;
//	}
//
//	/**
//	 * @param status the status to set
//	 */
//	public void setStatus(String status) {
//		this.status = status;
//	}

	/**
	 * @return the permissionId
	 */
	public String getPermissionId() {
		return permissionId;
	}

	/**
	 * @param permissionId the permissionId to set
	 */
	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the functionCategory
	 */
	public String getFunctionCategory() {
		return functionCategory;
	}
	/**
	 * @param functionCategory the functionCategory to set
	 */
	public void setFunctionCategory(String functionCategory) {
		this.functionCategory = functionCategory;
	}
	
	
}
