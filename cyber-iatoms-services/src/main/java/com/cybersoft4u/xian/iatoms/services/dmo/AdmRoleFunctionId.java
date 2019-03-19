package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

// Generated 2016/6/3 �U�� 01:30:43 by Hibernate Tools 3.4.0.CR1

/**
 * AdmRoleFunctionId generated by hbm2java
 */
public class AdmRoleFunctionId extends CompositeIdentifier implements java.io.Serializable {

	private int roleId;
	private String functionId;

	public AdmRoleFunctionId() {
	}

	public AdmRoleFunctionId(int roleId, String functionId) {
		this.roleId = roleId;
		this.functionId = functionId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getFunctionId() {
		return this.functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AdmRoleFunctionId))
			return false;
		AdmRoleFunctionId castOther = (AdmRoleFunctionId) other;

		return (this.getRoleId() == castOther.getRoleId())
				&& ((this.getFunctionId() == castOther.getFunctionId()) || (this
						.getFunctionId() != null
						&& castOther.getFunctionId() != null && this
						.getFunctionId().equals(castOther.getFunctionId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRoleId();
		result = 37
				* result
				+ (getFunctionId() == null ? 0 : this.getFunctionId()
						.hashCode());
		return result;
	}

}