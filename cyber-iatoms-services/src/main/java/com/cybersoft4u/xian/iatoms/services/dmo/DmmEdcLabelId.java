package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

public class DmmEdcLabelId extends CompositeIdentifier {

	private String merchantCode;
	private String dtid;
	
	public DmmEdcLabelId() {
	}
	
	public DmmEdcLabelId(String merchantCode, String dtid) {
		this.merchantCode = merchantCode;
		this.dtid = dtid;
	}

	public String getMerchantCode() {
		return merchantCode;
	}
	
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	public String getDtid() {
		return dtid;
	}
	
	public void setDtid(String dtid) {
		this.dtid = dtid;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DmmEdcLabelId))
			return false;
		DmmEdcLabelId caseOther = (DmmEdcLabelId) other;
		return ((this.getMerchantCode() == caseOther.getMerchantCode()) ||
				(this.getMerchantCode() != null && caseOther.getMerchantCode() != null &&
				this.getMerchantCode().equals(caseOther.getMerchantCode()))) &&
				((this.getDtid() == caseOther.getDtid()) ||
						(this.getDtid() != null && caseOther.getDtid() != null &&
						this.getDtid().equals(caseOther.getDtid())));
	}
}
