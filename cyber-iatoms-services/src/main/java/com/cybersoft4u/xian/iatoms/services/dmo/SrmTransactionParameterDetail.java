package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterDetailDTO;

// Generated 2016/12/7 �U�� 02:32:52 by Hibernate Tools 3.4.0.CR1

/**
 * SrmTransactionParameterDetail generated by hbm2java
 */
public class SrmTransactionParameterDetail extends DomainModelObject<SrmTransactionParameterDetailId, SrmTransactionParameterDetailDTO> {

	private SrmTransactionParameterDetailId id;
	private SrmTransactionParameterItem srmTransactionParameterItem;
	private String isEdit;
	private String defaultValue;	

	public SrmTransactionParameterDetail() {
	}

	public SrmTransactionParameterDetail(SrmTransactionParameterDetailId id,
			SrmTransactionParameterItem srmTransactionParameterItem) {
		this.id = id;
		this.srmTransactionParameterItem = srmTransactionParameterItem;
	}

	public SrmTransactionParameterDetail(SrmTransactionParameterDetailId id,
			SrmTransactionParameterItem srmTransactionParameterItem,
			String isEdit) {
		this.id = id;
		this.srmTransactionParameterItem = srmTransactionParameterItem;
		this.isEdit = isEdit;		
	}

	public SrmTransactionParameterDetailId getId() {
		return this.id;
	}

	public void setId(SrmTransactionParameterDetailId id) {
		this.id = id;
	}

	public SrmTransactionParameterItem getSrmTransactionParameterItem() {
		return this.srmTransactionParameterItem;
	}

	public void setSrmTransactionParameterItem(
			SrmTransactionParameterItem srmTransactionParameterItem) {
		this.srmTransactionParameterItem = srmTransactionParameterItem;
	}

	public String getIsEdit() {
		return this.isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}