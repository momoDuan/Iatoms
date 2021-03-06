package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

// Generated 2016/8/10 �U�� 01:29:28 by Hibernate Tools 3.4.0.CR1

/**
 * BimContractTypeId generated by hbm2java
 */
public class BimContractTypeId extends CompositeIdentifier {

	private String contractId;
	private String contractType;

	public BimContractTypeId() {
	}

	public BimContractTypeId(String contractId, String contractType) {
		this.contractId = contractId;
		this.contractType = contractType;
	}

	public String getContractId() {
		return this.contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BimContractTypeId))
			return false;
		BimContractTypeId castOther = (BimContractTypeId) other;

		return ((this.getContractId() == castOther.getContractId()) || (this
				.getContractId() != null && castOther.getContractId() != null && this
				.getContractId().equals(castOther.getContractId())))
				&& ((this.getContractType() == castOther.getContractType()) || (this
						.getContractType() != null
						&& castOther.getContractType() != null && this
						.getContractType().equals(castOther.getContractType())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getContractId() == null ? 0 : this.getContractId()
						.hashCode());
		result = 37
				* result
				+ (getContractType() == null ? 0 : this.getContractType()
						.hashCode());
		return result;
	}

}
