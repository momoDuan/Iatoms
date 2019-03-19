package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.CompositeIdentifier;

/**
 * 
 * Purpose: 
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/4/13
 * @MaintenancePersonnel Allenchen
 */
public class RolePermissionIdDTO extends CompositeIdentifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2631494246670294282L;
	private String roleId;
	private String permissionId;

	public RolePermissionIdDTO() {
	}

	public RolePermissionIdDTO(String roleId, String permissionId) {
		this.roleId = roleId;
		this.permissionId = permissionId;
	}


	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RolePermissionIdDTO))
			return false;
		RolePermissionIdDTO castOther = (RolePermissionIdDTO) other;

		return (this.roleId == castOther.getRoleId())
				&& ((this.permissionId == castOther.getPermissionId()) || (this
						.permissionId != null
						&& castOther.getPermissionId() != null && this
						.permissionId.equals(castOther.getPermissionId())));
	}


}
