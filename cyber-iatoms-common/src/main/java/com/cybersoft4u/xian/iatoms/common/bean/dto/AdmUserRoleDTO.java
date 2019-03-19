package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/** 
 * Purpose: 用戶角色
 * @author evanliu
 * @since  JDK 1.6
 * @date   2016年6月20日
 * @MaintenancePersonnel evanliu
 */
public class AdmUserRoleDTO extends DataTransferObject<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8202807629225445585L;
	/**
	 * 使用者編號
	 */
	private String userId;
	/**
	 * 角色編號
	 */
	private String roleId;
	/**
	 * Constructor:
	 */
	public AdmUserRoleDTO () {
		
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

}
