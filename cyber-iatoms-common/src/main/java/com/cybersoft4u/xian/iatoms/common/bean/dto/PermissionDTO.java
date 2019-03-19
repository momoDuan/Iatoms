package com.cybersoft4u.xian.iatoms.common.bean.dto;

import cafe.core.bean.dto.DataTransferObject;

/**
 * 
 * Purpose: 权限信息
 * @author Allenchen
 * @since  JDK 1.7
 * @date   2016/4/13
 * @MaintenancePersonnel Allenchen
 */
public class PermissionDTO extends DataTransferObject<String> {
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -4681059478596106881L;

	public static enum ATTRIBUTE {
		FUNCTION_ID("functionId"),
		PERMISSIONID("id"),  
		ACCESS_RIGHT("accessRight");
		String value;
		ATTRIBUTE(String paramTypeName) {
			this.value = paramTypeName;
		}
		public String getValue() {
			return this.value;
		}
	}
	private AdmFunctionTypeDTO functionDTO;
	private String functionId;
	private String accessRight;

	public PermissionDTO() {
	}

	public PermissionDTO(String id) {
		this.id = id;
	}

	/**
	 * Constructor:
	 */
	public PermissionDTO(String id, String functionId,
			String accessRight) {
		super();
		this.id = id;
		this.functionId = functionId;
		this.accessRight = accessRight;
	}

	public String getAccessRight() {
		return this.accessRight;
	}

	public void setAccessRight(String accessRight) {
		this.accessRight = accessRight;
	}

	/**
	 * @return the functionDTO
	 */
	public AdmFunctionTypeDTO getFunctionDTO() {
		return functionDTO;
	}

	/**
	 * @param functionDTO the functionDTO to set
	 */
	public void setFunctionDTO(AdmFunctionTypeDTO functionDTO) {
		this.functionDTO = functionDTO;
	}
	/**
	 * @return the functionId
	 */
	public String getFunctionId() {
		return functionId;
	}
	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

}
