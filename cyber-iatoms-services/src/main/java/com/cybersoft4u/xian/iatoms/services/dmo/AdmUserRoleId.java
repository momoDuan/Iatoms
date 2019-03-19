package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

// Generated 2016/3/30 �U�� 01:51:49 by Hibernate Tools 3.4.0.CR1

/**
 * AdmUserRoleId generated by hbm2java
 */
public class AdmUserRoleId extends CompositeIdentifier {

	private String userId;
	private String roleId;

	public AdmUserRoleId() {
	}

	public AdmUserRoleId(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AdmUserRoleId))
			return false;
		AdmUserRoleId castOther = (AdmUserRoleId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null && castOther.getUserId() != null && this
				.getUserId().equals(castOther.getUserId())))
				&& (this.getRoleId() == castOther.getRoleId());
	}

}
