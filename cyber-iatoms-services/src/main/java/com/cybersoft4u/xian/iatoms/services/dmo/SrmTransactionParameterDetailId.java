package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

// Generated 2016/12/7 �U�� 02:32:52 by Hibernate Tools 3.4.0.CR1

/**
 * SrmTransactionParameterDetailId generated by hbm2java
 */
public class SrmTransactionParameterDetailId extends CompositeIdentifier {

	private String transactionType;
	private String paramterItemId;

	public SrmTransactionParameterDetailId() {
	}

	public SrmTransactionParameterDetailId(String transactionType,
			String paramterItemId) {
		this.transactionType = transactionType;
		this.paramterItemId = paramterItemId;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getParamterItemId() {
		return this.paramterItemId;
	}

	public void setParamterItemId(String paramterItemId) {
		this.paramterItemId = paramterItemId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SrmTransactionParameterDetailId))
			return false;
		SrmTransactionParameterDetailId castOther = (SrmTransactionParameterDetailId) other;

		return ((this.getTransactionType() == castOther.getTransactionType()) || (this
				.getTransactionType() != null
				&& castOther.getTransactionType() != null && this
				.getTransactionType().equals(castOther.getTransactionType())))
				&& ((this.getParamterItemId() == castOther.getParamterItemId()) || (this
						.getParamterItemId() != null
						&& castOther.getParamterItemId() != null && this
						.getParamterItemId().equals(
								castOther.getParamterItemId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getTransactionType() == null ? 0 : this.getTransactionType()
						.hashCode());
		result = 37
				* result
				+ (getParamterItemId() == null ? 0 : this.getParamterItemId()
						.hashCode());
		return result;
	}

}
