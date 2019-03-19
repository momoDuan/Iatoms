package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractTypeDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractTypeId;

// Generated 2016/8/19 �W�� 11:46:02 by Hibernate Tools 3.4.0.CR1

/**
 * BimCompanyType generated by hbm2java
 */
public class BimCompanyType extends DomainModelObject<BimCompanyTypeId,CompanyTypeDTO> {

	private BimCompanyTypeId id;

	public BimCompanyType() {
	}

	public BimCompanyType(BimCompanyTypeId id) {
		this.id = id;
	}

	public BimCompanyTypeId getId() {
		return this.id;
	}

	public void setId(BimCompanyTypeId id) {
		this.id = id;
	}

}