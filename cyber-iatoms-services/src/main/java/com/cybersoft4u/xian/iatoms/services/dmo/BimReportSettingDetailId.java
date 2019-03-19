package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.CompositeIdentifier;

// Generated 2016/8/5 �U�� 01:28:31 by Hibernate Tools 3.4.0.CR1

/**
 * BimReportSettingDetailId generated by hbm2java
 */
public class BimReportSettingDetailId extends CompositeIdentifier {

	private String settingId;
	private String reportDetail;

	public BimReportSettingDetailId() {
	}

	public BimReportSettingDetailId(String settingId, String reportDetail) {
		this.settingId = settingId;
		this.reportDetail = reportDetail;
	}

	public String getSettingId() {
		return this.settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public String getReportDetail() {
		return this.reportDetail;
	}

	public void setReportDetail(String reportDetail) {
		this.reportDetail = reportDetail;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BimReportSettingDetailId))
			return false;
		BimReportSettingDetailId castOther = (BimReportSettingDetailId) other;

		return ((this.getSettingId() == castOther.getSettingId()) || (this
				.getSettingId() != null && castOther.getSettingId() != null && this
				.getSettingId().equals(castOther.getSettingId())))
				&& ((this.getReportDetail() == castOther.getReportDetail()) || (this
						.getReportDetail() != null
						&& castOther.getReportDetail() != null && this
						.getReportDetail().equals(castOther.getReportDetail())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSettingId() == null ? 0 : this.getSettingId().hashCode());
		result = 37
				* result
				+ (getReportDetail() == null ? 0 : this.getReportDetail()
						.hashCode());
		return result;
	}

}
