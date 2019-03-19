package com.cybersoft4u.xian.iatoms.services.dmo;

import cafe.core.dmo.DomainModelObject;

import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterPostCodeDTO;

public class BaseParameterPostCode extends DomainModelObject<String,BaseParameterPostCodeDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2004342726985138949L;
	private String postCodeId;
	private String cityId;
	private String postName;
	private String postCode;
	
	public BaseParameterPostCode() {
	}

	public BaseParameterPostCode(String postCodeId, String cityId, String postName, String postCode) {
		this.postCodeId = postCodeId;
		this.cityId = cityId;
		this.postName = postName;
		this.postCode = postCode;
	}

	public String getPostCodeId() {
		return postCodeId;
	}

	public void setPostCodeId(String postCodeId) {
		this.postCodeId = postCodeId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

}
