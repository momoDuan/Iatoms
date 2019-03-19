package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.bean.dto.parameter.BaseParameterTypeDefDTO;
import cafe.core.dmo.DomainModelObject;
import cafe.core.dmo.parameter.BaseParameterTypeDefId;

public class BaseParameterTypeDefIAtoms  extends DomainModelObject<BaseParameterTypeDefId, BaseParameterTypeDefDTO> {
	private static final long serialVersionUID = -3767518337253895538L;
	private String name;
	private String valueScopeOperator1;
	private String valueScopeOperator2;
	private String readOnly = "N";
	public BaseParameterTypeDefIAtoms(BaseParameterTypeDefDTO dto) throws Throwable {
		super(dto);
	}

	public BaseParameterTypeDefIAtoms() {
			super();
	}

	public BaseParameterTypeDefIAtoms(BaseParameterTypeDefId id, String name) {
		this.id = id;
		this.name = name;
	}

	public BaseParameterTypeDefIAtoms(BaseParameterTypeDefId id, String name,
			String valueScopeOperator1, String readOnly, String valueScopeOperator2) {
		this.id = id;
		this.name = name;
		this.valueScopeOperator1 = valueScopeOperator1;
		this.valueScopeOperator2 = valueScopeOperator2;
		this.readOnly = readOnly;
		
	}
	public void initiate() {
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the valueScopeOperator1
	 */
	public String getValueScopeOperator1() {
		return valueScopeOperator1;
	}

	/**
	 * @param valueScopeOperator1 the valueScopeOperator1 to set
	 */
	public void setValueScopeOperator1(String valueScopeOperator1) {
		this.valueScopeOperator1 = valueScopeOperator1;
	}

	/**
	 * @return the valueScopeOperator2
	 */
	public String getValueScopeOperator2() {
		return valueScopeOperator2;
	}

	/**
	 * @param valueScopeOperator2 the valueScopeOperator2 to set
	 */
	public void setValueScopeOperator2(String valueScopeOperator2) {
		this.valueScopeOperator2 = valueScopeOperator2;
	}

	/**
	 * @return the readOnly
	 */
	public String getReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	
}
